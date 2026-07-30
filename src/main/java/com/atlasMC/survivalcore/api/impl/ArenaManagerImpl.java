package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IArenaManager;
import com.atlasMC.survivalcore.db.ArenaRepository;
import com.atlasMC.survivalcore.models.Arena;
import com.atlasMC.survivalcore.models.ArenaStats;

import java.util.*;
import java.util.stream.Collectors;

public class ArenaManagerImpl implements IArenaManager {

    private final ArenaRepository arenaRepository;
    private final EconomyAPI economyAPI;
    private final Map<UUID, String> playerArenas = new HashMap<>();
    private final Map<String, Arena> arenas = new HashMap<>();
    private final Map<UUID, Map<String, ArenaStats>> playerStats = new HashMap<>();

    public ArenaManagerImpl(ArenaRepository arenaRepository, EconomyAPI economyAPI) {
        this.arenaRepository = arenaRepository;
        this.economyAPI = economyAPI;
        initializeDefaultArenas();
    }

    private void initializeDefaultArenas() {
        Arena mainArena = Arena.builder()
                .id("main")
                .name("Arena Principal")
                .world("arena_main")
                .maxPlayers(20)
                .active(true)
                .entryFee(1000)
                .winReward(5000)
                .build();

        Arena lavaArena = Arena.builder()
                .id("lava")
                .name("Lava Arena")
                .world("arena_lava")
                .maxPlayers(10)
                .active(true)
                .entryFee(2000)
                .winReward(10000)
                .build();

        arenas.put("main", mainArena);
        arenas.put("lava", lavaArena);
    }

    @Override
    public void joinArena(UUID playerUuid, String arenaId) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) return;

        if (arena.getParticipants().size() >= arena.getMaxPlayers()) {
            return;
        }

        arena.addParticipant(playerUuid);
        playerArenas.put(playerUuid, arenaId);

        playerStats.computeIfAbsent(playerUuid, k -> new HashMap<>())
                .computeIfAbsent(arenaId, k -> new ArenaStats(playerUuid, arenaId));
    }

    @Override
    public void leaveArena(UUID playerUuid) {
        String arenaId = playerArenas.remove(playerUuid);
        if (arenaId != null) {
            Arena arena = arenas.get(arenaId);
            if (arena != null) arena.removeParticipant(playerUuid);
        }
    }

    @Override
    public Arena getArena(String arenaId) {
        return arenas.get(arenaId);
    }

    @Override
    public Collection<Arena> getAllArenas() {
        return new ArrayList<>(arenas.values());
    }

    @Override
    public Arena getPlayerArena(UUID playerUuid) {
        String arenaId = playerArenas.get(playerUuid);
        return arenaId != null ? arenas.get(arenaId) : null;
    }

    @Override
    public void recordWin(UUID playerUuid, String arenaId, long reward) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) return;

        ArenaStats stats = playerStats
                .computeIfAbsent(playerUuid, k -> new HashMap<>())
                .computeIfAbsent(arenaId, k -> new ArenaStats(playerUuid, arenaId));

        stats.addWin();
        stats.addEarnings(reward);
        stats.setLastMatchTime(System.currentTimeMillis());

        economyAPI.addBalance(playerUuid, reward);
        updateRankings(arenaId);
    }

    @Override
    public void recordLoss(UUID playerUuid, String arenaId) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) return;

        ArenaStats stats = playerStats
                .computeIfAbsent(playerUuid, k -> new HashMap<>())
                .computeIfAbsent(arenaId, k -> new ArenaStats(playerUuid, arenaId));

        stats.addLoss();
        stats.setLastMatchTime(System.currentTimeMillis());

        updateRankings(arenaId);
    }

    @Override
    public int getArenaRank(UUID playerUuid, String arenaId) {
        ArenaStats stats = playerStats.getOrDefault(playerUuid, new HashMap<>())
                .get(arenaId);
        return stats != null ? stats.getRank() : 0;
    }

    @Override
    public int getArenaWins(UUID playerUuid, String arenaId) {
        ArenaStats stats = playerStats.getOrDefault(playerUuid, new HashMap<>())
                .get(arenaId);
        return stats != null ? stats.getWins() : 0;
    }

    @Override
    public int getArenaLosses(UUID playerUuid, String arenaId) {
        ArenaStats stats = playerStats.getOrDefault(playerUuid, new HashMap<>())
                .get(arenaId);
        return stats != null ? stats.getLosses() : 0;
    }

    private void updateRankings(String arenaId) {
        List<ArenaStats> allStats = playerStats.values().stream()
                .flatMap(map -> map.values().stream())
                .filter(stats -> stats.getArenaId().equals(arenaId))
                .sorted((s1, s2) -> Integer.compare(s2.getWins(), s1.getWins()))
                .collect(Collectors.toList());

        for (int i = 0; i < allStats.size(); i++) {
            allStats.get(i).setRank(i + 1);
        }
    }
}
