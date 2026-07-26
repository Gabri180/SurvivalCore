package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Mission;
import com.atlasMC.survivalcore.models.MissionProgress;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Placeholder de Hauch. Implementacion pendiente: conectar a
 * {@link com.atlasMC.survivalcore.db.MissionRepository}.
 */
public interface IMissionManager {

    void getActiveMissions(UUID uuid, Consumer<List<Mission>> callback);

    void updateProgress(UUID uuid, long missionId, int amount);

    void getProgress(UUID uuid, long missionId, Consumer<MissionProgress> callback);
}
