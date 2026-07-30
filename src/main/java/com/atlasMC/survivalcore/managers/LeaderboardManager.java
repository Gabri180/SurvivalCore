package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.enums.SkillType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Gestiona leaderboards globales con caché inteligente actualizado cada 5 minutos.
 * Soporta múltiples tipos de rankings con paginación.
 * v1.0.20+
 */
public class LeaderboardManager {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;
    private final PlayerCache playerCache;
    private final Map<String, CachedLeaderboard> cache = new HashMap<>();
    private final long CACHE_EXPIRY_MS = 5 * 60 * 1000; // 5 minutos

    public LeaderboardManager(JavaPlugin plugin, DatabaseManager databaseManager, PlayerCache playerCache) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerCache = playerCache;
        startCacheRefresh();
    }

    public static class LeaderboardEntry {
        public final int rank;
        public final UUID playerUuid;
        public final String playerName;
        public final long value;

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

    private static class CachedLeaderboard {
        final List<LeaderboardEntry> entries;
        final long createdAt;

        CachedLeaderboard(List<LeaderboardEntry> entries) {
            this.entries = entries;
            this.createdAt = System.currentTimeMillis();
        }

        boolean isExpired(long expiryMs) {
            return System.currentTimeMillis() - createdAt > expiryMs;
        }
    }

    /**
     * Obtiene top N jugadores por dinero total.
     */
    public void getMoneyLeaderboard(int limit, Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "money_" + limit;
        if (isCacheValid(cacheKey)) {
            callback.accept(cache.get(cacheKey).entries);
            return;
        }

        String sql = String.format("""
                SELECT id, uuid, name, money
                FROM players
                WHERE money > 0
                ORDER BY money DESC
                LIMIT %d
                """, limit);

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                long value = rs.getLong("money");
                return new LeaderboardEntry(rank, uuid, name, value);
            } catch (Exception e) {
                throw new RuntimeException("Error mapping money leaderboard", e);
            }
        }, entries -> {
            cacheLeaderboard(cacheKey, entries);
            callback.accept(entries);
        });
    }

    /**
     * Obtiene top N clanes por poder total.
     */
    public void getClanLeaderboard(int limit, Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "clan_" + limit;
        if (isCacheValid(cacheKey)) {
            callback.accept(cache.get(cacheKey).entries);
            return;
        }

        String sql = String.format("""
                SELECT c.id, c.name, c.power, p.uuid, p.name AS leader_name
                FROM clans c
                JOIN players p ON c.owner_id = p.id
                WHERE c.power > 0
                ORDER BY c.power DESC
                LIMIT %d
                """, limit);

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                String clanName = rs.getString("name");
                long power = rs.getLong("power");
                return new LeaderboardEntry(rank, null, clanName, power);
            } catch (Exception e) {
                throw new RuntimeException("Error mapping clan leaderboard", e);
            }
        }, entries -> {
            cacheLeaderboard(cacheKey, entries);
            callback.accept(entries);
        });
    }

    /**
     * Obtiene top N jugadores por ELO en arena.
     */
    public void getArenaLeaderboard(int limit, Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "arena_" + limit;
        if (isCacheValid(cacheKey)) {
            callback.accept(cache.get(cacheKey).entries);
            return;
        }

        String sql = String.format("""
                SELECT p.uuid, p.name, a.elo, a.wins
                FROM arena_stats a
                JOIN players p ON p.id = a.player_id
                WHERE a.elo > 0
                ORDER BY a.elo DESC
                LIMIT %d
                """, limit);

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                long elo = rs.getLong("elo");
                return new LeaderboardEntry(rank, uuid, name, elo);
            } catch (Exception e) {
                throw new RuntimeException("Error mapping arena leaderboard", e);
            }
        }, entries -> {
            cacheLeaderboard(cacheKey, entries);
            callback.accept(entries);
        });
    }

    /**
     * Obtiene top N jugadores por skill específico.
     */
    public void getSkillLeaderboard(SkillType skillType, int limit, Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "skill_" + skillType.name() + "_" + limit;
        if (isCacheValid(cacheKey)) {
            callback.accept(cache.get(cacheKey).entries);
            return;
        }

        String sql = String.format("""
                SELECT p.uuid, p.name, ps.level, ps.exp
                FROM player_skills ps
                JOIN players p ON p.id = ps.player_id
                WHERE ps.skill_type = ? AND ps.level > 1
                ORDER BY ps.level DESC, ps.exp DESC
                LIMIT %d
                """, limit);

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                long level = rs.getLong("level");
                return new LeaderboardEntry(rank, uuid, name, level);
            } catch (Exception e) {
                throw new RuntimeException("Error mapping skill leaderboard", e);
            }
        }, entries -> {
            cacheLeaderboard(cacheKey, entries);
            callback.accept(entries);
        }, skillType.name());
    }

    /**
     * Obtiene top N jugadores por job específico.
     */
    public void getJobLeaderboard(JobType jobType, int limit, Consumer<List<LeaderboardEntry>> callback) {
        String cacheKey = "job_" + jobType.name() + "_" + limit;
        if (isCacheValid(cacheKey)) {
            callback.accept(cache.get(cacheKey).entries);
            return;
        }

        String sql = String.format("""
                SELECT p.uuid, p.name, pj.level, pj.exp
                FROM player_jobs pj
                JOIN players p ON p.id = pj.player_id
                WHERE pj.job_type = ? AND pj.level > 1
                ORDER BY pj.level DESC, pj.exp DESC
                LIMIT %d
                """, limit);

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = rs.getRow();
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                long level = rs.getLong("level");
                return new LeaderboardEntry(rank, uuid, name, level);
            } catch (Exception e) {
                throw new RuntimeException("Error mapping job leaderboard", e);
            }
        }, entries -> {
            cacheLeaderboard(cacheKey, entries);
            callback.accept(entries);
        }, jobType.name());
    }

    /**
     * Obtiene posición de un jugador en un ranking específico.
     */
    public void getPlayerRank(UUID uuid, String leaderboardType, Consumer<Integer> callback) {
        String sql = switch (leaderboardType.toLowerCase()) {
            case "money" -> """
                    SELECT COUNT(*) + 1 FROM players
                    WHERE money > (SELECT money FROM players WHERE uuid = ?)
                    """;
            case "arena" -> """
                    SELECT COUNT(*) + 1 FROM arena_stats a
                    JOIN players p ON p.id = a.player_id
                    WHERE a.elo > (SELECT a2.elo FROM arena_stats a2
                    JOIN players p2 ON p2.id = a2.player_id WHERE p2.uuid = ?)
                    """;
            default -> "SELECT 0";
        };

        databaseManager.queryAsync(sql, rs -> {
            try {
                return rs.getInt(1);
            } catch (Exception e) {
                return 0;
            }
        }, result -> callback.accept(result.isEmpty() ? 0 : result.get(0)), uuid.toString());
    }

    /**
     * Obtiene página específica de un leaderboard (para paginación).
     */
    public void getLeaderboardPage(String type, int page, int pageSize, Consumer<List<LeaderboardEntry>> callback) {
        int offset = (page - 1) * pageSize;
        String cacheKey = type + "_page_" + page + "_size_" + pageSize;

        if (isCacheValid(cacheKey)) {
            callback.accept(cache.get(cacheKey).entries);
            return;
        }

        String sql = String.format("""
                SELECT id, uuid, name, money FROM players
                WHERE money > 0
                ORDER BY money DESC
                LIMIT %d OFFSET %d
                """, pageSize, offset);

        databaseManager.queryAsync(sql, rs -> {
            try {
                int rank = offset + rs.getRow();
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                long value = rs.getLong("money");
                return new LeaderboardEntry(rank, uuid, name, value);
            } catch (Exception e) {
                throw new RuntimeException("Error mapping paginated leaderboard", e);
            }
        }, entries -> {
            cacheLeaderboard(cacheKey, entries);
            callback.accept(entries);
        });
    }

    /**
     * Refresca todos los leaderboards (fuerza actualización inmediata).
     */
    public void refreshAllLeaderboards() {
        cache.clear();
        plugin.getLogger().info("✓ Leaderboards caché limpiado");
    }

    private void cacheLeaderboard(String key, List<LeaderboardEntry> entries) {
        cache.put(key, new CachedLeaderboard(entries));
    }

    private boolean isCacheValid(String key) {
        if (!cache.containsKey(key)) {
            return false;
        }
        return !cache.get(key).isExpired(CACHE_EXPIRY_MS);
    }

    private void startCacheRefresh() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            // Limpiar entradas expiradas
            List<String> toRemove = new ArrayList<>();
            for (Map.Entry<String, CachedLeaderboard> entry : cache.entrySet()) {
                if (entry.getValue().isExpired(CACHE_EXPIRY_MS)) {
                    toRemove.add(entry.getKey());
                }
            }
            toRemove.forEach(cache::remove);

            if (!toRemove.isEmpty()) {
                plugin.getLogger().finer("Leaderboard cache cleaned: " + toRemove.size() + " entries");
            }
        }, 20L * 60, 20L * 60); // Chequea cada minuto
    }
}
