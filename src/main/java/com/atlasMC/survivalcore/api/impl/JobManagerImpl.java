package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.JobRepository;
import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.models.PlayerJob;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

public class JobManagerImpl implements IJobManager {

    private final PlayerCache playerCache;
    private final JobRepository jobRepository;
    private final EconomyAPI economyAPI;

    public JobManagerImpl(PlayerCache playerCache, JobRepository jobRepository, EconomyAPI economyAPI) {
        this.playerCache = playerCache;
        this.jobRepository = jobRepository;
        this.economyAPI = economyAPI;
    }

    @Override
    public void joinJob(UUID uuid, JobType jobType) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) return;
        profile.setCurrentJob(jobType.name());
        PlayerJob job = PlayerJob.builder()
                .playerId(profile.getPlayerId())
                .jobType(jobType)
                .level(1)
                .exp(0)
                .moneyEarned(0)
        // job.setLastPaid(System.currentTimeMillis());
                .build();
        jobRepository.saveJob(job);
    }

    @Override
    public void leaveJob(UUID uuid, JobType jobType) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) return;
        profile.setCurrentJob(null);
    }

    @Override
    public void addExp(UUID uuid, JobType jobType, long amount) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) return;
        jobRepository.updateExp(profile.getPlayerId(), jobType, amount);
    }

    @Override
    public void getJob(UUID uuid, JobType jobType, Consumer<PlayerJob> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(null);
            return;
        }
        jobRepository.loadJob(profile.getPlayerId(), jobType, callback);
    }
}
