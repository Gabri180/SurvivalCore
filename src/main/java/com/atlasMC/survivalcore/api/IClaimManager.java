package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Claim;

import java.util.UUID;

/**
 * Dev3 - interfaz sin implementacion. Se conecta a
 * {@link com.atlasMC.survivalcore.db.ClaimRepository} manana.
 * Cubre el sistema de raideo: cargas de asedio, resistencia y ventanas de ataque.
 */
public interface IClaimManager {

    Claim getClaimAt(String world, int x, int z);

    boolean isWithinAttackWindow();

    boolean useSiegeCharge(UUID attackerUuid, long claimId);

    void notifyOwnerUnderAttack(long claimId, UUID attackerUuid);

    boolean canBreak(UUID playerUuid, Claim claim);

    boolean canPlace(UUID playerUuid, Claim claim);
}
