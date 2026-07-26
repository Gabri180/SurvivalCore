package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IArenaManager;
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

public class ArenaCommand implements CommandExecutor, TabExecutor {

    private final IArenaManager arenaManager;

    public ArenaCommand(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        openArenaMenu(player);
        return true;
    }

    private void openArenaMenu(Player player) {
        Inventory menu = org.bukkit.Bukkit.createInventory(null, 27, "§6Arenas PvP");

        addItem(menu, 0, Material.DIAMOND_SWORD, "§eArena Principal", "§7Click para unirse", "§7Jugadores: §b0/20");
        addItem(menu, 1, Material.IRON_SWORD, "§eArena Secundaria", "§7Click para unirse", "§7Jugadores: §b0/20");
        addItem(menu, 2, Material.GOLDEN_SWORD, "§eLava Arena", "§7Click para unirse", "§7Jugadores: §b0/20");

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
