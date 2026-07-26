package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.enums.JobType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class JobBlockBreakListener implements Listener {

    private final IJobManager jobManager;

    public JobBlockBreakListener(IJobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (jobManager == null || event.getPlayer().isOp()) return;
        jobManager.addExp(event.getPlayer().getUniqueId(), JobType.MINER, 10L);
    }
}
