package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IBountyManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.BountyRepository;
import com.atlasMC.survivalcore.models.Bounty;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.*;

public class BountyManagerImpl implements IBountyManager {

    private final BountyRepository bountyRepository;
    private final PlayerCache playerCache;
    private final EconomyAPI economyAPI;
    private final Map<Long, Bounty> bountiesCache = new HashMap<>();
    private final Map<UUID, Integer> playerKillstreaks = new HashMap<>();
    private final Map<UUID, Long> playerBountyEarnings = new HashMap<>();

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

        Player target = Bukkit.getPlayer(targetUuid);
        if (target != null) {
            target.sendMessage("§c⚠ §6Hay una recompensa de §e$" + reward + " §6sobre tu cabeza!");
        }

        return bounty;
    }

    @Override
    public void claimBounty(long bountyId, UUID killerUuid) {
        Bounty bounty = bountiesCache.get(bountyId);
        if (bounty == null || bounty.isClaimed()) return;

        PlayerProfile killer = playerCache.get(killerUuid);
        if (killer == null) return;

        economyAPI.addBalance(killerUuid, bounty.getReward());
        playerBountyEarnings.put(killerUuid, playerBountyEarnings.getOrDefault(killerUuid, 0L) + bounty.getReward());

        Player killerPlayer = Bukkit.getPlayer(killerUuid);
        if (killerPlayer != null) {
            killerPlayer.sendMessage("§a✓ §6Recompensa cobrada §e$" + bounty.getReward());
        }

        bountyRepository.claimBounty(bountyId, killer.getPlayerId());
        bountiesCache.remove(bountyId);
    }

    @Override
    public Collection<Bounty> getActiveBounties() {
        return bountiesCache.values().stream()
                .filter(b -> !b.isClaimed())
                .toList();
    }

    @Override
    public void incrementKillstreak(UUID playerUuid) {
        playerKillstreaks.put(playerUuid, playerKillstreaks.getOrDefault(playerUuid, 0) + 1);
    }

    @Override
    public Bounty getBounty(long bountyId) {
        return bountiesCache.get(bountyId);
    }

    @Override
    public Collection<Bounty> getBountiesOnPlayer(UUID targetUuid) {
        return bountiesCache.values().stream()
                .filter(b -> b.getTargetUuid().equals(targetUuid) && !b.isClaimed())
                .toList();
    }

    @Override
    public Collection<Bounty> getBountiesCreatedBy(UUID playerUuid) {
        PlayerProfile profile = playerCache.get(playerUuid);
        if (profile == null) return new ArrayList<>();
        return bountiesCache.values().stream()
                .filter(b -> b.getCreatedBy() == profile.getPlayerId())
                .toList();
    }

    @Override
    public int getPlayerKillstreak(UUID playerUuid) {
        return playerKillstreaks.getOrDefault(playerUuid, 0);
    }

    @Override
    public void resetKillstreak(UUID playerUuid) {
        playerKillstreaks.remove(playerUuid);
    }

    @Override
    public long getTotalBountyReward(UUID playerUuid) {
        return playerBountyEarnings.getOrDefault(playerUuid, 0L);
    }

    public void setBountyNotification(UUID targetUuid, long totalBounty) {
        Player target = Bukkit.getPlayer(targetUuid);
        if (target != null) {
            target.sendMessage("§c⚠ §6Recompensas totales sobre ti: §e$" + totalBounty);
        }
    }
}
