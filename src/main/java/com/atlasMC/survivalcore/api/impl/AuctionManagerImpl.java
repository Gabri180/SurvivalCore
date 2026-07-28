package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IAuctionManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.AuctionRepository;
import com.atlasMC.survivalcore.models.Auction;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
            notifyPlayer(new UUID(0, previousBidderId), "§6Tu puja fue superada en §e" + auction.getItemName() + " §6por §e$" + amount);
        }

        auction.placeBid(bidder.getPlayerId(), amount);
        economyAPI.removeBalance(bidderUuid, amount);

        Player bidderPlayer = Bukkit.getPlayer(bidderUuid);
        if (bidderPlayer != null) {
            bidderPlayer.sendMessage("§a✓ §6Puja colocada en §e" + auction.getItemName() + " §6por §e$" + amount);
        }

        auctionRepository.updateAuction(auction);
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

        UUID sellerId = new UUID(0, auction.getSellerId());
        if (auction.getCurrentBidderId() != null && auction.getCurrentBidderId() > 0) {
            UUID winnerId = new UUID(0, auction.getCurrentBidderId());
            economyAPI.addBalance(sellerId, auction.getCurrentBid());

            notifyPlayer(sellerId, "§a✓ §6Tu subasta de §e" + auction.getItemName() + " §6se vendió por §e$" + auction.getCurrentBid());
            notifyPlayer(winnerId, "§a✓ §6Ganaste la subasta de §e" + auction.getItemName() + " §6por §e$" + auction.getCurrentBid());
        } else {
            notifyPlayer(sellerId, "§7Tu subasta de §e" + auction.getItemName() + " §7no tuvo pujas");
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

    private void notifyPlayer(UUID uuid, String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            player.sendMessage(message);
        }
    }
}
