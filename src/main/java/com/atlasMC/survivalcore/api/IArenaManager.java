package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Arena;

import java.util.Collection;
import java.util.UUID;

public interface IArenaManager {

    void joinArena(UUID playerUuid, String arenaId);

    void leaveArena(UUID playerUuid);

    Arena getArena(String arenaId);

    Collection<Arena> getAllArenas();

    Arena getPlayerArena(UUID playerUuid);

    void recordWin(UUID playerUuid, String arenaId, long reward);

    void recordLoss(UUID playerUuid, String arenaId);

    int getArenaRank(UUID playerUuid, String arenaId);

    int getArenaWins(UUID playerUuid, String arenaId);

    int getArenaLosses(UUID playerUuid, String arenaId);
}
