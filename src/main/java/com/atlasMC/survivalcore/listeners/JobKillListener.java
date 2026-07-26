package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.enums.JobType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Placeholder de Hauch: estructura lista, logica de exp pendiente de
 * conectar a {@link IJobManager} manana.
 */
public class JobKillListener implements Listener {

    private final IJobManager jobManager;

    public JobKillListener(IJobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (jobManager == null || killer == null) return;
        jobManager.addExp(killer.getUniqueId(), JobType.HUNTER, 25L);
    }
}
