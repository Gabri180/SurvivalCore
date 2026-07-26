package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IAuctionManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.AuctionRepository;
import com.atlasMC.survivalcore.models.Auction;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        return true;
    }

    @Override
    public void cancelAuction(long auctionId) {
        auctionRepository.deleteAuction(auctionId);
        auctionsCache.remove(auctionId);
    }
}
