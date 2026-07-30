package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.SurvivalCorePlugin;
import com.atlasMC.survivalcore.menu.MenuEditorManager;
import com.atlasMC.survivalcore.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements CommandExecutor, TabExecutor {

    private final SurvivalCorePlugin plugin;

    public AdminCommand(SurvivalCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("survivalcore.admin")) {
            sender.sendMessage("§cNo tienes permiso");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> reloadPlugin(sender);
            case "gui" -> handleGuiCommand(sender, args);
            case "backup" -> backupDatabase(sender);
            case "help" -> showHelp(sender);
            default -> showHelp(sender);
        }

        return true;
    }

    private void reloadPlugin(CommandSender sender) {
        try {
            plugin.onDisable();
            plugin.onEnable();
            sender.sendMessage("§a✓ Plugin recargado correctamente");
        } catch (Exception e) {
            sender.sendMessage("§c✗ Error al recargar: " + e.getMessage());
        }
    }

    private void backupDatabase(CommandSender sender) {
        sender.sendMessage("§e⏳ Ejecutando backup de base de datos...");
        plugin.getBackupScheduler().performBackupNow();
        sender.sendMessage("§a✓ Backup iniciado (se completará en segundo plano)");
    }

    private void handleGuiCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo jugadores");
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("§cUso: /sc gui <edit|set>");
            return;
        }

        MenuManager menuManager = plugin.getMenuManager();
        MenuEditorManager editorManager = plugin.getMenuEditorManager();

        switch (args[1].toLowerCase()) {
            case "edit" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /sc gui edit <nombre_del_menu>");
                } else {
                    editorManager.startEditSession(player, args[2]);
                }
            }
            case "set" -> {
                if (args.length < 5) {
                    player.sendMessage("§cUso: /sc gui set command <nombre_del_menu> <item_id> <comando>");
                    return;
                }

                if (args[2].equalsIgnoreCase("command")) {
                    String menuName = args[3];
                    try {
                        int itemId = Integer.parseInt(args[4]);
                        String command = String.join(" ", java.util.Arrays.copyOfRange(args, 5, args.length));

                        editorManager.getSession(player).setSelectedSlot(itemId);
                        editorManager.setAction(player, "COMMAND", command);

                        player.sendMessage("§a✓ Acción establecida");
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cItem ID inválido");
                    }
                }
            }
            default -> player.sendMessage("§cUso: /sc gui <edit|set>");
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6=== Comandos de Admin (SurvivalCore) ===");
        sender.sendMessage("§e/sc reload §7- Recargar el plugin");
        sender.sendMessage("§e/sc backup §7- Ejecutar backup de BD");
        sender.sendMessage("§e/sc gui edit <menu> §7- Editar menú");
        sender.sendMessage("§e/sc gui set command <menu> <id> <cmd> §7- Establecer comando rápido");
        sender.sendMessage("§e/sc help §7- Ver esta ayuda");
        sender.sendMessage("");
        sender.sendMessage("§6Comandos disponibles (alias /sc):");
        sender.sendMessage("§e/job, /jobs §7- Menú de trabajos");
        sender.sendMessage("§e/stats §7- Menú de estadísticas");
        sender.sendMessage("§e/arena §7- Menú de arenas");
        sender.sendMessage("§e/clan §7- Menú de clanes");
        sender.sendMessage("§e/auction, /ah §7- Casa de subastas");
        sender.sendMessage("§e/bounty §7- Recompensas");
        sender.sendMessage("§e/menu §7- Editor de menús");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("backup");
            completions.add("gui");
            completions.add("help");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("gui")) {
            completions.add("edit");
            completions.add("set");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("gui") && args[1].equalsIgnoreCase("edit")) {
            completions.addAll(SurvivalCorePlugin.getInstance().getMenuManager().getAllMenus().keySet());
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("gui") && args[1].equalsIgnoreCase("set")) {
            if (args.length == 3) {
                completions.add("command");
            }
        }

        return completions;
    }
}
