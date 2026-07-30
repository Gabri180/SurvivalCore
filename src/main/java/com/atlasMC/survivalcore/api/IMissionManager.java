package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Mission;
import com.atlasMC.survivalcore.models.MissionProgress;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Interfaz de gestor de misiones diarias/semanales.
 * v1.0.25+: Mejorada con métodos para reclamar recompensas
 */
public interface IMissionManager {

    void getActiveMissions(UUID uuid, Consumer<List<Mission>> callback);

    void updateProgress(UUID uuid, long missionId, int amount);

    void getProgress(UUID uuid, long missionId, Consumer<MissionProgress> callback);

    /**
     * Obtiene todas las misiones del jugador.
     */
    void getPlayerMissions(UUID uuid, Consumer<List<Mission>> callback);

    /**
     * Reclama la recompensa de una misión completada.
     *
     * @param uuid UUID del jugador
     * @param missionId ID de la misión
     * @param callback Retorna true si se reclamó con éxito
     */
    void claimReward(UUID uuid, long missionId, Consumer<Boolean> callback);

    /**
     * Reclama todas las recompensas de misiones completadas.
     *
     * @param uuid UUID del jugador
     * @param callback Retorna la cantidad de misiones reclamadas
     */
    void claimAllRewards(UUID uuid, Consumer<Integer> callback);

    /**
     * Obtiene la lista de misiones completadas pero no reclamadas.
     */
    void getCompletedMissions(UUID uuid, Consumer<List<Mission>> callback);

    /**
     * Obtiene las recompensas totales de una misión.
     */
    void getMissionReward(long missionId, Consumer<Long> moneyCallback, Consumer<Long> xpCallback);
}
