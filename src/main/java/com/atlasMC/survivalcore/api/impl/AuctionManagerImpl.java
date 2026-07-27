package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IAuctionManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.AuctionRepository;
import com.atlasMC.survivalcore.models.Auction;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.time.Instant;
import java.util.*;

public class AuctionManagerImpl implements IAuctionManager {

    private final AuctionRepository auctionRepository;
    private final PlayerCache playerCache;
    private final EconomyAPI economyAPI;
    private final Map<Long, Auction> auctionsCache = new HashMap<>();

    public AuctionManagerImpl(AuctionRepository auctionRepository, PlayerCache playerCache, EconomyAPI economyAPI) {
        this.auctionRepository = auctionRepository;
        this.playerCache = playerCache;
        this.economyAPI = economyAPI;
        auctionRepository.loadActiveAuctions(auctions -> {
            auctionsCache.clear();
            auctions.forEach(a -> auctionsCache.put(a.getId(), a));
        });
    }

    @Override
    public Auction listItem(UUID sellerUuid, String itemName, int quantity, long startPrice) {
        PlayerProfile profile = playerCache.get(sellerUuid);
        if (profile == null) return null;
        Auction auction = Auction.builder()
                .sellerId(profile.getPlayerId())
                .itemName(itemName)
                .quantity(quantity)
                .startPrice(startPrice)
                .currentPrice(startPrice)
                .startTime(Instant.now())
                .endTime(Instant.now().plusSeconds(86400))
                .build();
        auctionRepository.createAuction(auction, id -> {
            auction.setId(id);
            auctionsCache.put(id, auction);
        });
        return auction;
    }

    @Override
    public boolean placeBid(long auctionId, UUID bidderUuid, long amount) {
        Auction auction = auctionsCache.get(auctionId);
        if (auction == null || auction.isExpired()) return false;

        PlayerProfile bidder = playerCache.get(bidderUuid);
        if (bidder == null || bidder.getBalance() < amount) return false;

        if (amount <= auction.getCurrentBid()) return false;

        Long previousBidderId = auction.getCurrentBidderId();
        if (previousBidderId != null && previousBidderId > 0) {
            economyAPI.addBalance(new UUID(0, previousBidderId), auction.getCurrentBid());
        }

        auction.placeBid(bidder.getPlayerId(), amount);
        economyAPI.removeBalance(bidderUuid, amount);
        return true;
    }

    @Override
    public void cancelAuction(long auctionId) {
        Auction auction = auctionsCache.get(auctionId);
        if (auction == null) return;

        if (auction.getCurrentBidderId() != null && auction.getCurrentBidderId() > 0) {
            economyAPI.addBalance(new UUID(0, auction.getCurrentBidderId()), auction.getCurrentBid());
        }

        auctionRepository.deleteAuction(auctionId);
        auctionsCache.remove(auctionId);
    }

    @Override
    public Auction getAuction(long auctionId) {
        return auctionsCache.get(auctionId);
    }

    @Override
    public Collection<Auction> getActiveAuctions() {
        return auctionsCache.values().stream()
                .filter(a -> !a.isExpired())
                .toList();
    }

    @Override
    public Collection<Auction> getPlayerAuctions(UUID playerUuid) {
        PlayerProfile profile = playerCache.get(playerUuid);
        if (profile == null) return new ArrayList<>();
        return auctionsCache.values().stream()
                .filter(a -> a.getSellerId() == profile.getPlayerId())
                .toList();
    }

    @Override
    public void completeAuction(long auctionId) {
        Auction auction = auctionsCache.get(auctionId);
        if (auction == null || !auction.isExpired()) return;

        if (auction.getCurrentBidderId() != null && auction.getCurrentBidderId() > 0) {
            economyAPI.addBalance(new UUID(0, auction.getSellerId()), auction.getCurrentBid());
        }

        auctionRepository.deleteAuction(auctionId);
        auctionsCache.remove(auctionId);
    }

    @Override
    public long getHighestBid(long auctionId) {
        Auction auction = auctionsCache.get(auctionId);
        return auction != null ? auction.getCurrentBid() : 0;
    }

    @Override
    public UUID getHighestBidder(long auctionId) {
        Auction auction = auctionsCache.get(auctionId);
        return auction != null && auction.getCurrentBidderId() != null
                ? new UUID(0, auction.getCurrentBidderId())
                : null;
    }
}
