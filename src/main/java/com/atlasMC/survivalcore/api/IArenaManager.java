package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Arena;

import java.util.UUID;

/**
 * Dev3 - interfaz sin implementacion. Se conecta a BD manana.
 */
public interface IArenaManager {

    void joinArena(UUID playerUuid, String arenaId);

    void leaveArena(UUID playerUuid);

    Arena getArena(String arenaId);
}
