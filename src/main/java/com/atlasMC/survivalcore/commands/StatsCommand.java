package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.ISkillManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatsCommand implements CommandExecutor {

    private final ISkillManager skillManager;
    private final PlayerCache playerCache;

    public StatsCommand(ISkillManager skillManager, PlayerCache playerCache) {
        this.skillManager = skillManager;
        this.playerCache = playerCache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;
        PlayerProfile profile = playerCache.get(player.getUniqueId());

        if (profile == null) {
            player.sendMessage("§cTu perfil no fue cargado.");
            return true;
        }

        openStatsMenu(player, profile);
        return true;
    }

    private void openStatsMenu(Player player, PlayerProfile profile) {
        Inventory menu = org.bukkit.Bukkit.createInventory(null, 27, "§6Estadísticas");

        // Fila 1: Información general
        addItem(menu, 0, Material.PLAYER_HEAD, "§6Rango", "§7" + profile.getRank());
        addItem(menu, 1, Material.AMETHYST_CLUSTER, "§6Prestigio", "§7" + profile.getPrestige());
        addItem(menu, 2, Material.SUNFLOWER, "§6Dinero", "§7§e$" + profile.getMoney());

        // Fila 2: Skills
        int slot = 9;
        for (String skillName : new String[]{"MINING", "FARMING", "COMBAT", "FISHING", "WOODCUTTING"}) {
            addItem(menu, slot, Material.BOOKSHELF, "§e" + skillName, "§7Nivel: §b1");
            slot++;
        }

        player.openInventory(menu);
    }

    private void addItem(Inventory inv, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            java.util.List<String> loreList = new java.util.ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        inv.setItem(slot, item);
    }
}
