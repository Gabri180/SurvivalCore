package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.SurvivalCorePlugin;
import com.atlasMC.survivalcore.menu.MenuEditorManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand implements CommandExecutor, TabExecutor {

    private final MenuEditorManager editorManager;

    public MenuCommand(MenuEditorManager editorManager) {
        this.editorManager = editorManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo jugadores");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("survivalcore.admin.menu")) {
            player.sendMessage("§cNo tienes permiso");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "edit" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /menu edit <nombre>");
                } else {
                    editorManager.startEditSession(player, args[1]);
                }
            }
            case "slot" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /menu slot <número>");
                } else {
                    try {
                        int slot = Integer.parseInt(args[1]);
                        editorManager.selectSlot(player, slot);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cSlot inválido");
                    }
                }
            }
            case "material" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /menu material <material>");
                } else {
                    editorManager.setMaterial(player, args[1]);
                }
            }
            case "name" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /menu name <nombre>");
                } else {
                    String name = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                    editorManager.setItemName(player, name);
                }
            }
            case "action" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /menu action <tipo> <valor>");
                } else {
                    String type = args[1];
                    String value = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                    editorManager.setAction(player, type, value);
                }
            }
            case "save" -> editorManager.saveMenu(player);
            case "cancel" -> editorManager.cancelEdit(player);
            case "help" -> showHelp(player);
            default -> showHelp(player);
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("§6=== Comandos de Menú ===");
        player.sendMessage("§e/menu edit <nombre> §7- Editar un menú");
        player.sendMessage("§e/menu slot <número> §7- Seleccionar slot (0-53)");
        player.sendMessage("§e/menu material <material> §7- Cambiar material del item");
        player.sendMessage("§e/menu name <nombre> §7- Cambiar nombre del item (color: §)");
        player.sendMessage("§e/menu action <tipo> <valor> §7- Establecer acción");
        player.sendMessage("§e/menu save §7- Guardar cambios");
        player.sendMessage("§e/menu cancel §7- Cancelar edición");
        player.sendMessage("");
        player.sendMessage("§6Tipos de acción:");
        player.sendMessage("§e  COMMAND <comando> §7- Ejecutar comando");
        player.sendMessage("§e  OPEN_MENU <menú> §7- Abrir otro menú");
        player.sendMessage("§e  MESSAGE <mensaje> §7- Mostrar mensaje");
        player.sendMessage("§e  CLOSE §7- Cerrar inventario");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("edit");
            completions.add("slot");
            completions.add("material");
            completions.add("name");
            completions.add("action");
            completions.add("save");
            completions.add("cancel");
            completions.add("help");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("edit")) {
                completions.addAll(SurvivalCorePlugin.getInstance().getMenuManager().getAllMenus().keySet());
            } else if (args[0].equalsIgnoreCase("material")) {
                completions.add("DIAMOND");
                completions.add("EMERALD");
                completions.add("GOLD_INGOT");
                completions.add("DIAMOND_SWORD");
            } else if (args[0].equalsIgnoreCase("action")) {
                completions.add("COMMAND");
                completions.add("OPEN_MENU");
                completions.add("MESSAGE");
                completions.add("CLOSE");
                completions.add("NONE");
            }
        }

        return completions;
    }
}
