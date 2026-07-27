package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IBountyManager;
import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.enums.JobType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class JobKillListener implements Listener {

    private final IJobManager jobManager;
    private final IBountyManager bountyManager;

    public JobKillListener(IJobManager jobManager, IBountyManager bountyManager) {
        this.jobManager = jobManager;
        this.bountyManager = bountyManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (jobManager == null) return;

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        if (event.getEntity() instanceof Player victim) {
            handlePlayerKill(killer, victim);
        } else {
            handleMobKill(killer, event.getEntity());
        }
    }

    private void handlePlayerKill(Player killer, Player victim) {
        long exp = 100L;
        jobManager.addExp(killer.getUniqueId(), JobType.HUNTER, exp);

        if (bountyManager != null) {
            bountyManager.incrementKillstreak(killer.getUniqueId());

            var bounties = bountyManager.getBountiesOnPlayer(victim.getUniqueId());
            for (var bounty : bounties) {
                bountyManager.claimBounty(bounty.getId(), killer.getUniqueId());
            }
        }

        killer.sendMessage("§6+§e" + exp + " XP §eMuertes: §b1");
    }

    private void handleMobKill(Player killer, LivingEntity mob) {
        long exp = getExpForMob(mob);
        if (exp > 0) {
            jobManager.addExp(killer.getUniqueId(), JobType.HUNTER, exp);
        }
    }

    private long getExpForMob(LivingEntity mob) {
        return switch (mob.getType()) {
            case WITHER -> 500L;
            case ENDER_DRAGON -> 250L;
            case WARDEN -> 150L;
            case CREEPER, SKELETON, ZOMBIE, SPIDER -> 25L;
            case ENDERMAN, BLAZE -> 50L;
            case GHAST -> 40L;
            case PIGLIN_BRUTE -> 60L;
            case VINDICATOR, EVOKER -> 70L;
            case GUARDIAN, ELDER_GUARDIAN -> 80L;
            default -> 10L;
        };
    }
}
