package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Auction;

import java.util.UUID;

/**
 * Dev3 - interfaz sin implementacion. Se conecta a BD manana.
 */
public interface IAuctionManager {

    Auction listItem(UUID sellerUuid, String itemName, int quantity, long startPrice);

    boolean placeBid(long auctionId, UUID bidderUuid, long amount);

    void cancelAuction(long auctionId);
}
