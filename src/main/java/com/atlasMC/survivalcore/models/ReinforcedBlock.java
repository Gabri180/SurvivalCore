package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.ReinforcementLevel;
import java.time.Instant;

public class ReinforcedBlock {
    private long id;
    private long claimId;
    private int x;
    private int y;
    private int z;
    private String world;
    private ReinforcementLevel level;
    private long health;
    private long maxHealth;
    private Instant reinforcedAt;

    public ReinforcedBlock() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getClaimId() { return claimId; }
    public void setClaimId(long claimId) { this.claimId = claimId; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getZ() { return z; }
    public void setZ(int z) { this.z = z; }
    public String getWorld() { return world; }
    public void setWorld(String world) { this.world = world; }
    public ReinforcementLevel getLevel() { return level; }
    public void setLevel(ReinforcementLevel level) { this.level = level; }
    public long getHealth() { return health; }
    public void setHealth(long health) { this.health = Math.max(0, health); }
    public long getMaxHealth() { return maxHealth; }
    public void setMaxHealth(long maxHealth) { this.maxHealth = maxHealth; }
    public Instant getReinforcedAt() { return reinforcedAt; }
    public void setReinforcedAt(Instant reinforcedAt) { this.reinforcedAt = reinforcedAt; }

    public boolean isDestroyed() { return health <= 0; }
    public double getHealthPercent() { return (double) health / maxHealth * 100; }

    public void takeDamage(long damage) {
        long reducedDamage = (long) (damage * (1 - level.getDamageReduction()));
        setHealth(health - reducedDamage);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long id;
        private long claimId;
        private int x, y, z;
        private String world;
        private ReinforcementLevel level = ReinforcementLevel.NONE;
        private long health = 100;
        private long maxHealth = 100;
        private Instant reinforcedAt;

        public Builder id(long id) { this.id = id; return this; }
        public Builder claimId(long claimId) { this.claimId = claimId; return this; }
        public Builder x(int x) { this.x = x; return this; }
        public Builder y(int y) { this.y = y; return this; }
        public Builder z(int z) { this.z = z; return this; }
        public Builder world(String world) { this.world = world; return this; }
        public Builder level(ReinforcementLevel level) { this.level = level; return this; }
        public Builder health(long health) { this.health = health; return this; }
        public Builder maxHealth(long maxHealth) { this.maxHealth = maxHealth; return this; }
        public Builder reinforcedAt(Instant reinforcedAt) { this.reinforcedAt = reinforcedAt; return this; }

        public ReinforcedBlock build() {
            ReinforcedBlock obj = new ReinforcedBlock();
            obj.id = this.id;
            obj.claimId = this.claimId;
            obj.x = this.x;
            obj.y = this.y;
            obj.z = this.z;
            obj.world = this.world;
            obj.level = this.level;
            obj.health = this.health;
            obj.maxHealth = this.maxHealth;
            obj.reinforcedAt = this.reinforcedAt;
            return obj;
        }
    }
}
