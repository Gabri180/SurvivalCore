package com.atlasMC.survivalcore.models;

import java.time.Instant;
import java.util.UUID;

public class ScheduledAuction {
    private long id;
    private UUID sellerUuid;
    private String itemName;
    private int quantity;
    private long startPrice;
    private Instant scheduledTime;
    private boolean executed;

    public ScheduledAuction() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public UUID getSellerUuid() { return sellerUuid; }
    public void setSellerUuid(UUID sellerUuid) { this.sellerUuid = sellerUuid; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public long getStartPrice() { return startPrice; }
    public void setStartPrice(long startPrice) { this.startPrice = startPrice; }
    public Instant getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(Instant scheduledTime) { this.scheduledTime = scheduledTime; }
    public boolean isExecuted() { return executed; }
    public void setExecuted(boolean executed) { this.executed = executed; }

    public boolean isReady() {
        return !executed && Instant.now().isAfter(scheduledTime);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long id;
        private UUID sellerUuid;
        private String itemName;
        private int quantity;
        private long startPrice;
        private Instant scheduledTime;
        private boolean executed;

        public Builder id(long id) { this.id = id; return this; }
        public Builder sellerUuid(UUID sellerUuid) { this.sellerUuid = sellerUuid; return this; }
        public Builder itemName(String itemName) { this.itemName = itemName; return this; }
        public Builder quantity(int quantity) { this.quantity = quantity; return this; }
        public Builder startPrice(long startPrice) { this.startPrice = startPrice; return this; }
        public Builder scheduledTime(Instant scheduledTime) { this.scheduledTime = scheduledTime; return this; }
        public Builder executed(boolean executed) { this.executed = executed; return this; }

        public ScheduledAuction build() {
            ScheduledAuction obj = new ScheduledAuction();
            obj.id = this.id;
            obj.sellerUuid = this.sellerUuid;
            obj.itemName = this.itemName;
            obj.quantity = this.quantity;
            obj.startPrice = this.startPrice;
            obj.scheduledTime = this.scheduledTime;
            obj.executed = this.executed;
            return obj;
        }
    }
}
