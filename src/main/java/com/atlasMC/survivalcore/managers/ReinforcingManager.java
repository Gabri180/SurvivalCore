package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.enums.ReinforcementLevel;
import com.atlasMC.survivalcore.models.ReinforcedBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.*;

public class ReinforcingManager {

    private final Map<String, ReinforcedBlock> reinforcedBlocks = new HashMap<>();
    private final EconomyAPI economyAPI;
    private long nextBlockId = 1;

    public ReinforcingManager(EconomyAPI economyAPI) {
        this.economyAPI = economyAPI;
    }

    public boolean reinforceBlock(Location location, long claimId, UUID playerUuid, ReinforcementLevel level) {
        if (level == ReinforcementLevel.NONE) return false;

        if (!economyAPI.removeBalance(playerUuid, level.getCost())) {
            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null) {
                player.sendMessage("§c✗ No tienes suficiente dinero. Necesitas §e$" + level.getCost());
            }
            return false;
        }

        String key = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().getName();
        ReinforcedBlock reinforced = ReinforcedBlock.builder()
                .id(nextBlockId++)
                .claimId(claimId)
                .x(location.getBlockX())
                .y(location.getBlockY())
                .z(location.getBlockZ())
                .world(location.getWorld().getName())
                .level(level)
                .health(100 + (level.getLevel() * 50))
                .maxHealth(100 + (level.getLevel() * 50))
                .reinforcedAt(Instant.now())
                .build();

        reinforcedBlocks.put(key, reinforced);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Bloque reforzado a nivel §e" + level.name() + " §6por §e$" + level.getCost());
        }

        return true;
    }

    public ReinforcedBlock getBlock(Location location) {
        String key = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().getName();
        return reinforcedBlocks.get(key);
    }

    public void damageBlock(Location location, long damage) {
        ReinforcedBlock block = getBlock(location);
        if (block == null) return;

        block.takeDamage(damage);

        if (block.isDestroyed()) {
            reinforcedBlocks.remove(getKey(location));
            location.getBlock().breakNaturally();
        }
    }

    public boolean upgradeBlock(Location location, UUID playerUuid) {
        ReinforcedBlock block = getBlock(location);
        if (block == null || block.getLevel() == ReinforcementLevel.OBSIDIAN) return false;

        ReinforcementLevel nextLevel = block.getLevel().upgrade();
        if (!economyAPI.removeBalance(playerUuid, nextLevel.getCost())) return false;

        block.setLevel(nextLevel);
        block.setHealth(block.getMaxHealth() + 50);
        block.setMaxHealth(block.getMaxHealth() + 50);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Bloque mejorado a §e" + nextLevel.name());
        }

        return true;
    }

    public void repairBlock(Location location, UUID playerUuid) {
        ReinforcedBlock block = getBlock(location);
        if (block == null) return;

        long repairCost = (block.getMaxHealth() - block.getHealth()) * 10;
        if (!economyAPI.removeBalance(playerUuid, repairCost)) {
            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null) {
                player.sendMessage("§c✗ No tienes suficiente dinero. Costo: §e$" + repairCost);
            }
            return;
        }

        block.setHealth(block.getMaxHealth());

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Bloque reparado por §e$" + repairCost);
        }
    }

    public int getClaimReinforcedBlockCount(long claimId) {
        return (int) reinforcedBlocks.values().stream()
                .filter(b -> b.getClaimId() == claimId)
                .count();
    }

    private String getKey(Location location) {
        return location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().getName();
    }
}
