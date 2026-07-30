package com.atlasMC.survivalcore.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code clan_wars}: guerras entre clanes, su progreso y resultado.
 */
public class ClanWarRepository {

    public record ClanWarRow(long id, long attackingClanId, long defendingClanId,
                              long attackerScore, long defenderScore, boolean active) {
    }

    private static final Logger LOGGER = Logger.getLogger(ClanWarRepository.class.getName());

    private final DatabaseManager databaseManager;

    public ClanWarRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void startWar(long attackingClanId, long defendingClanId, Consumer<Long> onId) {
        String sql = """
                INSERT INTO clan_wars (attacking_clan_id, defending_clan_id, started_at, active)
                VALUES (?, ?, NOW(), TRUE)
                """;
        databaseManager.executeInsertAsync(sql, onId, error ->
                LOGGER.severe("Error creando guerra de clanes: " + error.getMessage()),
                attackingClanId, defendingClanId);
    }

    public void loadActiveWars(Consumer<List<ClanWarRow>> callback) {
        databaseManager.queryAsync("SELECT * FROM clan_wars WHERE active = TRUE", this::mapRow, callback);
    }

    public void addScore(long warId, boolean attackerSide, long amount) {
        String column = attackerSide ? "attacker_score" : "defender_score";
        databaseManager.executeAsync(
                "UPDATE clan_wars SET " + column + " = " + column + " + ? WHERE id = ?",
                null, amount, warId);
    }

    public void endWar(long warId, Long winnerClanId) {
        databaseManager.executeAsync(
                "UPDATE clan_wars SET active = FALSE, ended_at = NOW(), winner_clan_id = ? WHERE id = ?",
                null, winnerClanId, warId);
    }

    private ClanWarRow mapRow(ResultSet rs) {
        try {
            return new ClanWarRow(
                    rs.getLong("id"),
                    rs.getLong("attacking_clan_id"),
                    rs.getLong("defending_clan_id"),
                    rs.getLong("attacker_score"),
                    rs.getLong("defender_score"),
                    rs.getBoolean("active")
            );
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de clan_wars", e);
            throw new IllegalStateException(e);
        }
    }
}
