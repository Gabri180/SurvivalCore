package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.MissionFrequency;
import com.atlasMC.survivalcore.enums.MissionType;
import com.atlasMC.survivalcore.models.Mission;
import com.atlasMC.survivalcore.models.MissionProgress;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code missions} y {@code player_mission_progress} para el
 * modulo de Misiones (Hauch). Listo para usar: solo falta que IMissionManager
 * llame a estos metodos.
 */
public class MissionRepository {

    private static final Logger LOGGER = Logger.getLogger(MissionRepository.class.getName());

    private final DatabaseManager databaseManager;

    public MissionRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void loadMissions(Consumer<List<Mission>> callback) {
        String sql = "SELECT * FROM missions";
        databaseManager.queryAsync(sql, this::mapMission, callback);
    }

    public void loadPlayerProgress(long playerId, Consumer<List<MissionProgress>> callback) {
        String sql = "SELECT * FROM player_mission_progress WHERE player_id = ?";
        databaseManager.queryAsync(sql, this::mapProgress, callback, playerId);
    }

    public void saveMissionProgress(MissionProgress progress) {
        String sql = """
                INSERT INTO player_mission_progress (player_id, mission_id, progress, completed, completed_at)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    progress = VALUES(progress),
                    completed = VALUES(completed),
                    completed_at = VALUES(completed_at)
                """;
        databaseManager.executeAsync(sql, null,
                progress.getPlayerId(),
                progress.getMissionId(),
                progress.getProgress(),
                progress.isCompleted(),
                progress.getCompletedAt() != null ? Timestamp.from(progress.getCompletedAt()) : null
        );
    }

    public void updateProgress(long playerId, long missionId, int progressDelta) {
        String sql = "UPDATE player_mission_progress SET progress = progress + ? WHERE player_id = ? AND mission_id = ?";
        databaseManager.executeAsync(sql, null, progressDelta, playerId, missionId);
    }

    private Mission mapMission(ResultSet rs) {
        try {
            return Mission.builder()
                    .id(rs.getLong("id"))
                    .missionType(MissionType.valueOf(rs.getString("mission_type")))
                    .frequency(MissionFrequency.valueOf(rs.getString("frequency")))
                    .target(rs.getString("target"))
                    .rewardMoney(rs.getLong("reward_money"))
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de missions", e);
            throw new IllegalStateException(e);
        }
    }

    private MissionProgress mapProgress(ResultSet rs) {
        try {
            Timestamp completedAt = rs.getTimestamp("completed_at");
            return MissionProgress.builder()
                    .playerId(rs.getLong("player_id"))
                    .missionId(rs.getLong("mission_id"))
                    .progress(rs.getInt("progress"))
                    .completed(rs.getBoolean("completed"))
                    .completedAt(completedAt != null ? completedAt.toInstant() : null)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de player_mission_progress", e);
            throw new IllegalStateException(e);
        }
    }
}
