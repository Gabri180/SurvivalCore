package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.WeeklyBoss;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IBossManager {

    void spawnBoss(WeeklyBoss boss);

    void onBossDamaged(long bossId, int damage);

    void onBossDefeated(long bossId, UUID killerUuid);

    void recordBossDeath(LivingEntity entity, Player killer);
}
