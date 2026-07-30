package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.leaderboard.LeaderboardEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILeaderboardManager {
    enum LeaderboardType {
        MONEY("Dinero"),
        ARENA_WINS("Victorias en Arena"),
        CLAN_POWER("Poder del Clan"),
        SKILL_LEVEL("Nivel de Skills"),
        JOB_LEVEL("Nivel de Trabajo");

        private final String displayName;

        LeaderboardType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    void updateLeaderboard(LeaderboardType type);
    void updateAllLeaderboards();

    List<LeaderboardEntry> getLeaderboard(LeaderboardType type, int page, int perPage);
    List<LeaderboardEntry> getTopPlayers(LeaderboardType type, int limit);

    Optional<LeaderboardEntry> getPlayerRank(UUID uuid, LeaderboardType type);
    int getPlayerPosition(UUID uuid, LeaderboardType type);

    void clearLeaderboard(LeaderboardType type);
    void saveLeaderboards();
    void loadLeaderboards();
}
