package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.notifications.NotificationManager;
import com.atlasMC.survivalcore.notifications.NotificationPreferences;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class NotificationsCommand implements CommandExecutor, TabExecutor {

    private final NotificationManager notificationManager;
    private final MenuManager menuManager;

    public NotificationsCommand(NotificationManager notificationManager, MenuManager menuManager) {
        this.notificationManager = notificationManager;
        this.menuManager = menuManager;
        createNotificationMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "notifications");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "toggle" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /notificaciones toggle <tipo>");
                    showToggleHelp(player);
                } else {
                    toggleNotification(player, args[1]);
                }
            }
            case "status" -> showStatus(player);
            default -> menuManager.openMenu(player, "notifications");
        }
        return true;
    }

    private void toggleNotification(Player player, String type) {
        NotificationPreferences prefs = notificationManager.getPlayerPreferences(player.getUniqueId());

        try {
            NotificationManager.NotificationType notifType = NotificationManager.NotificationType.valueOf(type.toUpperCase());
            prefs.toggleNotificationType(notifType);
            boolean enabled = prefs.isNotificationEnabled(notifType);
            String status = enabled ? "§aActivada" : "§cDesactivada";
            player.sendMessage(String.format("§eNotificaciones de %s: %s", notifType.getDisplayName(), status));
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cTipo de notificación desconocido: " + type);
            showToggleHelp(player);
        }
    }

    private void showToggleHelp(Player player) {
        player.sendMessage("§6Tipos disponibles: arena, clan, auction, bounty, job");
        player.sendMessage("§7También puedes toggle: sounds, titles, chat");
    }

    private void showStatus(Player player) {
        NotificationPreferences prefs = notificationManager.getPlayerPreferences(player.getUniqueId());
        player.sendMessage("§6=== Estado de Notificaciones ===");
        player.sendMessage(formatStatus("Arena", prefs.isArenaNotifications()));
        player.sendMessage(formatStatus("Clan", prefs.isClanNotifications()));
        player.sendMessage(formatStatus("Subasta", prefs.isAuctionNotifications()));
        player.sendMessage(formatStatus("Recompensa", prefs.isBountyNotifications()));
        player.sendMessage(formatStatus("Trabajo", prefs.isJobNotifications()));
        player.sendMessage("§6=== Opciones Globales ===");
        player.sendMessage(formatStatus("Sonidos", prefs.isSoundsEnabled()));
        player.sendMessage(formatStatus("Títulos", prefs.isTitlesEnabled()));
        player.sendMessage(formatStatus("Chat", prefs.isChatEnabled()));
    }

    private String formatStatus(String name, boolean enabled) {
        String status = enabled ? "§a✓ Activada" : "§c✗ Desactivada";
        return String.format("§e• %s: %s", name, status);
    }

    private void createNotificationMenu() {
        MenuData menu = MenuFactory.createMenu("notifications", "§6Gestionar Notificaciones", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.DIAMOND_PICKAXE, "§eArena",
                "Notificaciones de arena", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 1, Material.SHIELD, "§eClan",
                "Notificaciones de clan", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 2, Material.GOLD_INGOT, "§eSubasta",
                "Notificaciones de auction", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 3, Material.REDSTONE, "§eRecompensa",
                "Notificaciones de bounty", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 4, Material.PAPER, "§eTrabajo",
                "Notificaciones de job", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 9, Material.NOTE_BLOCK, "§eSonidos",
                "Toggle sonidos", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 10, Material.ARMOR_STAND, "§eTítulos",
                "Toggle títulos", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 11, Material.BOOK, "§eChat",
                "Toggle mensajes de chat", "§7Click para toggle");

        MenuFactory.addMenuSlot(menu, 26, Material.BARRIER, "§cCerrar",
                "Cerrar menú", "§7Click para cerrar");

        menuManager.registerMenu("notifications", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("toggle", "status");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("toggle")) {
            return List.of("arena", "clan", "auction", "bounty", "job", "sounds", "titles", "chat");
        }
        return List.of();
    }
}
