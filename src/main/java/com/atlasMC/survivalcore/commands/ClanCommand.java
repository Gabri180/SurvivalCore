package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IClanManager;
import com.atlasMC.survivalcore.api.IClanWarManager;
import com.atlasMC.survivalcore.menu.MenuAction;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class ClanCommand implements CommandExecutor, TabExecutor {

    private final IClanManager clanManager;
    private final IClanWarManager clanWarManager;
    private final MenuManager menuManager;

    public ClanCommand(IClanManager clanManager, IClanWarManager clanWarManager, MenuManager menuManager) {
        this.clanManager = clanManager;
        this.clanWarManager = clanWarManager;
        this.menuManager = menuManager;
        createClanMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        menuManager.openMenu(player, "clans");
        return true;
    }

    private void createClanMenu() {
        MenuData menu = MenuFactory.createMenu("clans", "§6Clanes", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.SHIELD, "§eCrear Clan",
                MenuAction.command("clan create"), "§7Click para crear");

        MenuFactory.addMenuSlot(menu, 1, Material.BOOK, "§eMi Clan",
                MenuAction.command("clan info"), "§7Ver información");

        MenuFactory.addMenuSlot(menu, 2, Material.BLAZE_POWDER, "§eGuerras",
                MenuAction.command("clan wars"), "§7Ver guerras activas");

        MenuFactory.addMenuSlot(menu, 3, Material.PLAYER_HEAD, "§eMiembros",
                MenuAction.command("clan members"), "§7Ver miembros");

        MenuFactory.addMenuSlot(menu, 4, Material.EMERALD_BLOCK, "§eTesorería",
                MenuAction.command("clan bank"), "§7Ver dinero del clan");

        menuManager.registerMenu("clans", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
