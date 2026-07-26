package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.menu.MenuEditorGUI;
import com.atlasMC.survivalcore.menu.ShopMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuEditCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public MenuEditCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo jugadores");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("survivalcore.admin.menuedit")) {
            player.sendMessage("§cNo tienes permiso");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Uso: /menuedit <shop>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "shop" -> {
                ShopMenu shop = new ShopMenu(plugin);
                MenuEditorGUI gui = new MenuEditorGUI(shop, plugin);
                gui.open(player);
            }
            default -> player.sendMessage("Menú desconocido");
        }
        return true;
    }
}
