package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.BossType;
import com.atlasMC.survivalcore.models.WeeklyBoss;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code weekly_bosses}.
 */
public class BossRepository {

    private static final Logger LOGGER = Logger.getLogger(BossRepository.class.getName());

    private final DatabaseManager databaseManager;

    public BossRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveBoss(WeeklyBoss boss, Consumer<Long> onId) {
        String sql = """
                INSERT INTO weekly_bosses (boss_type, spawn_time, spawn_location, health, defeated_by, rewards)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        databaseManager.executeInsertAsync(sql, onId, error ->
                        LOGGER.severe("Error guardando weekly_boss: " + error.getMessage()),
                boss.getBossType().name(),
                boss.getSpawnTime() != null ? Timestamp.from(boss.getSpawnTime()) : null,
                boss.getSpawnLocation(),
                boss.getHealth(),
                boss.getDefeatedBy(),
                String.join(",", boss.getRewards())
        );
    }

    public void markDefeated(long bossId, long killerId) {
        databaseManager.executeAsync(
                "UPDATE weekly_bosses SET defeated_by = ? WHERE id = ?",
                null, killerId, bossId);
    }

    public void loadRecent(Consumer<List<WeeklyBoss>> callback) {
        databaseManager.queryAsync(
                "SELECT * FROM weekly_bosses ORDER BY id DESC LIMIT 20", this::mapRow, callback);
    }

    private WeeklyBoss mapRow(ResultSet rs) {
        try {
            Timestamp spawnTime = rs.getTimestamp("spawn_time");
            long defeatedBy = rs.getLong("defeated_by");
            boolean defeatedByNull = rs.wasNull();
            String rewards = rs.getString("rewards");
            return WeeklyBoss.builder()
                    .id(rs.getLong("id"))
                    .bossType(BossType.valueOf(rs.getString("boss_type")))
                    .spawnTime(spawnTime != null ? spawnTime.toInstant() : null)
                    .spawnLocation(rs.getString("spawn_location"))
                    .health(rs.getInt("health"))
                    .defeatedBy(defeatedByNull ? null : defeatedBy)
                    .rewards(rewards != null && !rewards.isBlank()
                            ? List.of(rewards.split(","))
                            : List.of())
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de weekly_bosses", e);
            throw new IllegalStateException(e);
        }
    }
}
