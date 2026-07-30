package com.atlasMC.survivalcore.license;

import java.time.Instant;

public class License {
    private String key;
    private String tier; // STARTER, PROFESSIONAL, ENTERPRISE, LIFETIME
    private String serverUuid;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant lastValidatedAt;
    private int maxPlayers;
    private int maxServers;
    private boolean active;

    public License() {}

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public String getServerUuid() { return serverUuid; }
    public void setServerUuid(String serverUuid) { this.serverUuid = serverUuid; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getLastValidatedAt() { return lastValidatedAt; }
    public void setLastValidatedAt(Instant lastValidatedAt) { this.lastValidatedAt = lastValidatedAt; }
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public int getMaxServers() { return maxServers; }
    public void setMaxServers(int maxServers) { this.maxServers = maxServers; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isExpired() {
        return !active || (expiresAt != null && Instant.now().isAfter(expiresAt));
    }

    public boolean isValid() {
        return active && !isExpired();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String key;
        private String tier;
        private String serverUuid;
        private Instant expiresAt;
        private Instant createdAt = Instant.now();
        private Instant lastValidatedAt = Instant.now();
        private int maxPlayers;
        private int maxServers;
        private boolean active = true;

        public Builder key(String key) { this.key = key; return this; }
        public Builder tier(String tier) { this.tier = tier; return this; }
        public Builder serverUuid(String serverUuid) { this.serverUuid = serverUuid; return this; }
        public Builder expiresAt(Instant expiresAt) { this.expiresAt = expiresAt; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder lastValidatedAt(Instant lastValidatedAt) { this.lastValidatedAt = lastValidatedAt; return this; }
        public Builder maxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; return this; }
        public Builder maxServers(int maxServers) { this.maxServers = maxServers; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public License build() {
            License obj = new License();
            obj.key = this.key;
            obj.tier = this.tier;
            obj.serverUuid = this.serverUuid;
            obj.expiresAt = this.expiresAt;
            obj.createdAt = this.createdAt;
            obj.lastValidatedAt = this.lastValidatedAt;
            obj.maxPlayers = this.maxPlayers;
            obj.maxServers = this.maxServers;
            obj.active = this.active;
            return obj;
        }
    }
}
