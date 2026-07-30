package com.atlasMC.survivalcore.leaderboard;

import java.util.UUID;

public class LeaderboardEntry {
    private UUID uuid;
    private String playerName;
    private double value;
    private int rank;

    public LeaderboardEntry(UUID uuid, String playerName, double value) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.value = value;
        this.rank = 0;
    }

    public UUID getUuid() { return uuid; }
    public String getPlayerName() { return playerName; }
    public double getValue() { return value; }
    public int getRank() { return rank; }

    public void setValue(double value) { this.value = value; }
    public void setRank(int rank) { this.rank = rank; }

    @Override
    public String toString() {
        return String.format("#%d %s - %.0f", rank, playerName, value);
    }
}
