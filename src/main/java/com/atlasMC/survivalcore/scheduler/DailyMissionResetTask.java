package com.atlasMC.survivalcore.scheduler;

import com.atlasMC.survivalcore.db.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Resetea misiones diarias cada día a las 00:00.
 * v1.0.21+
 */
public class DailyMissionResetTask {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public DailyMissionResetTask(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startDailyReset();
    }

    private void startDailyReset() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            long delayMs = ChronoUnit.MILLIS.between(now, midnight);
            long delayTicks = Math.max(1, delayMs / 50);

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                    this::resetDailyMissions,
                    delayTicks
            );
        }, 0L, 20L * 60 * 60 * 24); // Chequea cada 24 horas
    }

    private void resetDailyMissions() {
        String sql = """
                UPDATE player_mission_progress
                SET progress = 0, completed = FALSE, completed_at = NULL
                WHERE mission_id IN (
                    SELECT id FROM missions WHERE frequency = 'DAILY'
                )
                """;

        databaseManager.executeAsync(sql, affected -> {
            if (affected != null && affected > 0) {
                plugin.getLogger().info("✓ Misiones diarias reseteadas: " + affected + " registros");
            }
        });
    }
}
