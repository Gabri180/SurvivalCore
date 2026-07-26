package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.IBossManager;
import com.atlasMC.survivalcore.db.BossRepository;
import com.atlasMC.survivalcore.models.WeeklyBoss;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossManagerImpl implements IBossManager {

    private final BossRepository bossRepository;
    private final Map<Long, WeeklyBoss> bossesCache = new HashMap<>();

    public BossManagerImpl(BossRepository bossRepository) {
        this.bossRepository = bossRepository;
        bossRepository.loadRecent(bosses -> {
            bossesCache.clear();
            bosses.forEach(b -> bossesCache.put(b.getId(), b));
        });
    }

    @Override
    public void spawnBoss(WeeklyBoss boss) {
        bossRepository.saveBoss(boss, id -> {
            boss.setId(id);
            bossesCache.put(id, boss);
        });
    }

    @Override
    public void onBossDamaged(long bossId, int damage) {
        WeeklyBoss boss = bossesCache.get(bossId);
        if (boss != null) {
            boss.damage(damage);
        }
    }

    @Override
    public void onBossDefeated(long bossId, UUID killerUuid) {
        WeeklyBoss boss = bossesCache.get(bossId);
        if (boss != null) {
            bossRepository.markDefeated(bossId, 0);
        }
    }

    @Override
    public void recordBossDeath(LivingEntity entity, Player killer) {
        onBossDefeated(0, killer.getUniqueId());
    }
}
