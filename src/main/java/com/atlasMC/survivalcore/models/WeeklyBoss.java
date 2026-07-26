package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.BossType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Dev3 - modelo puro, sin persistencia. Se conecta a BD manana.
 */
public class WeeklyBoss {

    private long id;
    @NotNull
    private BossType bossType;
    @Nullable
    private Instant spawnTime;
    @NotNull
    private String spawnLocation;
    private int health;
    private int maxHealth;
    @Nullable
    private Long defeatedBy;

    @NotNull
    private List<String> rewards = new ArrayList<>();

    public WeeklyBoss() {
    }

    public WeeklyBoss(long id, @NotNull BossType bossType, @Nullable Instant spawnTime, @NotNull String spawnLocation, int health, int maxHealth, @Nullable Long defeatedBy, @NotNull List<String> rewards) {
        this.id = id;
        this.bossType = bossType;
        this.spawnTime = spawnTime;
        this.spawnLocation = spawnLocation;
        this.health = health;
        this.maxHealth = maxHealth;
        this.defeatedBy = defeatedBy;
        this.rewards = rewards;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @NotNull
    public BossType getBossType() { return bossType; }
    public void setBossType(@NotNull BossType bossType) { this.bossType = bossType; }

    @Nullable
    public Instant getSpawnTime() { return spawnTime; }
    public void setSpawnTime(@Nullable Instant spawnTime) { this.spawnTime = spawnTime; }

    @NotNull
    public String getSpawnLocation() { return spawnLocation; }
    public void setSpawnLocation(@NotNull String spawnLocation) { this.spawnLocation = spawnLocation; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    @Nullable
    public Long getDefeatedBy() { return defeatedBy; }
    public void setDefeatedBy(@Nullable Long defeatedBy) { this.defeatedBy = defeatedBy; }

    @NotNull
    public List<String> getRewards() { return rewards; }
    public void setRewards(@NotNull List<String> rewards) { this.rewards = rewards; }

    public boolean isDefeated() {
        return defeatedBy != null;
    }

    public void damage(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private BossType bossType;
        private Instant spawnTime;
        private String spawnLocation;
        private int health;
        private int maxHealth;
        private Long defeatedBy;
        private List<String> rewards = new ArrayList<>();

        public Builder id(long id) { this.id = id; return this; }
        public Builder bossType(BossType bossType) { this.bossType = bossType; return this; }
        public Builder spawnTime(Instant spawnTime) { this.spawnTime = spawnTime; return this; }
        public Builder spawnLocation(String spawnLocation) { this.spawnLocation = spawnLocation; return this; }
        public Builder health(int health) { this.health = health; return this; }
        public Builder maxHealth(int maxHealth) { this.maxHealth = maxHealth; return this; }
        public Builder defeatedBy(Long defeatedBy) { this.defeatedBy = defeatedBy; return this; }
        public Builder rewards(List<String> rewards) { this.rewards = rewards; return this; }

        public WeeklyBoss build() {
            WeeklyBoss obj = new WeeklyBoss();
            obj.id = this.id;
            obj.bossType = this.bossType;
            obj.spawnTime = this.spawnTime;
            obj.spawnLocation = this.spawnLocation;
            obj.health = this.health;
            obj.maxHealth = this.maxHealth;
            obj.defeatedBy = this.defeatedBy;
            obj.rewards = this.rewards;
            return obj;
        }
    }
}
