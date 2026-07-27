package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IAuctionManager;
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

public class AuctionCommand implements CommandExecutor, TabExecutor {

    private final IAuctionManager auctionManager;
    private final MenuManager menuManager;

    public AuctionCommand(IAuctionManager auctionManager, MenuManager menuManager) {
        this.auctionManager = auctionManager;
        this.menuManager = menuManager;
        createAuctionMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        menuManager.openMenu(player, "auction");
        return true;
    }

    private void createAuctionMenu() {
        MenuData menu = MenuFactory.createMenu("auction", "§6Casa de Subastas", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.DIAMOND, "§eSubastas Activas",
                "auction list", "§7Ver todas las subastas");

        MenuFactory.addMenuSlot(menu, 1, Material.EMERALD, "§eMis Subastas",
                "auction mylist", "§7Click para ver");

        MenuFactory.addMenuSlot(menu, 2, Material.GOLD_INGOT, "§eMis Pujas",
                "auction bids", "§7Ver historial");

        MenuFactory.addMenuSlot(menu, 3, Material.ENDER_PEARL, "§eVender Item",
                "auction sell", "§7Poner item a la venta");

        menuManager.registerMenu("auction", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
