package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.util.UUID;

/**
 * API publica de economia. Toda escritura pasa primero por {@link PlayerCache}
 * (para lectura inmediata) y se persiste de forma asincrona en la base de datos.
 */
public class EconomyAPI {

    private final PlayerCache playerCache;
    private final DatabaseManager databaseManager;

    public EconomyAPI(PlayerCache playerCache, DatabaseManager databaseManager) {
        this.playerCache = playerCache;
        this.databaseManager = databaseManager;
    }

    public long getBalance(UUID uuid) {
        PlayerProfile profile = playerCache.get(uuid);
        return profile != null ? profile.getBalance() : 0L;
    }

    public void addBalance(UUID uuid, long amount) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            return;
        }

        profile.addMoney(amount);
        databaseManager.executeAsync(
                "UPDATE players SET money = money + ? WHERE uuid = ?",
                null,
                amount, uuid.toString()
        );
        logTransaction(uuid, amount, "CREDIT", "addBalance");
    }

    public boolean removeBalance(UUID uuid, long amount) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null || profile.getBalance() < amount) {
            return false;
        }

        profile.removeMoney(amount);
        databaseManager.executeAsync(
                "UPDATE players SET money = money - ? WHERE uuid = ? AND money >= ?",
                null,
                amount, uuid.toString(), amount
        );
        logTransaction(uuid, -amount, "DEBIT", "removeBalance");
        return true;
    }

    public void logTransaction(UUID uuid, long amount, String type, String description) {
        String sql = """
                INSERT INTO transaction_logs (player_id, amount, type, description, timestamp)
                SELECT id, ?, ?, ?, NOW() FROM players WHERE uuid = ?
                """;
        databaseManager.executeAsync(sql, null, amount, type, description, uuid.toString());
    }
}
