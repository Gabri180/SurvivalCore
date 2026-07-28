package com.atlasMC.survivalcore.managers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CacheOptimizer {

    private final JavaPlugin plugin;
    private long cacheHits = 0;
    private long cacheMisses = 0;
    private long totalMemory = 0;

    public CacheOptimizer(JavaPlugin plugin) {
        this.plugin = plugin;
        startMemoryMonitor();
    }

    private void startMemoryMonitor() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                long maxMemory = runtime.maxMemory() / 1024 / 1024;

                if (usedMemory > (maxMemory * 0.9)) {
                    plugin.getLogger().warning("⚠ Memoria casi llena: " + usedMemory + "MB / " + maxMemory + "MB");
                    System.gc();
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 60); // Monitorear cada minuto
    }

    public void recordCacheHit() {
        cacheHits++;
    }

    public void recordCacheMiss() {
        cacheMisses++;
    }

    public double getCacheHitRate() {
        long total = cacheHits + cacheMisses;
        if (total == 0) return 0;
        return (double) cacheHits / total * 100;
    }

    public String getCacheStats() {
        return String.format("§6Cache: §e%.2f%% hit rate §7(hits: %d, misses: %d)",
                getCacheHitRate(), cacheHits, cacheMisses);
    }

    public void resetStats() {
        cacheHits = 0;
        cacheMisses = 0;
    }

    public long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
    }

    public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() / 1024 / 1024;
    }

    public String getMemoryStats() {
        return String.format("§6Memory: §e%dMB§7/§e%dMB",
                getUsedMemory(), getMaxMemory());
    }
}
