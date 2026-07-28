package com.atlasMC.survivalcore.scheduler;

import com.atlasMC.survivalcore.api.IAuctionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AuctionFinalizerScheduler extends BukkitRunnable {

    private final IAuctionManager auctionManager;
    private final JavaPlugin plugin;

    public AuctionFinalizerScheduler(IAuctionManager auctionManager, JavaPlugin plugin) {
        this.auctionManager = auctionManager;
        this.plugin = plugin;
    }

    public void start() {
        runTaskTimer(plugin, 0, 20 * 60);
    }

    @Override
    public void run() {
        auctionManager.getActiveAuctions().stream()
                .filter(a -> a.isExpired())
                .forEach(a -> auctionManager.completeAuction(a.getId()));
    }
}
