package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.enums.JobType;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class JobFishingListener implements Listener {

    private final IJobManager jobManager;

    public JobFishingListener(IJobManager jobManager) {
        this.jobManager = jobManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        if (jobManager == null) return;

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            long exp = 15L;
            if (event.getCaught() instanceof Item item) {
                exp = getExpForFish(item.getItemStack());
            }

            if (exp > 0) {
                jobManager.addExp(event.getPlayer().getUniqueId(), JobType.FISHERMAN, exp);
            }
        }
    }

    private long getExpForFish(ItemStack caught) {
        if (caught == null) return 5L;

        return switch (caught.getType()) {
            case TROPICAL_FISH -> 30L;
            case PUFFERFISH -> 25L;
            case SALMON -> 20L;
            case COD -> 15L;
            case FISHING_ROD -> 50L;
            case ENCHANTED_BOOK -> 80L;
            case SADDLE, NAME_TAG, NAUTILUS_SHELL -> 60L;
            default -> 5L;
        };
    }
}
