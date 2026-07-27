package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Auction;

import java.util.Collection;
import java.util.UUID;

public interface IAuctionManager {

    Auction listItem(UUID sellerUuid, String itemName, int quantity, long startPrice);

    boolean placeBid(long auctionId, UUID bidderUuid, long amount);

    void cancelAuction(long auctionId);

    Auction getAuction(long auctionId);

    Collection<Auction> getActiveAuctions();

    Collection<Auction> getPlayerAuctions(UUID playerUuid);

    void completeAuction(long auctionId);

    long getHighestBid(long auctionId);

    UUID getHighestBidder(long auctionId);
}
