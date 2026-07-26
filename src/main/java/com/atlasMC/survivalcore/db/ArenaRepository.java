package com.atlasMC.survivalcore.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code arena_stats} (ranking ELO + wins/losses) para el
 * modulo de arenas PvP.
 */
public class ArenaRepository {

    public record ArenaStats(long playerId, int wins, int losses, int elo) {
    }

    private static final Logger LOGGER = Logger.getLogger(ArenaRepository.class.getName());
    private static final int DEFAULT_ELO = 1000;

    private final DatabaseManager databaseManager;

    public ArenaRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void loadStats(long playerId, Consumer<ArenaStats> callback) {
        String sql = "SELECT * FROM arena_stats WHERE player_id = ?";
        databaseManager.queryAsync(sql, this::mapRow, results ->
                callback.accept(results.isEmpty() ? new ArenaStats(playerId, 0, 0, DEFAULT_ELO) : results.get(0)),
                playerId);
    }

    public void recordResult(long winnerId, long loserId, int winnerNewElo, int loserNewElo) {
        upsert(winnerId, true, winnerNewElo);
        upsert(loserId, false, loserNewElo);
    }

    private void upsert(long playerId, boolean won, int newElo) {
        String sql = """
                INSERT INTO arena_stats (player_id, wins, losses, elo)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    wins = wins + VALUES(wins),
                    losses = losses + VALUES(losses),
                    elo = VALUES(elo)
                """;
        databaseManager.executeAsync(sql, null, playerId, won ? 1 : 0, won ? 0 : 1, newElo);
    }

    private ArenaStats mapRow(ResultSet rs) {
        try {
            return new ArenaStats(
                    rs.getLong("player_id"),
                    rs.getInt("wins"),
                    rs.getInt("losses"),
                    rs.getInt("elo")
            );
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de arena_stats", e);
            throw new IllegalStateException(e);
        }
    }
}
