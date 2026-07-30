package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.PetType;
import java.time.Instant;
import java.util.UUID;

public class Pet {
    private long id;
    private UUID ownerUuid;
    private PetType type;
    private String name;
    private int level;
    private long exp;
    private int health;
    private int maxHealth;
    private boolean active;
    private Instant createdAt;

    public Pet() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public UUID getOwnerUuid() { return ownerUuid; }
    public void setOwnerUuid(UUID ownerUuid) { this.ownerUuid = ownerUuid; }
    public PetType getType() { return type; }
    public void setType(PetType type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    public long getExp() { return exp; }
    public void setExp(long exp) { this.exp = Math.max(0, exp); }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(0, health); }
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public long getExpNeeded() {
        return (long) (1000 * Math.pow(1.5, level - 1));
    }

    public void addExp(long amount) {
        exp += amount;
        while (exp >= getExpNeeded()) {
            exp -= getExpNeeded();
            level++;
            health = maxHealth;
        }
    }

    public double getHealthPercent() {
        return (double) health / maxHealth * 100;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long id;
        private UUID ownerUuid;
        private PetType type;
        private String name;
        private int level = 1;
        private long exp = 0;
        private int health;
        private int maxHealth;
        private boolean active = true;
        private Instant createdAt;

        public Builder id(long id) { this.id = id; return this; }
        public Builder ownerUuid(UUID ownerUuid) { this.ownerUuid = ownerUuid; return this; }
        public Builder type(PetType type) { this.type = type; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder exp(long exp) { this.exp = exp; return this; }
        public Builder health(int health) { this.health = health; return this; }
        public Builder maxHealth(int maxHealth) { this.maxHealth = maxHealth; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public Pet build() {
            Pet obj = new Pet();
            obj.id = this.id;
            obj.ownerUuid = this.ownerUuid;
            obj.type = this.type;
            obj.name = this.name;
            obj.level = this.level;
            obj.exp = this.exp;
            obj.health = this.health != 0 ? this.health : (this.maxHealth != 0 ? this.maxHealth : this.type.getMaxHealth());
            obj.maxHealth = this.maxHealth != 0 ? this.maxHealth : this.type.getMaxHealth();
            obj.active = this.active;
            obj.createdAt = this.createdAt != null ? this.createdAt : Instant.now();
            return obj;
        }
    }
}
