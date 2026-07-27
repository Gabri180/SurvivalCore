package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.menu.CustomMenuBuilder;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.menu.MenuAliasManager;
import com.atlasMC.survivalcore.menu.MenuEditorUI;
import com.atlasMC.survivalcore.menu.MenuYamlWriter;
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
    private final MenuAliasManager aliasManager;
    private final MenuYamlWriter yamlWriter;
    private final Map<String, MenuBuilder> builders = new HashMap<>();

    public CustomMenuCommand(MenuManager menuManager, MenuAliasManager aliasManager, MenuYamlWriter yamlWriter) {
        this.menuManager = menuManager;
        this.aliasManager = aliasManager;
        this.yamlWriter = yamlWriter;
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
            case "command" -> {
                if (args.length < 4) {
                    player.sendMessage("§cUso: /custommenu command <menuId> set <comando>");
                } else {
                    commandAlias(player, args[1], args[2], args[3]);
                }
            }
            case "unalias" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /custommenu unalias <comando>");
                } else {
                    removeAlias(player, args[1]);
                }
            }
            case "aliases" -> listAliases(player);
            case "permission" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /custommenu permission <menuId> set <permiso>");
                    player.sendMessage("§cUso: /custommenu permission <menuId> clear");
                } else {
                    handlePermission(player, args[1], args[2], args);
                }
            }
            case "editor" -> {
                MenuEditorUI editorUI = new MenuEditorUI(menuManager, player);
                editorUI.openMenuList();
            }
            case "itemaction" -> {
                if (args.length < 4) {
                    player.sendMessage("§cUso: /custommenu itemaction <menuId> <slot> <acción> [valor]");
                } else {
                    setItemAction(player, args[1], args[2], args[3], args);
                }
            }
            default -> showHelp(player);
        }
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("§6═══ Custom Menu Builder ═══");
        player.sendMessage("§7§m                                   ");
        player.sendMessage("§e/custommenu create <id> §7- Crear nuevo menú");
        player.sendMessage("§e/custommenu item <id> <slot> <material> §7- Agregar item");
        player.sendMessage("§e/custommenu title <id> <título> §7- Cambiar título");
        player.sendMessage("§e/custommenu size <id> <filas> §7- Tamaño (1-6)");
        player.sendMessage("§e/custommenu bgcolor <id> <material> §7- Color fondo");
        player.sendMessage("§e/custommenu save <id> §7- Guardar menú");
        player.sendMessage("§e/custommenu cancel <id> §7- Cancelar edición");
        player.sendMessage("§e/custommenu open <id> §7- Abrir menú");
        player.sendMessage("§e/custommenu editor §7- Abrir editor visual de menús");
        player.sendMessage("§7§m                                   ");
        player.sendMessage("§e/custommenu command <id> set <cmd> §7- Crear alias");
        player.sendMessage("§e/custommenu unalias <cmd> §7- Eliminar alias");
        player.sendMessage("§e/custommenu permission <id> set <perm> §7- Setear permiso");
        player.sendMessage("§e/custommenu permission <id> clear §7- Remover permiso");
        player.sendMessage("§e/custommenu aliases §7- Listar aliases");
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

    private MenuBuilder getOrCreateBuilder(Player player, String menuId) {
        MenuBuilder builder = builders.get(menuId);
        if (builder == null) {
            // Si no existe en edición pero existe guardado, cargarlo
            com.atlasMC.survivalcore.menu.MenuData menuData = menuManager.getMenu(menuId);
            if (menuData != null) {
                builder = new MenuBuilder(menuId);
                builders.put(menuId, builder);
                player.sendMessage("§7Menú guardado cargado en edición.");
                return builder;
            }
            player.sendMessage("§cMenú no encontrado. Crea uno con: /custommenu create " + menuId);
            return null;
        }
        return builder;
    }

    private void autoSaveMenu(String menuId) {
        MenuBuilder builder = builders.get(menuId);
        if (builder != null) {
            com.atlasMC.survivalcore.menu.MenuData menu = builder.customBuilder.build();
            menuManager.registerMenu(menuId, menu);
            yamlWriter.saveMenu(menu, menuId);
        }
    }

    private void addItem(Player player, String menuId, String slotStr, String material, String[] args) {
        MenuBuilder builder = getOrCreateBuilder(player, menuId);
        if (builder == null) {
            return;
        }

        try {
            int slot = Integer.parseInt(slotStr);
            if (slot < 0 || slot >= builder.customBuilder.rows * 9) {
                player.sendMessage("§cSlot debe estar entre 0 y " + (builder.customBuilder.rows * 9 - 1));
                return;
            }

            Material mat = Material.valueOf(material.toUpperCase());

            String displayName = args.length > 4 ? String.join(" ", java.util.Arrays.copyOfRange(args, 4, args.length)) : "§bItem";

            builder.customBuilder.addItem(slot, mat, displayName, com.atlasMC.survivalcore.menu.MenuAction.none());
            player.sendMessage(String.format("§a✓ Item agregado en slot %d", slot));
            autoSaveMenu(menuId);
        } catch (NumberFormatException e) {
            player.sendMessage("§cSlot debe ser un número válido");
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cMaterial no válido: " + material);
        }
    }

    private void setTitle(Player player, String menuId, String title) {
        MenuBuilder builder = getOrCreateBuilder(player, menuId);
        if (builder == null) {
            return;
        }

        builder.customBuilder.title(title);
        player.sendMessage(String.format("§a✓ Título actualizado: %s", title));
        autoSaveMenu(menuId);
    }

    private void setSize(Player player, String menuId, String rowsStr) {
        MenuBuilder builder = getOrCreateBuilder(player, menuId);
        if (builder == null) {
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
            autoSaveMenu(menuId);
        } catch (NumberFormatException e) {
            player.sendMessage("§cDebe ser un número entre 1 y 6");
        }
    }

    private void setBackgroundColor(Player player, String menuId, String material) {
        MenuBuilder builder = getOrCreateBuilder(player, menuId);
        if (builder == null) {
            return;
        }

        try {
            Material.valueOf(material.toUpperCase());
            builder.customBuilder.backgroundColor(material.toUpperCase());
            player.sendMessage(String.format("§a✓ Color de fondo actualizado: %s", material));
            autoSaveMenu(menuId);
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
        yamlWriter.saveMenu(menu, menuId);
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
        // Primero intentar abrir menú guardado
        com.atlasMC.survivalcore.menu.MenuData savedMenu = menuManager.getMenu(menuId);
        if (savedMenu != null) {
            menuManager.openMenu(player, menuId);
            return;
        }

        // Si no existe guardado, intentar desde builders (en edición)
        MenuBuilder builder = builders.get(menuId);
        if (builder != null) {
            com.atlasMC.survivalcore.menu.MenuData menu = builder.customBuilder.build();
            String tempMenuId = "_temp_" + menuId + "_" + System.currentTimeMillis();
            menuManager.registerMenu(tempMenuId, menu);
            menuManager.openMenu(player, tempMenuId);
            player.sendMessage("§7Abriendo menú en edición (no guardado aún)");
            return;
        }

        player.sendMessage("§cMenú no encontrado: " + menuId);
    }

    private void listMenus(Player player) {
        player.sendMessage("§6=== Menús Disponibles ===");

        // Menús guardados
        Map<String, com.atlasMC.survivalcore.menu.MenuData> savedMenus = menuManager.getAllMenus();
        if (!savedMenus.isEmpty()) {
            player.sendMessage("§a📦 Menús Guardados:");
            savedMenus.forEach((id, menu) ->
                player.sendMessage(String.format("§e  • %s §7(%d items)", id, menu.getSize()))
            );
        }

        // Menús en edición
        if (!builders.isEmpty()) {
            player.sendMessage("§e✏️ Menús en Edición:");
            builders.forEach((id, builder) ->
                player.sendMessage(String.format("§e  • %s §7(%d filas)", id, builder.customBuilder.rows))
            );
        }

        if (savedMenus.isEmpty() && builders.isEmpty()) {
            player.sendMessage("§7No hay menús creados.");
        }
    }

    private void commandAlias(Player player, String menuId, String action, String commandName) {
        if (!action.equalsIgnoreCase("set")) {
            player.sendMessage("§cUso: /custommenu command <menuId> set <comando>");
            return;
        }

        if (menuManager.getMenu(menuId) == null && !builders.containsKey(menuId)) {
            player.sendMessage("§cMenú no encontrado: " + menuId);
            player.sendMessage("§7Primero debes crear el menú con: §f/custommenu create " + menuId);
            return;
        }

        if (!isValidCommandName(commandName)) {
            player.sendMessage("§cNombre de comando inválido. Solo letras, números y guiones.");
            return;
        }

        try {
            aliasManager.registerAlias(commandName, menuId);
            player.sendMessage(String.format("§a✓ Alias creado: §f/%s §7→ Abre §f%s", commandName, menuId));
            player.sendMessage("§7Los jugadores ahora pueden usar §f/" + commandName + " §7para abrir el menú");
        } catch (IllegalArgumentException e) {
            player.sendMessage("§c" + e.getMessage());
        }
    }

    private void removeAlias(Player player, String commandName) {
        String menuId = aliasManager.getMenuForAlias(commandName);
        if (menuId == null) {
            player.sendMessage("§cAlias no encontrado: " + commandName);
            return;
        }

        aliasManager.removeAlias(commandName);
        player.sendMessage(String.format("§c✗ Alias eliminado: §f/%s", commandName));
    }

    private void listAliases(Player player) {
        Map<String, String> aliases = aliasManager.getAllAliases();
        if (aliases.isEmpty()) {
            player.sendMessage("§7No hay aliases configurados.");
            return;
        }

        player.sendMessage("§6=== Aliases de Menús ===");
        aliases.forEach((cmd, menuId) ->
            player.sendMessage(String.format("§e• /%-15s §7→ §f%s", cmd, menuId))
        );
    }

    private boolean isValidCommandName(String name) {
        return name.matches("^[a-zA-Z0-9_-]+$") && name.length() >= 2 && name.length() <= 20;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("create", "item", "itemaction", "title", "size", "bgcolor", "save", "cancel", "open",
                          "command", "unalias", "aliases", "list", "permission", "editor");
        }
        if (args.length == 2) {
            List<String> menuIds = new ArrayList<>(builders.keySet());
            // Agregar menús guardados
            menuIds.addAll(menuManager.getAllMenus().keySet());
            return menuIds;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("command")) {
                return List.of("set");
            }
            if (args[0].equalsIgnoreCase("permission")) {
                return List.of("set", "clear");
            }
            if (args[0].equalsIgnoreCase("itemaction")) {
                // Tab-complete para slot (mostrar números 0-26)
                List<String> slots = new ArrayList<>();
                for (int i = 0; i < 27; i++) {
                    slots.add(String.valueOf(i));
                }
                return slots;
            }
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("itemaction")) {
            return List.of("COMMAND", "MESSAGE", "OPEN_MENU", "CLOSE", "NONE");
        }
        return List.of();
    }

    private void handlePermission(Player player, String menuId, String action, String[] args) {
        if (action.equalsIgnoreCase("clear")) {
            clearPermission(player, menuId);
        } else if (action.equalsIgnoreCase("set")) {
            if (args.length < 4) {
                player.sendMessage("§cUso: /custommenu permission <menuId> set <permiso>");
                return;
            }
            setPermission(player, menuId, args[3]);
        } else {
            player.sendMessage("§cAcción desconocida: " + action);
        }
    }

    private void setPermission(Player player, String menuId, String permission) {
        if (menuManager.getMenu(menuId) == null && !builders.containsKey(menuId)) {
            player.sendMessage("§cMenú no encontrado: " + menuId);
            return;
        }

        if (permission == null || permission.trim().isEmpty()) {
            player.sendMessage("§cPermiso no puede estar vacío.");
            return;
        }

        aliasManager.setPermission(menuId, permission.trim());
        player.sendMessage(String.format("§a✓ Permiso asignado: §f%s", permission.trim()));
        player.sendMessage(String.format("§7Menú §f%s §7requiere ahora §f%s", menuId, permission.trim()));
    }

    private void clearPermission(Player player, String menuId) {
        aliasManager.clearPermission(menuId);
        player.sendMessage(String.format("§a✓ Permiso removido del menú §f%s", menuId));
        player.sendMessage("§7El menú ahora está disponible para todos");
    }

    private void setItemAction(Player player, String menuId, String slotStr, String actionType, String[] args) {
        MenuBuilder builder = getOrCreateBuilder(player, menuId);
        if (builder == null) {
            return;
        }

        try {
            int slot = Integer.parseInt(slotStr);
            if (slot < 0 || slot >= builder.customBuilder.rows * 9) {
                player.sendMessage("§cSlot debe estar entre 0 y " + (builder.customBuilder.rows * 9 - 1));
                return;
            }

            String actionValue = args.length > 4 ? String.join(" ", java.util.Arrays.copyOfRange(args, 4, args.length)) : "";

            // Validar tipo de acción
            switch (actionType.toUpperCase()) {
                case "COMMAND", "MESSAGE", "OPEN_MENU", "CLOSE", "NONE" -> {
                    player.sendMessage(String.format("§a✓ Acción configurada: §f%s", actionType.toUpperCase()));
                    if (!actionValue.isEmpty()) {
                        player.sendMessage(String.format("§7Valor: §f%s", actionValue));
                    }
                    player.sendMessage("§7Para aplicar, recrea el item con: §f/custommenu item " + menuId + " " + slot + " <material>");
                }
                default -> {
                    player.sendMessage("§cTipo de acción no válido: " + actionType);
                    player.sendMessage("§7Válidos: COMMAND, MESSAGE, OPEN_MENU, CLOSE, NONE");
                }
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§cSlot debe ser un número válido");
        }
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
