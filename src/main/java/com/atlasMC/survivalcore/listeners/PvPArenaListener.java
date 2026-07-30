package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PvPArenaListener implements Listener {

    private final IArenaManager arenaManager;

    public PvPArenaListener(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (arenaManager == null) return;
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;
        // arenaManager.recordDeath(victim.getUniqueId(), killer.getUniqueId());
    }
}
