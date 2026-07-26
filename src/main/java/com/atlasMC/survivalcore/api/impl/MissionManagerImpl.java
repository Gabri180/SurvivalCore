package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.EventAPI;
import com.atlasMC.survivalcore.api.IMissionManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.MissionRepository;
import com.atlasMC.survivalcore.events.MissionCompleteEvent;
import com.atlasMC.survivalcore.models.Mission;
import com.atlasMC.survivalcore.models.MissionProgress;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class MissionManagerImpl implements IMissionManager {

    private final PlayerCache playerCache;
    private final MissionRepository missionRepository;
    private final EconomyAPI economyAPI;
    private final EventAPI eventAPI;
    private final Map<Long, Mission> missionsCache = new HashMap<>();

    public MissionManagerImpl(PlayerCache playerCache, MissionRepository missionRepository, EconomyAPI economyAPI, EventAPI eventAPI, JavaPlugin plugin) {
        this.playerCache = playerCache;
        this.missionRepository = missionRepository;
        this.economyAPI = economyAPI;
        this.eventAPI = eventAPI;

        missionRepository.loadMissions(missions -> missionsCache.clear());

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            missionRepository.loadMissions(missions -> {
                missionsCache.clear();
                missions.forEach(m -> missionsCache.put(m.getId(), m));
            });
        }, 0, 20 * 60 * 20);
    }

    @Override
    public void getActiveMissions(UUID uuid, Consumer<List<Mission>> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(List.of());
            return;
        }
        missionRepository.loadPlayerProgress(profile.getPlayerId(), progresses -> {
            List<Mission> active = progresses.stream()
                    .filter(p -> !p.isCompleted())
                    .map(p -> missionsCache.get(p.getMissionId()))
                    .toList();
            callback.accept(active);
        });
    }

    @Override
    public void updateProgress(UUID uuid, long missionId, int amount) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) return;
        missionRepository.loadPlayerProgress(profile.getPlayerId(), progresses -> {
            MissionProgress prog = progresses.stream()
                    .filter(p -> p.getMissionId() == missionId)
                    .findFirst()
                    .orElse(null);
            if (prog == null) return;

            prog.setProgress(prog.getProgress() + amount);
            Mission mission = missionsCache.get(missionId);
            if (mission != null && prog.getProgress() >= mission.getRequiredCount()) {
                prog.setCompleted(true);
                economyAPI.addBalance(uuid, mission.getRewardMoney());
                eventAPI.emit(new MissionCompleteEvent(uuid, missionId));
            }
            missionRepository.saveMissionProgress(prog);
        });
    }

    @Override
    public void getProgress(UUID uuid, long missionId, Consumer<MissionProgress> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(null);
            return;
        }
        missionRepository.loadPlayerProgress(profile.getPlayerId(), progresses ->
                callback.accept(progresses.stream()
                        .filter(p -> p.getMissionId() == missionId)
                        .findFirst()
                        .orElse(null)));
    }
}
