package com.atlasMC.survivalcore.models;


import java.time.Instant;

/**
 * Dev3 - modelo puro, sin persistencia. Se conecta a BD manana.
 */
public class SiegeCharge {

    private long id;
    private long attackerId;
    private long claimId;
    private int damage;
    private Instant usedAt;

    public SiegeCharge() {}

    public SiegeCharge(long id, long attackerId, long claimId, int damage, Instant usedAt) {
        this.id = id;
        this.attackerId = attackerId;
        this.claimId = claimId;
        this.damage = damage;
        this.usedAt = usedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getAttackerId() { return attackerId; }
    public void setAttackerId(long attackerId) { this.attackerId = attackerId; }

    public long getClaimId() { return claimId; }
    public void setClaimId(long claimId) { this.claimId = claimId; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public Instant getUsedAt() { return usedAt; }
    public void setUsedAt(Instant usedAt) { this.usedAt = usedAt; }
}
