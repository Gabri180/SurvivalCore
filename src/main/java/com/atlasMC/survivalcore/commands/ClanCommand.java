package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IClanManager;
import com.atlasMC.survivalcore.api.IClanWarManager;
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

public class ClanCommand implements CommandExecutor, TabExecutor {

    private final IClanManager clanManager;
    private final IClanWarManager clanWarManager;

    public ClanCommand(IClanManager clanManager, IClanWarManager clanWarManager) {
        this.clanManager = clanManager;
        this.clanWarManager = clanWarManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        openClanMenu(player);
        return true;
    }

    private void openClanMenu(Player player) {
        Inventory menu = org.bukkit.Bukkit.createInventory(null, 27, "§6Clanes");

        addItem(menu, 0, Material.SHIELD, "§eCrear Clan", "§7Click para crear");
        addItem(menu, 1, Material.BOOK, "§eMi Clan", "§7Ver información");
        addItem(menu, 2, Material.BLAZE_POWDER, "§eGuerras", "§7Ver guerras activas");

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
