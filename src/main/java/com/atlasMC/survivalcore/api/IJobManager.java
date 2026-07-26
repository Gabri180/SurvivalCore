package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.models.PlayerJob;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Placeholder de Hauch. Implementacion pendiente: conectar a
 * {@link com.atlasMC.survivalcore.db.JobRepository}.
 */
public interface IJobManager {

    void joinJob(UUID uuid, JobType jobType);

    void leaveJob(UUID uuid, JobType jobType);

    void addExp(UUID uuid, JobType jobType, long amount);

    void getJob(UUID uuid, JobType jobType, Consumer<PlayerJob> callback);
}
