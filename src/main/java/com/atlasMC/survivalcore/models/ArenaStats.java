package com.atlasMC.survivalcore.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaStats {
    private UUID playerId;
    private String arenaId;
    private int wins;
    private int losses;
    private int rank;
    private long totalEarnings;
    private long lastMatchTime;

    public ArenaStats() {}

    public ArenaStats(UUID playerId, String arenaId) {
        this.playerId = playerId;
        this.arenaId = arenaId;
        this.wins = 0;
        this.losses = 0;
        this.rank = 0;
        this.totalEarnings = 0;
        this.lastMatchTime = System.currentTimeMillis();
    }

    public UUID getPlayerId() { return playerId; }
    public void setPlayerId(UUID playerId) { this.playerId = playerId; }

    public String getArenaId() { return arenaId; }
    public void setArenaId(String arenaId) { this.arenaId = arenaId; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }
    public void addWin() { this.wins++; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }
    public void addLoss() { this.losses++; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public long getTotalEarnings() { return totalEarnings; }
    public void addEarnings(long amount) { this.totalEarnings += amount; }

    public long getLastMatchTime() { return lastMatchTime; }
    public void setLastMatchTime(long lastMatchTime) { this.lastMatchTime = lastMatchTime; }

    public int getWinRate() {
        int total = wins + losses;
        if (total == 0) return 0;
        return (int) ((wins * 100.0) / total);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID playerId;
        private String arenaId;
        private int wins;
        private int losses;
        private int rank;
        private long totalEarnings;
        private long lastMatchTime;

        public Builder playerId(UUID playerId) { this.playerId = playerId; return this; }
        public Builder arenaId(String arenaId) { this.arenaId = arenaId; return this; }
        public Builder wins(int wins) { this.wins = wins; return this; }
        public Builder losses(int losses) { this.losses = losses; return this; }
        public Builder rank(int rank) { this.rank = rank; return this; }
        public Builder totalEarnings(long totalEarnings) { this.totalEarnings = totalEarnings; return this; }
        public Builder lastMatchTime(long lastMatchTime) { this.lastMatchTime = lastMatchTime; return this; }

        public ArenaStats build() {
            ArenaStats stats = new ArenaStats();
            stats.playerId = this.playerId;
            stats.arenaId = this.arenaId;
            stats.wins = this.wins;
            stats.losses = this.losses;
            stats.rank = this.rank;
            stats.totalEarnings = this.totalEarnings;
            stats.lastMatchTime = this.lastMatchTime;
            return stats;
        }
    }
}
