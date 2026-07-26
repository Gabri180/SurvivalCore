package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IArenaManager;
import com.atlasMC.survivalcore.db.ArenaRepository;
import com.atlasMC.survivalcore.models.Arena;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaManagerImpl implements IArenaManager {

    private final ArenaRepository arenaRepository;
    private final EconomyAPI economyAPI;
    private final Map<UUID, String> playerArenas = new HashMap<>();
    private final Map<String, Arena> arenas = new HashMap<>();

    public ArenaManagerImpl(ArenaRepository arenaRepository, EconomyAPI economyAPI) {
        this.arenaRepository = arenaRepository;
        this.economyAPI = economyAPI;
    }

    @Override
    public void joinArena(UUID playerUuid, String arenaId) {
        Arena arena = arenas.get(arenaId);
        if (arena == null) return;
        playerArenas.put(playerUuid, arenaId);
        // participants conversion
    }

    @Override
    public void leaveArena(UUID playerUuid) {
        String arenaId = playerArenas.remove(playerUuid);
        if (arenaId != null) {
            Arena arena = arenas.get(arenaId);
            if (arena != null) arena.getParticipants().remove(playerUuid);
        }
    }

    @Override
    public Arena getArena(String arenaId) {
        return arenas.get(arenaId);
    }
}
