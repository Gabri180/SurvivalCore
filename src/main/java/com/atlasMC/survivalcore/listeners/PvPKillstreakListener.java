package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IBountyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PvPKillstreakListener implements Listener {

    private final IBountyManager bountyManager;

    public PvPKillstreakListener(IBountyManager bountyManager) {
        this.bountyManager = bountyManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (bountyManager == null || killer == null) return;
        bountyManager.incrementKillstreak(killer.getUniqueId());
    }
}
