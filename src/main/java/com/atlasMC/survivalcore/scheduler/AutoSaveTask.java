package com.atlasMC.survivalcore.scheduler;

import com.atlasMC.survivalcore.cache.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AutoSaveTask implements Runnable {

    public static final int DEFAULT_INTERVAL_MINUTES = 5;

    private final PlayerCache cache;
    private final int intervalMinutes;

    public AutoSaveTask(PlayerCache cache) {
        this(cache, DEFAULT_INTERVAL_MINUTES);
    }

    public AutoSaveTask(PlayerCache cache, int intervalMinutes) {
        this.cache = cache;
        if (intervalMinutes <= 0) {
            throw new IllegalArgumentException("intervalMinutes must be > 0");
        }
        this.intervalMinutes = intervalMinutes;
    }

    public int intervalMinutes() {
        return intervalMinutes;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                var profile = cache.get(player.getUniqueId());
                if (profile != null) {
                    // Save will be handled by repository
                }
            } catch (RuntimeException ignored) {}
        }
    }
}
