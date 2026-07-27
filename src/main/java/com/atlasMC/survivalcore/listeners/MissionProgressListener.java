package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.api.IMissionManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Detecta progreso de misiones automáticamente basado en eventos de jugador.
 * v1.0.25+: Mejorado con más tipos de misiones y auto-detección
 */
public class MissionProgressListener implements Listener {

    private final IMissionManager missionManager;

    public MissionProgressListener(IMissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (missionManager == null || event.isCancelled()) return;

        Block block = event.getBlock();
        Material material = block.getType();

        // Actualizar misión BLOCKS_BROKEN
        missionManager.updateProgress(event.getPlayer().getUniqueId(), 1, 1);

        // Actualizar misiones específicas por tipo de bloque
        if (material.name().contains("ORE")) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 4, 1); // Ore mining
        } else if (material.name().contains("LOG")) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 5, 1); // Wood chopping
        } else if (material.name().contains("STONE")) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 6, 1); // Stone breaking
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (missionManager == null || event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        // Actualizar misión FISH_CAUGHT
        missionManager.updateProgress(event.getPlayer().getUniqueId(), 2, 1);

        // Actualizar misiones específicas por cantidad
        if (event.getExpToDrop() > 3) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 7, 1); // Big catch
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (missionManager == null || event.getEntity().getKiller() == null) return;

        EntityType entityType = event.getEntity().getType();
        int kills = event.getDroppedExp() / 10;

        // Actualizar misión KILLS
        missionManager.updateProgress(event.getEntity().getKiller().getUniqueId(), 3, kills);

        // Actualizar misiones específicas por tipo de mob
        if (entityType.isAlive()) {
            switch (entityType) {
                case ZOMBIE, HUSK, DROWNED -> 
                    missionManager.updateProgress(event.getEntity().getKiller().getUniqueId(), 8, kills);
                case SKELETON, WITHER_SKELETON, STRAY -> 
                    missionManager.updateProgress(event.getEntity().getKiller().getUniqueId(), 9, kills);
                case ENDERMAN, ENDERMITE -> 
                    missionManager.updateProgress(event.getEntity().getKiller().getUniqueId(), 10, kills);
                case CREEPER -> 
                    missionManager.updateProgress(event.getEntity().getKiller().getUniqueId(), 11, kills);
                default -> {
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (missionManager == null || event.isCancelled()) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Material material = block.getType();

        // Actualizar misiones de interacción
        if (material.name().contains("FURNACE")) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 12, 1);
        } else if (material == Material.CRAFTING_TABLE) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 13, 1);
        } else if (material.name().contains("ANVIL")) {
            missionManager.updateProgress(event.getPlayer().getUniqueId(), 14, 1);
        }
    }
}
