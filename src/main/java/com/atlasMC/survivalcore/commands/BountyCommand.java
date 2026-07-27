package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IBountyManager;
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

public class BountyCommand implements CommandExecutor, TabExecutor {

    private final IBountyManager bountyManager;
    private final MenuManager menuManager;

    public BountyCommand(IBountyManager bountyManager, MenuManager menuManager) {
        this.bountyManager = bountyManager;
        this.menuManager = menuManager;
        createBountyMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        menuManager.openMenu(player, "bounty");
        return true;
    }

    private void createBountyMenu() {
        MenuData menu = MenuFactory.createMenu("bounty", "§6Recompensas por Cabeza", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.REDSTONE, "§eRecompensas Activas",
                "bounty list", "§7Ver todas las recompensas");

        MenuFactory.addMenuSlot(menu, 1, Material.IRON_INGOT, "§eMis Recompensas",
                "bounty mylist", "§7Click para ver");

        MenuFactory.addMenuSlot(menu, 2, Material.GOLD_BLOCK, "§eHistorial",
                "bounty history", "§7Ver mis recompensas pagadas");

        MenuFactory.addMenuSlot(menu, 3, Material.DIAMOND_AXE, "§eCrear Recompensa",
                "bounty create", "§7Poner precio a una cabeza");

        menuManager.registerMenu("bounty", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
