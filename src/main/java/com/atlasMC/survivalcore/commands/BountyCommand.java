package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IBountyManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BountyCommand implements CommandExecutor, TabExecutor {

    private final IBountyManager bountyManager;

    public BountyCommand(IBountyManager bountyManager) {
        this.bountyManager = bountyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        openBountyMenu(player);
        return true;
    }

    private void openBountyMenu(Player player) {
        Inventory menu = org.bukkit.Bukkit.createInventory(null, 27, "§6Recompensas");

        addItem(menu, 0, Material.REDSTONE, "§eRecompensas Activas", "§7Ver todas");
        addItem(menu, 1, Material.IRON_INGOT, "§eMis Recompensas", "§7Click para ver");
        addItem(menu, 2, Material.GOLD_BLOCK, "§eHistorial", "§7Ver mis compras");

        player.openInventory(menu);
    }

    private void addItem(Inventory inv, int slot, Material material, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (lore.length > 0) meta.setDisplayName(lore[0]);
            List<String> loreList = new ArrayList<>();
            for (int i = 1; i < lore.length; i++) {
                loreList.add(lore[i]);
            }
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        inv.setItem(slot, item);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
