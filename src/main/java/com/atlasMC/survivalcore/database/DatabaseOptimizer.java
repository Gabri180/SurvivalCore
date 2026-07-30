package com.atlasMC.survivalcore.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Bukkit;

public class DatabaseOptimizer {
    private final Connection connection;

    public DatabaseOptimizer(Connection connection) {
        this.connection = connection;
    }

    public void optimizeDatabase() {
        try {
            Bukkit.getLogger().info("§b[SurvivalCore] Optimizing database...");

            createIndexes();
            analyzeDatabase();

            Bukkit.getLogger().info("§a[SurvivalCore] Database optimization completed!");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[SurvivalCore] Database optimization failed: " + e.getMessage());
        }
    }

    private void createIndexes() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String[] indexes = {
                    "CREATE INDEX IF NOT EXISTS idx_player_uuid ON players(uuid)",
                    "CREATE INDEX IF NOT EXISTS idx_arena_active ON arenas(active)",
                    "CREATE INDEX IF NOT EXISTS idx_clan_owner ON clans(owner_uuid)",
                    "CREATE INDEX IF NOT EXISTS idx_auction_active ON auctions(active)",
                    "CREATE INDEX IF NOT EXISTS idx_bounty_active ON bounties(active)",
                    "CREATE INDEX IF NOT EXISTS idx_job_player ON jobs(player_uuid)",
                    "CREATE INDEX IF NOT EXISTS idx_skill_player ON skills(player_uuid)",
                    "CREATE INDEX IF NOT EXISTS idx_mission_player ON missions(player_uuid)",
                    "CREATE INDEX IF NOT EXISTS idx_transaction_player ON transactions(player_uuid)",
                    "CREATE INDEX IF NOT EXISTS idx_validation_logs_timestamp ON validation_logs(timestamp)"
            };

            for (String index : indexes) {
                try {
                    stmt.execute(index);
                    Bukkit.getLogger().info("§e[Database] Created: " + index.substring(0, Math.min(50, index.length())));
                } catch (SQLException e) {
                    if (!e.getMessage().toLowerCase().contains("already exists")) {
                        Bukkit.getLogger().warning("§c[Database] Failed to create index: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void analyzeDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String[] tables = {
                    "players", "arenas", "clans", "auctions", "bounties",
                    "jobs", "skills", "missions", "transactions"
            };

            for (String table : tables) {
                try {
                    stmt.execute("ANALYZE TABLE " + table);
                    Bukkit.getLogger().info("§e[Database] Analyzed: " + table);
                } catch (SQLException e) {
                    if (!e.getMessage().toLowerCase().contains("doesn't exist")) {
                        Bukkit.getLogger().warning("§c[Database] Failed to analyze " + table + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    public void optimizeTable(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("OPTIMIZE TABLE " + tableName);
            Bukkit.getLogger().info("§a[Database] Optimized table: " + tableName);
        } catch (SQLException e) {
            Bukkit.getLogger().warning("§c[Database] Failed to optimize " + tableName + ": " + e.getMessage());
        }
    }
}
