package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IMissionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class MissionProgressListener implements Listener {

    private final IMissionManager missionManager;

    public MissionProgressListener(IMissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (missionManager == null) return;
        missionManager.updateProgress(event.getPlayer().getUniqueId(), 1, 1);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (missionManager == null || event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        missionManager.updateProgress(event.getPlayer().getUniqueId(), 2, 1);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (missionManager == null || event.getEntity().getKiller() == null) return;
        missionManager.updateProgress(event.getEntity().getKiller().getUniqueId(), 3, 1);
    }
}
