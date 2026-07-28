package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.api.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemRepairManager {

    private final EconomyAPI economyAPI;

    public ItemRepairManager(EconomyAPI economyAPI) {
        this.economyAPI = economyAPI;
    }

    public boolean repairItem(Player player, ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasEnchants()) {
            player.sendMessage("§c✗ Este item no puede ser reparado");
            return false;
        }

        short maxDurability = item.getType().getMaxDurability();
        if (maxDurability == 0) {
            player.sendMessage("§c✗ Este item no tiene durabilidad");
            return false;
        }

        short damage = item.getDurability();
        if (damage == 0) {
            player.sendMessage("§c✗ Este item está en perfecto estado");
            return false;
        }

        long repairCost = (long) (damage * 100);

        if (!economyAPI.removeBalance(player.getUniqueId(), repairCost)) {
            player.sendMessage("§c✗ No tienes suficiente dinero. Costo: §e$" + repairCost);
            return false;
        }

        item.setDurability((short) 0);
        player.sendMessage("§a✓ §6Item reparado por §e$" + repairCost);
        return true;
    }

    public long getRepairCost(ItemStack item) {
        if (!item.hasItemMeta()) return 0;

        short maxDurability = item.getType().getMaxDurability();
        if (maxDurability == 0) return 0;

        short damage = item.getDurability();
        return (long) (damage * 100);
    }

    public boolean needsRepair(ItemStack item) {
        if (item.getType() == Material.AIR) return false;

        short maxDurability = item.getType().getMaxDurability();
        if (maxDurability == 0) return false;

        return item.getDurability() > 0;
    }

    public String getRepairStatus(ItemStack item) {
        if (!needsRepair(item)) return "§a100%";

        short maxDurability = item.getType().getMaxDurability();
        short damage = item.getDurability();
        double percent = (100.0 * (maxDurability - damage)) / maxDurability;

        if (percent > 75) return "§a" + (int)percent + "%";
        if (percent > 50) return "§e" + (int)percent + "%";
        if (percent > 25) return "§c" + (int)percent + "%";
        return "§4" + (int)percent + "%";
    }
}
