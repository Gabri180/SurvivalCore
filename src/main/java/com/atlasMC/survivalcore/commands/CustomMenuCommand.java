package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.menu.CustomMenuBuilder;
import com.atlasMC.survivalcore.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMenuCommand implements CommandExecutor, TabExecutor {

    private final MenuManager menuManager;
    private final Map<String, MenuBuilder> builders = new HashMap<>();

    public CustomMenuCommand(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (!player.hasPermission("survivalcore.admin")) {
            player.sendMessage("§cNo tienes permiso para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /custommenu create <id>");
                } else {
                    createMenu(player, args[1]);
                }
            }
            case "item" -> {
                if (args.length < 4) {
                    player.sendMessage("§cUso: /custommenu item <menuId> <slot> <material>");
                } else {
                    addItem(player, args[1], args[2], args[3], args);
                }
            }
            case "title" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /custommenu title <menuId> <título>");
                } else {
                    setTitle(player, args[1], String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)));
                }
            }
            case "size" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /custommenu size <menuId> <filas 1-6>");
                } else {
                    setSize(player, args[1], args[2]);
                }
            }
            case "bgcolor" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /custommenu bgcolor <menuId> <material>");
                } else {
                    setBackgroundColor(player, args[1], args[2]);
                }
            }
            case "save" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /custommenu save <menuId>");
                } else {
                    saveMenu(player, args[1]);
                }
            }
            case "cancel" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /custommenu cancel <menuId>");
                } else {
                    cancelMenu(player, args[1]);
                }
            }
            case "open" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /custommenu open <menuId>");
                } else {
                    openMenu(player, args[1]);
                }
            }
            case "list" -> listMenus(player);
            default -> showHelp(player);
        }
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("§6═══ Custom Menu Builder ═══");
        player.sendMessage("§e/custommenu create <id> §7- Crear nuevo menú");
        player.sendMessage("§e/custommenu item <id> <slot> <material> §7- Agregar item");
        player.sendMessage("§e/custommenu title <id> <título> §7- Cambiar título");
        player.sendMessage("§e/custommenu size <id> <filas> §7- Tamaño (1-6)");
        player.sendMessage("§e/custommenu bgcolor <id> <material> §7- Color fondo");
        player.sendMessage("§e/custommenu save <id> §7- Guardar menú");
        player.sendMessage("§e/custommenu cancel <id> §7- Cancelar edición");
        player.sendMessage("§e/custommenu open <id> §7- Abrir menú");
        player.sendMessage("§e/custommenu list §7- Listar menús");
    }

    private void createMenu(Player player, String menuId) {
        if (builders.containsKey(menuId)) {
            player.sendMessage("§cYa hay un menú en edición con ese ID.");
            return;
        }

        MenuBuilder builder = new MenuBuilder(menuId);
        builders.put(menuId, builder);
        player.sendMessage(String.format("§a✓ Menú creado: %s\n§7Usa los comandos para personalizarlo", menuId));
    }

    private void addItem(Player player, String menuId, String slotStr, String material, String[] args) {
        MenuBuilder builder = builders.get(menuId);
        if (builder == null) {
            player.sendMessage("§cMenú no encontrado. Crea uno con: /custommenu create " + menuId);
            return;
        }

        try {
            int slot = Integer.parseInt(slotStr);
            Material mat = Material.valueOf(material.toUpperCase());

            String displayName = args.length > 4 ? String.join(" ", java.util.Arrays.copyOfRange(args, 4, args.length)) : "§bItem";

            builder.customBuilder.addItem(slot, mat, displayName, "COMMAND", "");
            player.sendMessage(String.format("§a✓ Item agregado en slot %d", slot));
        } catch (NumberFormatException e) {
            player.sendMessage("§cSlot debe ser un número válido");
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cMaterial no válido: " + material);
        }
    }

    private void setTitle(Player player, String menuId, String title) {
        MenuBuilder builder = builders.get(menuId);
        if (builder == null) {
            player.sendMessage("§cMenú no encontrado.");
            return;
        }

        builder.customBuilder.title(title);
        player.sendMessage(String.format("§a✓ Título actualizado: %s", title));
    }

    private void setSize(Player player, String menuId, String rowsStr) {
        MenuBuilder builder = builders.get(menuId);
        if (builder == null) {
            player.sendMessage("§cMenú no encontrado.");
            return;
        }

        try {
            int rows = Integer.parseInt(rowsStr);
            if (rows < 1 || rows > 6) {
                player.sendMessage("§cLas filas deben estar entre 1 y 6");
                return;
            }
            builder.customBuilder.rows(rows);
            player.sendMessage(String.format("§a✓ Tamaño actualizado: %d filas", rows));
        } catch (NumberFormatException e) {
            player.sendMessage("§cDebe ser un número entre 1 y 6");
        }
    }

    private void setBackgroundColor(Player player, String menuId, String material) {
        MenuBuilder builder = builders.get(menuId);
        if (builder == null) {
            player.sendMessage("§cMenú no encontrado.");
            return;
        }

        try {
            Material.valueOf(material.toUpperCase());
            builder.customBuilder.backgroundColor(material.toUpperCase());
            player.sendMessage(String.format("§a✓ Color de fondo actualizado: %s", material));
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cMaterial no válido: " + material);
        }
    }

    private void saveMenu(Player player, String menuId) {
        MenuBuilder builder = builders.remove(menuId);
        if (builder == null) {
            player.sendMessage("§cMenú no encontrado en edición.");
            return;
        }

        com.atlasMC.survivalcore.menu.MenuData menu = builder.customBuilder.build();
        menuManager.registerMenu(menuId, menu);
        player.sendMessage(String.format("§a✓ Menú guardado: %s", menuId));
    }

    private void cancelMenu(Player player, String menuId) {
        if (builders.remove(menuId) != null) {
            player.sendMessage(String.format("§c✗ Edición cancelada: %s", menuId));
        } else {
            player.sendMessage("§cNo hay menú en edición con ese ID.");
        }
    }

    private void openMenu(Player player, String menuId) {
        menuManager.openMenu(player, menuId);
    }

    private void listMenus(Player player) {
        player.sendMessage("§6=== Menús Disponibles ===");
        if (builders.isEmpty()) {
            player.sendMessage("§7No hay menús en edición.");
        } else {
            builders.forEach((id, builder) ->
                player.sendMessage(String.format("§e• %s §7(%d filas)", id, builder.customBuilder.rows))
            );
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("create", "item", "title", "size", "bgcolor", "save", "cancel", "open", "list");
        }
        if (args.length == 2) {
            List<String> menuIds = new ArrayList<>(builders.keySet());
            menuIds.addAll(List.of("myMenu", "shopMenu", "rankingsMenu"));
            return menuIds;
        }
        return List.of();
    }

    private static class MenuBuilder {
        String menuId;
        CustomMenuBuilder customBuilder;
        int rows = 3;

        MenuBuilder(String menuId) {
            this.menuId = menuId;
            this.customBuilder = new CustomMenuBuilder(menuId);
        }
    }
}
