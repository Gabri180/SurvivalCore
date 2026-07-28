package com.atlasMC.survivalcore.models;

import java.util.UUID;

public class ClanBank {
    private long clanId;
    private long balance;
    private int maxSlots;
    private int usedSlots;

    public ClanBank() {}

    public long getClanId() { return clanId; }
    public void setClanId(long clanId) { this.clanId = clanId; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
    public int getMaxSlots() { return maxSlots; }
    public void setMaxSlots(int maxSlots) { this.maxSlots = maxSlots; }
    public int getUsedSlots() { return usedSlots; }
    public void setUsedSlots(int usedSlots) { this.usedSlots = usedSlots; }

    public boolean canDeposit(long amount) { return balance + amount <= Long.MAX_VALUE; }
    public boolean canWithdraw(long amount) { return balance >= amount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long clanId;
        private long balance;
        private int maxSlots;
        private int usedSlots;

        public Builder clanId(long clanId) { this.clanId = clanId; return this; }
        public Builder balance(long balance) { this.balance = balance; return this; }
        public Builder maxSlots(int maxSlots) { this.maxSlots = maxSlots; return this; }
        public Builder usedSlots(int usedSlots) { this.usedSlots = usedSlots; return this; }

        public ClanBank build() {
            ClanBank obj = new ClanBank();
            obj.clanId = this.clanId;
            obj.balance = this.balance;
            obj.maxSlots = this.maxSlots;
            obj.usedSlots = this.usedSlots;
            return obj;
        }
    }
}
