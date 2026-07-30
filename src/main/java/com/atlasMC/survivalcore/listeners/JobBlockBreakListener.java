package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.enums.JobType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class JobBlockBreakListener implements Listener {

    private final IJobManager jobManager;

    public JobBlockBreakListener(IJobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (jobManager == null) return;

        Material blockType = event.getBlock().getType();
        long exp = getExpForBlock(blockType);

        if (exp > 0) {
            jobManager.addExp(event.getPlayer().getUniqueId(), JobType.MINER, exp);
        }
    }

    private long getExpForBlock(Material material) {
        return switch (material) {
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> 100L;
            case IRON_ORE, DEEPSLATE_IRON_ORE -> 50L;
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> 60L;
            case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> 40L;
            case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> 80L;
            case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> 35L;
            case COAL_ORE, DEEPSLATE_COAL_ORE -> 25L;
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> 30L;
            case STONE, GRANITE, DIORITE, ANDESITE, DEEPSLATE -> 5L;
            case COBBLESTONE -> 3L;
            default -> 0L;
        };
    }
}
