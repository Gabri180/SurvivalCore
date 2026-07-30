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

    @Override
    public void getPlayerMissions(UUID uuid, Consumer<List<Mission>> callback) {
        getActiveMissions(uuid, callback);
    }

    @Override
    public void claimReward(UUID uuid, long missionId, Consumer<Boolean> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(false);
            return;
        }

        missionRepository.loadPlayerProgress(profile.getPlayerId(), progresses -> {
            MissionProgress prog = progresses.stream()
                    .filter(p -> p.getMissionId() == missionId)
                    .findFirst()
                    .orElse(null);

            if (prog == null || !prog.isCompleted() || prog.isClaimed()) {
                callback.accept(false);
                return;
            }

            Mission mission = missionsCache.get(missionId);
            if (mission == null) {
                callback.accept(false);
                return;
            }

            prog.setClaimed(true);
            missionRepository.saveMissionProgress(prog);
            economyAPI.addBalance(uuid, mission.getRewardMoney());
            callback.accept(true);
        });
    }

    @Override
    public void claimAllRewards(UUID uuid, Consumer<Integer> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(0);
            return;
        }

        missionRepository.loadPlayerProgress(profile.getPlayerId(), progresses -> {
            int count = 0;
            for (MissionProgress prog : progresses) {
                if (prog.isCompleted() && !prog.isClaimed()) {
                    Mission mission = missionsCache.get(prog.getMissionId());
                    if (mission != null) {
                        prog.setClaimed(true);
                        missionRepository.saveMissionProgress(prog);
                        economyAPI.addBalance(uuid, mission.getRewardMoney());
                        count++;
                    }
                }
            }
            callback.accept(count);
        });
    }

    @Override
    public void getCompletedMissions(UUID uuid, Consumer<List<Mission>> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(List.of());
            return;
        }

        missionRepository.loadPlayerProgress(profile.getPlayerId(), progresses -> {
            List<Mission> completed = progresses.stream()
                    .filter(p -> p.isCompleted() && !p.isClaimed())
                    .map(p -> missionsCache.get(p.getMissionId()))
                    .toList();
            callback.accept(completed);
        });
    }

    @Override
    public void getMissionReward(long missionId, Consumer<Long> moneyCallback, Consumer<Long> xpCallback) {
        Mission mission = missionsCache.get(missionId);
        if (mission != null) {
            moneyCallback.accept(mission.getRewardMoney());
            xpCallback.accept(mission.getRewardExp());
        } else {
            moneyCallback.accept(0L);
            xpCallback.accept(0L);
        }
    }
}
