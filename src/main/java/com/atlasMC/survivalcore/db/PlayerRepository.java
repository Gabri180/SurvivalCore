package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.models.PlayerProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de la tabla {@code players}. Todas las operaciones son
 * asincronas para no bloquear el hilo principal.
 */
public class PlayerRepository {

    private static final Logger LOGGER = Logger.getLogger(PlayerRepository.class.getName());

    private final DatabaseManager databaseManager;

    public PlayerRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void savePlayer(PlayerProfile profile) {
        String sql = """
                INSERT INTO players (uuid, name, money, premium_money, `rank`, prestige, last_login)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    money = VALUES(money),
                    premium_money = VALUES(premium_money),
                    `rank` = VALUES(`rank`),
                    prestige = VALUES(prestige),
                    last_login = VALUES(last_login)
                """;

        databaseManager.executeAsync(sql, affected -> {
            if (affected == null || affected == 0) {
                LOGGER.fine(() -> "No se afectaron filas guardando el perfil de " + profile.getUuid());
            }
        },
                profile.getUuid().toString(),
                profile.getName(),
                profile.getMoney(),
                profile.getPremiumMoney(),
                profile.getRank(),
                profile.getPrestige(),
                new Timestamp(profile.getLastLogin())
        );
    }

    /**
     * Inserta un jugador nuevo y devuelve su id autogenerado (players.id),
     * necesario antes de poder registrar filas en player_jobs/player_skills/etc.
     */
    public void insertNewPlayer(PlayerProfile profile, Consumer<Long> onId) {
        String sql = """
                INSERT INTO players (uuid, name, money, premium_money, `rank`, prestige, last_login)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        databaseManager.executeInsertAsync(sql, onId, error ->
                        LOGGER.severe("No se pudo crear el jugador " + profile.getUuid() + ": " + error.getMessage()),
                profile.getUuid().toString(),
                profile.getName(),
                profile.getMoney(),
                profile.getPremiumMoney(),
                profile.getRank(),
                profile.getPrestige(),
                new Timestamp(profile.getLastLogin())
        );
    }

    public void loadPlayer(UUID uuid, Consumer<PlayerProfile> callback) {
        String sql = "SELECT * FROM players WHERE uuid = ?";

        databaseManager.queryAsync(sql, this::mapRow, results -> {
            PlayerProfile profile = results.isEmpty() ? null : results.get(0);
            if (callback != null) {
                callback.accept(profile);
            }
        }, uuid.toString());
    }

    public void getAllPlayers(Consumer<List<PlayerProfile>> callback) {
        String sql = "SELECT * FROM players";
        databaseManager.queryAsync(sql, this::mapRow, callback);
    }

    private PlayerProfile mapRow(ResultSet rs) {
        try {
            Timestamp lastLogin = rs.getTimestamp("last_login");
            return PlayerProfile.builder()
                    .playerId(rs.getLong("id"))
                    .uuid(UUID.fromString(rs.getString("uuid")))
                    .name(rs.getString("name"))
                    .money(rs.getLong("money"))
                    .premiumMoney(rs.getLong("premium_money"))
                    .rank(rs.getString("rank"))
                    .prestige(rs.getInt("prestige"))
                    .lastLogin(lastLogin != null ? lastLogin.getTime() : 0L)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de players", e);
            throw new IllegalStateException(e);
        }
    }
}
