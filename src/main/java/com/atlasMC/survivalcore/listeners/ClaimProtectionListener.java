package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IClaimManager;
import com.atlasMC.survivalcore.models.Claim;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ClaimProtectionListener implements Listener {

    private final IClaimManager claimManager;

    public ClaimProtectionListener(IClaimManager claimManager) {
        this.claimManager = claimManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (claimManager == null) return;
        Claim claim = claimManager.getClaimAt(
                event.getBlock().getWorld().getName(),
                event.getBlock().getX(),
                event.getBlock().getZ());
        if (claim == null) return;
        if (!claimManager.canBreak(event.getPlayer().getUniqueId(), claim)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cNo tienes permiso para romper bloques aqui");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (claimManager == null) return;
        Claim claim = claimManager.getClaimAt(
                event.getBlock().getWorld().getName(),
                event.getBlock().getX(),
                event.getBlock().getZ());
        if (claim == null) return;
        if (!claimManager.canPlace(event.getPlayer().getUniqueId(), claim)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cNo tienes permiso para colocar bloques aqui");
        }
    }
}
