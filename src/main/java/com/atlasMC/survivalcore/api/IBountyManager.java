package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Bounty;

import java.util.List;
import java.util.UUID;

/**
 * Dev3 - interfaz sin implementacion. Se conecta a BD manana.
 */
public interface IBountyManager {

    Bounty setBounty(UUID createdByUuid, UUID targetUuid, long reward);

    void claimBounty(long bountyId, UUID killerUuid);

    List<Bounty> getActiveBounties();

    void incrementKillstreak(UUID playerUuid);
}
