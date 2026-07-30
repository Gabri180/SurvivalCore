package com.atlasMC.survivalcore.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

/**
 * Dev3 - modelo puro, sin persistencia. Se conecta a BD manana.
 */
public class Bounty {

    private long id;
    @NotNull
    private UUID targetUuid;
    private long reward;
    private long createdBy;
    @Nullable
    private Instant createdAt;
    @Nullable
    private Long claimedBy;
    @Nullable
    private Instant claimedAt;

    public Bounty() {
    }

    public Bounty(long id, @NotNull UUID targetUuid, long reward, long createdBy, @Nullable Instant createdAt, @Nullable Long claimedBy, @Nullable Instant claimedAt) {
        this.id = id;
        this.targetUuid = targetUuid;
        this.reward = reward;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.claimedBy = claimedBy;
        this.claimedAt = claimedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @NotNull
    public UUID getTargetUuid() { return targetUuid; }
    public void setTargetUuid(@NotNull UUID targetUuid) { this.targetUuid = targetUuid; }

    public long getReward() { return reward; }
    public void setReward(long reward) { this.reward = reward; }

    public long getCreatedBy() { return createdBy; }
    public void setCreatedBy(long createdBy) { this.createdBy = createdBy; }

    @Nullable
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(@Nullable Instant createdAt) { this.createdAt = createdAt; }

    @Nullable
    public Long getClaimedBy() { return claimedBy; }
    public void setClaimedBy(@Nullable Long claimedBy) { this.claimedBy = claimedBy; }

    @Nullable
    public Instant getClaimedAt() { return claimedAt; }
    public void setClaimedAt(@Nullable Instant claimedAt) { this.claimedAt = claimedAt; }

    public boolean isClaimed() {
        return claimedBy != null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private UUID targetUuid;
        private long reward;
        private long createdBy;
        private Instant createdAt;
        private Long claimedBy;
        private Instant claimedAt;

        public Builder id(long id) { this.id = id; return this; }
        public Builder targetUuid(UUID targetUuid) { this.targetUuid = targetUuid; return this; }
        public Builder reward(long reward) { this.reward = reward; return this; }
        public Builder createdBy(long createdBy) { this.createdBy = createdBy; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder claimedBy(Long claimedBy) { this.claimedBy = claimedBy; return this; }
        public Builder claimedAt(Instant claimedAt) { this.claimedAt = claimedAt; return this; }

        public Bounty build() {
            Bounty obj = new Bounty();
            obj.id = this.id;
            obj.targetUuid = this.targetUuid;
            obj.reward = this.reward;
            obj.createdBy = this.createdBy;
            obj.createdAt = this.createdAt;
            obj.claimedBy = this.claimedBy;
            obj.claimedAt = this.claimedAt;
            return obj;
        }
    }
}
