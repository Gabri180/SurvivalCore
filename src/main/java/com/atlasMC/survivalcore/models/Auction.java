package com.atlasMC.survivalcore.models;

import org.jetbrains.annotations.Nullable;
import java.time.Instant;

public class Auction {
    private long id;
    private long sellerId;
    private String itemName;
    private long startPrice;
    private long currentBid;
    @Nullable private Long currentBidderId;
    private Instant startTime;
    @Nullable private Instant endTime;
    private int quantity;
    private long currentPrice;

    public Auction() {}
    public Auction(long id, long sellerId, String itemName, long startPrice, long currentBid, @Nullable Long currentBidderId, Instant startTime, @Nullable Instant endTime, int quantity, long currentPrice) {
        this.id = id;
        this.sellerId = sellerId;
        this.itemName = itemName;
        this.startPrice = startPrice;
        this.currentBid = currentBid;
        this.currentBidderId = currentBidderId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getSellerId() { return sellerId; }
    public void setSellerId(long sellerId) { this.sellerId = sellerId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public long getStartPrice() { return startPrice; }
    public void setStartPrice(long startPrice) { this.startPrice = startPrice; }
    public long getCurrentBid() { return currentBid; }
    public void setCurrentBid(long currentBid) { this.currentBid = currentBid; }
    @Nullable public Long getCurrentBidderId() { return currentBidderId; }
    public void setCurrentBidderId(@Nullable Long currentBidderId) { this.currentBidderId = currentBidderId; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    @Nullable public Instant getEndTime() { return endTime; }
    public void setEndTime(@Nullable Instant endTime) { this.endTime = endTime; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public long getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(long currentPrice) { this.currentPrice = currentPrice; }

    public boolean isExpired() { return endTime != null && Instant.now().isAfter(endTime); }
    public void placeBid(long bidderId, long bidAmount) { this.currentBid = bidAmount; this.currentBidderId = bidderId; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long id;
        private long sellerId;
        private String itemName;
        private long startPrice;
        private long currentBid;
        private Long currentBidderId;
        private Instant startTime;
        private Instant endTime;
        private int quantity;
        private long currentPrice;
        public Builder id(long id) { this.id = id; return this; }
        public Builder sellerId(long sellerId) { this.sellerId = sellerId; return this; }
        public Builder itemName(String itemName) { this.itemName = itemName; return this; }
        public Builder startPrice(long startPrice) { this.startPrice = startPrice; return this; }
        public Builder currentBid(long currentBid) { this.currentBid = currentBid; return this; }
        public Builder currentBidderId(Long currentBidderId) { this.currentBidderId = currentBidderId; return this; }
        public Builder startTime(Instant startTime) { this.startTime = startTime; return this; }
        public Builder endTime(Instant endTime) { this.endTime = endTime; return this; }
        public Builder quantity(int quantity) { this.quantity = quantity; return this; }
        public Builder currentPrice(long currentPrice) { this.currentPrice = currentPrice; return this; }
        public Auction build() {
            Auction obj = new Auction();
            obj.id = this.id;
            obj.sellerId = this.sellerId;
            obj.itemName = this.itemName;
            obj.startPrice = this.startPrice;
            obj.currentBid = this.currentBid;
            obj.currentBidderId = this.currentBidderId;
            obj.startTime = this.startTime;
            obj.endTime = this.endTime;
            obj.quantity = this.quantity;
            obj.currentPrice = this.currentPrice;
            return obj;
        }
    }
}
