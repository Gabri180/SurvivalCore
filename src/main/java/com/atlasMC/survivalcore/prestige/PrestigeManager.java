package com.atlasMC.survivalcore.prestige;

import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.util.UUID;

/**
 * Gestiona el reseteo de progresion y avance de prestigio de un jugador.
 */
public class PrestigeManager {

    private final PlayerCache playerCache;
    private final DatabaseManager databaseManager;

    public PrestigeManager(PlayerCache playerCache, DatabaseManager databaseManager) {
        this.playerCache = playerCache;
        this.databaseManager = databaseManager;
    }

    public int getPrestigeLevel(UUID uuid) {
        PlayerProfile profile = playerCache.get(uuid);
        return profile != null ? profile.getPrestige() : 0;
    }

    public void addPrestigeLevel(UUID uuid) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            return;
        }

        profile.setPrestige(profile.getPrestige() + 1);
        databaseManager.executeAsync(
                "UPDATE players SET prestige = prestige + 1 WHERE uuid = ?",
                null,
                uuid.toString()
        );
    }

    /**
     * Resetea la progresion del jugador (rango, skills, jobs) y suma un
     * nivel de prestigio. La limpieza de skills/jobs la ejecutan sus
     * respectivos managers via EventAPI al escuchar este cambio.
     */
    public void prestigePlayer(UUID uuid) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            return;
        }

        profile.setRank("DEFAULT");
        profile.getSkills().clear();
        profile.getActiveMissions().clear();
        addPrestigeLevel(uuid);

        databaseManager.executeAsync(
                "UPDATE players SET `rank` = 'DEFAULT' WHERE uuid = ?",
                null,
                uuid.toString()
        );
        databaseManager.executeAsync(
                "INSERT INTO player_prestige (player_id, prestige_level, reset_count, last_prestige_date) "
                        + "SELECT id, ?, 1, NOW() FROM players WHERE uuid = ? "
                        + "ON DUPLICATE KEY UPDATE prestige_level = VALUES(prestige_level), "
                        + "reset_count = player_prestige.reset_count + 1, last_prestige_date = NOW()",
                null,
                profile.getPrestige(), uuid.toString()
        );
    }
}
