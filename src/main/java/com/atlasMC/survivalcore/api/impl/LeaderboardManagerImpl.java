package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.ILeaderboardManager;
import com.atlasMC.survivalcore.leaderboard.LeaderboardEntry;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LeaderboardManagerImpl implements ILeaderboardManager {
    private final Map<LeaderboardType, List<LeaderboardEntry>> leaderboards = new ConcurrentHashMap<>();

    public LeaderboardManagerImpl() {
        for (LeaderboardType type : LeaderboardType.values()) {
            leaderboards.put(type, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    @Override
    public void updateLeaderboard(LeaderboardType type) {
        List<LeaderboardEntry> entries = leaderboards.get(type);
        if (entries != null) {
            entries.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            int rank = 1;
            for (LeaderboardEntry entry : entries) {
                entry.setRank(rank++);
            }
        }
    }

    @Override
    public void updateAllLeaderboards() {
        for (LeaderboardType type : LeaderboardType.values()) {
            updateLeaderboard(type);
        }
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(LeaderboardType type, int page, int perPage) {
        updateLeaderboard(type);
        List<LeaderboardEntry> entries = leaderboards.get(type);
        if (entries == null) return new ArrayList<>();

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, entries.size());

        return entries.subList(start, end);
    }

    @Override
    public List<LeaderboardEntry> getTopPlayers(LeaderboardType type, int limit) {
        updateLeaderboard(type);
        List<LeaderboardEntry> entries = leaderboards.get(type);
        if (entries == null) return new ArrayList<>();

        return entries.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LeaderboardEntry> getPlayerRank(UUID uuid, LeaderboardType type) {
        updateLeaderboard(type);
        List<LeaderboardEntry> entries = leaderboards.get(type);
        if (entries == null) return Optional.empty();

        return entries.stream()
                .filter(e -> e.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public int getPlayerPosition(UUID uuid, LeaderboardType type) {
        return getPlayerRank(uuid, type)
                .map(LeaderboardEntry::getRank)
                .orElse(-1);
    }

    @Override
    public void clearLeaderboard(LeaderboardType type) {
        List<LeaderboardEntry> entries = leaderboards.get(type);
        if (entries != null) {
            entries.clear();
        }
    }

    @Override
    public void saveLeaderboards() {
    }

    @Override
    public void loadLeaderboards() {
    }

    public void addOrUpdateEntry(LeaderboardType type, UUID uuid, String playerName, double value) {
        List<LeaderboardEntry> entries = leaderboards.get(type);
        if (entries == null) return;

        entries.stream()
                .filter(e -> e.getUuid().equals(uuid))
                .findFirst()
                .ifPresentOrElse(
                        e -> e.setValue(value),
                        () -> entries.add(new LeaderboardEntry(uuid, playerName, value))
                );
    }
}
