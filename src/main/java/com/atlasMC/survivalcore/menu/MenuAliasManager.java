package com.atlasMC.survivalcore.menu;

import com.atlasMC.survivalcore.SurvivalCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MenuAliasManager {

    private final MenuManager menuManager;
    private final Plugin plugin;
    private final Map<String, String> aliases = new HashMap<>();
    private final Map<String, String> permissions = new HashMap<>();
    private final File aliasesFile;

    public MenuAliasManager(MenuManager menuManager, Plugin plugin) {
        this.menuManager = menuManager;
        this.plugin = plugin;
        this.aliasesFile = new File(plugin.getDataFolder(), "menu-aliases.yml");
        loadAliases();
    }

    /**
     * Registra un alias para un menú (puede estar en edición o guardado).
     * Si está en edición, el alias funcionará una vez que se guarde el menú.
     */
    public void registerAlias(String commandName, String menuId) {
        // Permitir crear alias para menús guardados o en edición
        // Se validará cuando el menú se intente abrir
        aliases.put(commandName.toLowerCase(), menuId);
        registerCommand(commandName, menuId);
        saveAliases();
    }

    public void removeAlias(String commandName) {
        aliases.remove(commandName.toLowerCase());
        saveAliases();
    }

    public String getMenuForAlias(String commandName) {
        return aliases.get(commandName.toLowerCase());
    }

    public Map<String, String> getAllAliases() {
        return new HashMap<>(aliases);
    }

    public void setPermission(String menuId, String permission) {
        permissions.put(menuId.toLowerCase(), permission);
        saveAliases();
    }

    public void clearPermission(String menuId) {
        permissions.remove(menuId.toLowerCase());
        saveAliases();
    }

    public String getPermission(String menuId) {
        return permissions.get(menuId.toLowerCase());
    }

    public Map<String, String> getAllPermissions() {
        return new HashMap<>(permissions);
    }

    private void registerCommand(String commandName, String menuId) {
        try {
            CommandMap commandMap = Bukkit.getCommandMap();
            MenuOpenCommand cmd = new MenuOpenCommand(commandName, menuManager, menuId, plugin);
            commandMap.register(plugin.getName(), cmd);
        } catch (Exception e) {
            plugin.getLogger().warning("Error registering command alias: " + commandName);
            e.printStackTrace();
        }
    }

    public void loadAliases() {
        if (!aliasesFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(aliasesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("PERM:")) {
                    String[] parts = line.substring(5).split("=");
                    if (parts.length == 2) {
                        String menuId = parts[0].trim();
                        String permission = parts[1].trim();
                        permissions.put(menuId.toLowerCase(), permission);
                    }
                } else {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String commandName = parts[0].trim();
                        String menuId = parts[1].trim();
                        if (menuManager.getMenu(menuId) != null) {
                            aliases.put(commandName.toLowerCase(), menuId);
                            registerCommand(commandName, menuId);
                        }
                    }
                }
            }
            plugin.getLogger().info("Cargados " + aliases.size() + " aliases y " + permissions.size() + " permisos");
        } catch (IOException e) {
            plugin.getLogger().warning("Error loading menu aliases");
            e.printStackTrace();
        }
    }

    public void saveAliases() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(aliasesFile))) {
                writer.println("# Aliases de menús personalizados");
                writer.println("# Formato alias: comando:menuId");
                writer.println("# Formato permisos: PERM:menuId=permiso");
                writer.println();

                writer.println("# === ALIASES ===");
                for (Map.Entry<String, String> entry : aliases.entrySet()) {
                    writer.println(entry.getKey() + ":" + entry.getValue());
                }

                if (!permissions.isEmpty()) {
                    writer.println();
                    writer.println("# === PERMISOS ===");
                    for (Map.Entry<String, String> entry : permissions.entrySet()) {
                        writer.println("PERM:" + entry.getKey() + "=" + entry.getValue());
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Error saving menu aliases");
            e.printStackTrace();
        }
    }

    public class MenuOpenCommand extends Command {
        private final MenuManager menuManager;
        private final String menuId;

        public MenuOpenCommand(String name, MenuManager menuManager, String menuId, Plugin plugin) {
            super(name);
            this.menuManager = menuManager;
            this.menuId = menuId;
            setDescription("Abre el menú personalizado: " + menuId);
            setUsage("/" + name);
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Este comando solo puede ser usado por jugadores.");
                return true;
            }

            // Verificar permisos
            String permission = MenuAliasManager.this.getPermission(menuId);
            if (permission != null && !permission.isEmpty()) {
                if (!player.hasPermission(permission)) {
                    player.sendMessage("§cNo tienes permiso para acceder a este menú.");
                    return true;
                }
            }

            try {
                menuManager.openMenu(player, menuId);
            } catch (Exception e) {
                player.sendMessage("§cError al abrir el menú: " + menuId);
            }

            return true;
        }
    }
}
