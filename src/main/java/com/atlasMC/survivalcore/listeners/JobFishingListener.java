package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.enums.JobType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Placeholder de Hauch: estructura lista, logica de exp pendiente de
 * conectar a {@link IJobManager} manana.
 */
public class JobFishingListener implements Listener {

    private final IJobManager jobManager;

    public JobFishingListener(IJobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (jobManager == null || event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        jobManager.addExp(event.getPlayer().getUniqueId(), JobType.FISHERMAN, 15L);
    }
}
