package com.atlasMC.survivalcore.events;


import java.util.UUID;

/**
 * Emitido via EventAPI cuando un jugador completa una mision.
 */
public class MissionCompleteEvent {

    private final UUID playerUuid;
    private final long missionId;

    public MissionCompleteEvent(UUID playerUuid, long missionId) {
        this.playerUuid = playerUuid;
        this.missionId = missionId;
    }

    public UUID getPlayerUuid() { return playerUuid; }
    public long getMissionId() { return missionId; }
}
