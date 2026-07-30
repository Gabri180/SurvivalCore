package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Bounty;

import java.util.Collection;
import java.util.UUID;

public interface IBountyManager {

    Bounty setBounty(UUID createdByUuid, UUID targetUuid, long reward);

    void claimBounty(long bountyId, UUID killerUuid);

    Collection<Bounty> getActiveBounties();

    void incrementKillstreak(UUID playerUuid);

    Bounty getBounty(long bountyId);

    Collection<Bounty> getBountiesOnPlayer(UUID targetUuid);

    Collection<Bounty> getBountiesCreatedBy(UUID playerUuid);

    int getPlayerKillstreak(UUID playerUuid);

    void resetKillstreak(UUID playerUuid);

    long getTotalBountyReward(UUID playerUuid);
}
