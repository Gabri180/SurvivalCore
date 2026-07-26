package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IBountyManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.BountyRepository;
import com.atlasMC.survivalcore.models.Bounty;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BountyManagerImpl implements IBountyManager {

    private final BountyRepository bountyRepository;
    private final PlayerCache playerCache;
    private final EconomyAPI economyAPI;
    private final Map<Long, Bounty> bountiesCache = new HashMap<>();

    public BountyManagerImpl(BountyRepository bountyRepository, PlayerCache playerCache, EconomyAPI economyAPI) {
        this.bountyRepository = bountyRepository;
        this.playerCache = playerCache;
        this.economyAPI = economyAPI;
        bountyRepository.loadActiveBounties(bounties -> {
            bountiesCache.clear();
            bounties.forEach(b -> bountiesCache.put(b.getId(), b));
        });
    }

    @Override
    public Bounty setBounty(UUID createdByUuid, UUID targetUuid, long reward) {
        PlayerProfile creator = playerCache.get(createdByUuid);
        if (creator == null || creator.getBalance() < reward) return null;

        economyAPI.removeBalance(createdByUuid, reward);
        Bounty bounty = Bounty.builder()
                .targetUuid(targetUuid)
                .reward(reward)
                .createdBy(creator.getPlayerId())
                .createdAt(Instant.now())
                .build();
        bountyRepository.createBounty(bounty, id -> {
            bounty.setId(id);
            bountiesCache.put(id, bounty);
        });
        return bounty;
    }

    @Override
    public void claimBounty(long bountyId, UUID killerUuid) {
        Bounty bounty = bountiesCache.get(bountyId);
        if (bounty == null || bounty.isClaimed()) return;

        PlayerProfile killer = playerCache.get(killerUuid);
        if (killer == null) return;

        economyAPI.addBalance(killerUuid, bounty.getReward());
        bountyRepository.claimBounty(bountyId, killer.getPlayerId());
        bountiesCache.remove(bountyId);
    }

    @Override
    public List<Bounty> getActiveBounties() {
        return bountiesCache.values().stream()
                .filter(b -> !b.isClaimed())
                .toList();
    }

    @Override
    public void incrementKillstreak(UUID playerUuid) {
        PlayerProfile profile = playerCache.get(playerUuid);
        if (profile != null) {
        // playerCache.update(profile);
        }
    }
}
