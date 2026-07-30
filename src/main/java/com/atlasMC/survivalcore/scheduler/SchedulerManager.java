package com.atlasMC.survivalcore.scheduler;

import com.atlasMC.survivalcore.api.IBossManager;
import com.atlasMC.survivalcore.api.IAuctionManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.seasons.SeasonManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SchedulerManager {

    private final JavaPlugin plugin;

    public SchedulerManager(JavaPlugin plugin, PlayerCache playerCache, SeasonManager seasonManager,
                          IBossManager bossManager, IAuctionManager auctionManager) {
        this.plugin = plugin;
        startSeasonRotation(seasonManager);
        startBossSpawner(bossManager);
        startAuctionExpiry(auctionManager);
    }

    private void startSeasonRotation(SeasonManager seasonManager) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (seasonManager.currentSeason() != null && seasonManager.currentSeason().isActive()) {
                return;
            }
            seasonManager.resetSeason();
        }, 0L, 20L * 60 * 60 * 24);
    }

    private void startBossSpawner(IBossManager bossManager) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            // TODO: Lógica de spawn de jefes semanales
        }, 0L, 20L * 60 * 20);
    }

    private void startAuctionExpiry(IAuctionManager auctionManager) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            // TODO: Lógica de expiración de subastas
        }, 0L, 20L * 60);
    }
}
