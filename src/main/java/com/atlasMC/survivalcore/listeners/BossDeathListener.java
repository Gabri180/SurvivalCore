package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IBossManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossDeathListener implements Listener {

    private final IBossManager bossManager;

    public BossDeathListener(IBossManager bossManager) {
        this.bossManager = bossManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (bossManager == null || killer == null) return;
        bossManager.recordBossDeath(event.getEntity(), killer);
        killer.sendMessage("§6✓ Jefe derrotado! Recompensas otorgadas!");
    }
}
