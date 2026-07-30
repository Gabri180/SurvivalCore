package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Placeholder de Hauch: estructura lista, logica de exp pendiente de
 * conectar a {@link IJobManager} manana.
 */
public class JobHarvestListener implements Listener {

    private final IJobManager jobManager;
    private final PlayerCache playerCache;

    public JobHarvestListener(IJobManager jobManager, PlayerCache playerCache) {
        this.jobManager = jobManager;
        this.playerCache = playerCache;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        if (jobManager == null || !(event.getBlock().getBlockData() instanceof Ageable)) return;
        Ageable ageable = (Ageable) event.getBlock().getBlockData();
        if (ageable.getAge() < ageable.getMaximumAge()) return;

        Player player = event.getPlayer();
        PlayerProfile profile = playerCache.get(player.getUniqueId());

        if (profile != null && profile.getCurrentJob() != null && profile.getCurrentJob().equalsIgnoreCase("FARMER")) {
            long newExp = profile.getJobExp() + 20;
            int newLevel = profile.getJobLevel();

            if (newExp >= 1000) {
                newLevel++;
                newExp = 0;
                player.sendMessage("§6¡Subiste a nivel " + newLevel + " en FARMER!");
            } else {
                player.sendMessage("§a+20 exp FARMER (§e" + newExp + "§a/1000)");
            }

            profile.setJobExp(newExp);
            profile.setJobLevel(newLevel);
            // playerCache update
            jobManager.addExp(player.getUniqueId(), JobType.FARMER, 20L);
        }
    }
}
