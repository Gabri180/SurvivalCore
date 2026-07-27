package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Gestiona leaderboards globales con caché actualizado cada 5 minutos.
 * v1.0.20+
 */
public class LeaderboardManager {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;
    private final PlayerCache playerCache;
    private final Map<String, List<LeaderboardEntry>> cache = new LinkedHashMap<>();

    public LeaderboardManager(JavaPlugin plugin, DatabaseManager databaseManager, PlayerCache playerCache) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerCache = playerCache;
        startCacheRefresh();
    }

    public static class LeaderboardEntry {
        public int rank;
        public UUID playerUuid;
        public String playerName;
        public long value;

        public LeaderboardEntry(int rank, UUID playerUuid, String playerName, long value) {
            this.rank = rank;
            this.playerUuid = playerUuid;
            this.playerName = playerName;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("§6#%d §e%s §7→ §b%,d", rank, playerName, value);
        }
    }

    /**
     * Obtiene top 10 jugadores por dinero.
     */
    public void getMoneyLeaderboard(Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "money";
        if (cache.containsKey(cacheKey)) {
            callback.accept(cache.get(cacheKey));
            return;
        }

        String sql = "SELECT uuid, name, money FROM players ORDER BY money DESC LIMIT 10";
        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                return new LeaderboardEntry(rank, UUID.fromString(rs.getString(1)), rs.getString(2), rs.getLong(3));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, entries -> callback.accept(entries));
    }

    /**
     * Obtiene top 10 clanes por poder.
     */
    public void getClanLeaderboard(Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "clan";
        if (cache.containsKey(cacheKey)) {
            callback.accept(cache.get(cacheKey));
            return;
        }

        String sql = "SELECT owner_id, name, power FROM clans ORDER BY power DESC LIMIT 10";
        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                PlayerProfile profile = playerCache.get(UUID.nameUUIDFromBytes(("player:" + rs.getLong(1)).getBytes()));
                return new LeaderboardEntry(rank, null, rs.getString(2), rs.getLong(3));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, entries -> callback.accept(entries));
    }

    /**
     * Obtiene top 10 jugadores por ELO en arena.
     */
    public void getArenaLeaderboard(Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "arena";
        if (cache.containsKey(cacheKey)) {
            callback.accept(cache.get(cacheKey));
            return;
        }

        String sql = """
                SELECT p.uuid, p.name, a.elo
                FROM arena_stats a
                JOIN players p ON p.id = a.player_id
                ORDER BY a.elo DESC
                LIMIT 10
                """;

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                return new LeaderboardEntry(rank, UUID.fromString(rs.getString(1)), rs.getString(2), rs.getLong(3));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, entries -> callback.accept(entries));
    }

    /**
     * Obtiene posición de un jugador en ranking de dinero.
     */
    public void getPlayerMoneyRank(UUID uuid, Consumer<Integer> callback) {
        String sql = "SELECT COUNT(*) + 1 FROM players WHERE money > (SELECT money FROM players WHERE uuid = ?)";
        databaseManager.queryAsync(sql, rs -> {
            try {
                return rs.getInt(1);
            } catch (Exception e) {
                return 0;
            }
        }, result -> callback.accept(result.isEmpty() ? 0 : result.get(0)), uuid.toString());
    }

    private void startCacheRefresh() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            cache.clear();
            plugin.getLogger().finer("Leaderboards caché limpiado");
        }, 20L * 60 * 5, 20L * 60 * 5); // Cada 5 minutos
    }
}
