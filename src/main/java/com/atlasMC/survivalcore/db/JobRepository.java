package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.models.PlayerJob;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobRepository {

    private static final Logger LOGGER = Logger.getLogger(JobRepository.class.getName());

    private final DatabaseManager databaseManager;

    public JobRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveJob(PlayerJob job) {
        String sql = """
                INSERT INTO player_jobs (player_id, job_type, level, exp, money_earned, last_paid)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    level = VALUES(level),
                    exp = VALUES(exp),
                    money_earned = VALUES(money_earned),
                    last_paid = VALUES(last_paid)
                """;
        databaseManager.executeAsync(sql, null,
                job.getPlayerId(),
                job.getJobType().name(),
                job.getLevel(),
                job.getExp(),
                job.getMoneyEarned(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    public void loadJob(long playerId, JobType jobType, java.util.function.Consumer<PlayerJob> callback) {
        String sql = "SELECT * FROM player_jobs WHERE player_id = ? AND job_type = ?";
        databaseManager.queryAsync(sql, this::mapRow, results -> {
            PlayerJob job = results.isEmpty() ? null : results.get(0);
            if (callback != null) {
                callback.accept(job);
            }
        }, playerId, jobType.name());
    }

    public void loadJobs(long playerId, java.util.function.Consumer<List<PlayerJob>> callback) {
        String sql = "SELECT * FROM player_jobs WHERE player_id = ?";
        databaseManager.queryAsync(sql, this::mapRow, callback, playerId);
    }

    public void updateExp(long playerId, JobType jobType, long expDelta) {
        String sql = "UPDATE player_jobs SET exp = exp + ? WHERE player_id = ? AND job_type = ?";
        databaseManager.executeAsync(sql, null, expDelta, playerId, jobType.name());
    }

    private PlayerJob mapRow(ResultSet rs) {
        try {
            return PlayerJob.builder()
                    .playerId(rs.getLong("player_id"))
                    .jobType(JobType.valueOf(rs.getString("job_type")))
                    .level(rs.getInt("level"))
                    .exp(rs.getLong("exp"))
                    .moneyEarned(rs.getLong("money_earned"))
                    .lastPaid(rs.getTimestamp("last_paid") != null ? rs.getTimestamp("last_paid").getTime() : 0)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de player_jobs", e);
            throw new IllegalStateException(e);
        }
    }
}
