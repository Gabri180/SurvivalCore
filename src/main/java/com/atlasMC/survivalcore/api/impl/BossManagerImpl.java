package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.IBossManager;
import com.atlasMC.survivalcore.db.BossRepository;
import com.atlasMC.survivalcore.models.WeeklyBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class BossManagerImpl implements IBossManager {

    private final BossRepository bossRepository;
    private final Map<Long, WeeklyBoss> bossesCache = new HashMap<>();
    private final Map<Long, Map<UUID, Integer>> bossDamageTracking = new HashMap<>();

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
            Map<UUID, Integer> damageMap = bossDamageTracking.getOrDefault(bossId, new HashMap<>());
            int topDamage = damageMap.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            Player killer = Bukkit.getPlayer(killerUuid);

            if (killer != null) {
                Bukkit.broadcastMessage("§6§l⚔ §r§6El jefe §e" + boss.getBossType() + " §6fue derrotado por §e" + killer.getName());
                killer.sendMessage("§a✓ §6Ganaste §e$" + boss.getReward() + " §6y §e" + boss.getExp() + " §6exp");
            }

            bossRepository.markDefeated(bossId, 0);
            bossDamageTracking.remove(bossId);
        }
    }

    @Override
    public void recordBossDeath(LivingEntity entity, Player killer) {
        onBossDefeated(0, killer.getUniqueId());
    }

    public void trackDamage(long bossId, UUID playerUuid, int damage) {
        Map<UUID, Integer> damageMap = bossDamageTracking.computeIfAbsent(bossId, k -> new HashMap<>());
        damageMap.put(playerUuid, damageMap.getOrDefault(playerUuid, 0) + damage);
    }

    public List<UUID> getTopDamagers(long bossId, int limit) {
        Map<UUID, Integer> damageMap = bossDamageTracking.getOrDefault(bossId, new HashMap<>());
        return damageMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}
