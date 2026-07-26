package com.atlasMC.survivalcore.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * Dev3 - modelo puro, sin persistencia. Se conecta a BD manana.
 * Representa un terreno protegido con resistencia (power) frente al sistema de asedio.
 */
public class Claim {

    private long id;
    private long ownerId;
    @Nullable
    private Long clanId;
    private int x1;
    private int z1;
    private int x2;
    private int z2;
    @NotNull
    private String world;
    private int power;
    @Nullable
    private Instant lastDamaged;
    @Nullable
    private Instant damageImmunityUntil;

    public Claim() {
    }

    public Claim(long id, long ownerId, @Nullable Long clanId, int x1, int z1, int x2, int z2, @NotNull String world, int power, @Nullable Instant lastDamaged, @Nullable Instant damageImmunityUntil) {
        this.id = id;
        this.ownerId = ownerId;
        this.clanId = clanId;
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
        this.world = world;
        this.power = power;
        this.lastDamaged = lastDamaged;
        this.damageImmunityUntil = damageImmunityUntil;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    @Nullable
    public Long getClanId() { return clanId; }
    public void setClanId(@Nullable Long clanId) { this.clanId = clanId; }

    public int getX1() { return x1; }
    public void setX1(int x1) { this.x1 = x1; }

    public int getZ1() { return z1; }
    public void setZ1(int z1) { this.z1 = z1; }

    public int getX2() { return x2; }
    public void setX2(int x2) { this.x2 = x2; }

    public int getZ2() { return z2; }
    public void setZ2(int z2) { this.z2 = z2; }

    @NotNull
    public String getWorld() { return world; }
    public void setWorld(@NotNull String world) { this.world = world; }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    @Nullable
    public Instant getLastDamaged() { return lastDamaged; }
    public void setLastDamaged(@Nullable Instant lastDamaged) { this.lastDamaged = lastDamaged; }

    @Nullable
    public Instant getDamageImmunityUntil() { return damageImmunityUntil; }
    public void setDamageImmunityUntil(@Nullable Instant damageImmunityUntil) { this.damageImmunityUntil = damageImmunityUntil; }

    public boolean isImmune() {
        return damageImmunityUntil != null && Instant.now().isBefore(damageImmunityUntil);
    }

    public boolean isBroken() {
        return power <= 0;
    }

    public void applyDamage(int amount) {
        this.power = Math.max(0, this.power - amount);
        this.lastDamaged = Instant.now();
    }

    public boolean contains(int x, int z) {
        return x >= Math.min(x1, x2) && x <= Math.max(x1, x2)
                && z >= Math.min(z1, z2) && z <= Math.max(z1, z2);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private long ownerId;
        private Long clanId;
        private int x1;
        private int z1;
        private int x2;
        private int z2;
        private String world;
        private int power;
        private Instant lastDamaged;
        private Instant damageImmunityUntil;

        public Builder id(long id) { this.id = id; return this; }
        public Builder ownerId(long ownerId) { this.ownerId = ownerId; return this; }
        public Builder clanId(Long clanId) { this.clanId = clanId; return this; }
        public Builder x1(int x1) { this.x1 = x1; return this; }
        public Builder z1(int z1) { this.z1 = z1; return this; }
        public Builder x2(int x2) { this.x2 = x2; return this; }
        public Builder z2(int z2) { this.z2 = z2; return this; }
        public Builder world(String world) { this.world = world; return this; }
        public Builder power(int power) { this.power = power; return this; }
        public Builder lastDamaged(Instant lastDamaged) { this.lastDamaged = lastDamaged; return this; }
        public Builder damageImmunityUntil(Instant damageImmunityUntil) { this.damageImmunityUntil = damageImmunityUntil; return this; }

        public Claim build() {
            Claim obj = new Claim();
            obj.id = this.id;
            obj.ownerId = this.ownerId;
            obj.clanId = this.clanId;
            obj.x1 = this.x1;
            obj.z1 = this.z1;
            obj.x2 = this.x2;
            obj.z2 = this.z2;
            obj.world = this.world;
            obj.power = this.power;
            obj.lastDamaged = this.lastDamaged;
            obj.damageImmunityUntil = this.damageImmunityUntil;
            return obj;
        }
    }
}
