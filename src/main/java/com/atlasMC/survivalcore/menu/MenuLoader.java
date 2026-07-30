package com.atlasMC.survivalcore.menu;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuLoader {
    private final JavaPlugin plugin;
    private final MenuManager menuManager;

    public MenuLoader(JavaPlugin plugin, MenuManager menuManager) {
        this.plugin = plugin;
        this.menuManager = menuManager;
    }

    public void loadMenus() {
        File menusFolder = new File(plugin.getDataFolder(), "menus");
        if (!menusFolder.exists()) {
            menusFolder.mkdirs();
            createDefaultMenus(menusFolder);
        }

        File[] files = menusFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            loadMenuFromFile(file);
        }
    }

    private void loadMenuFromFile(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String menuName = file.getName().replace(".yml", "");

        String title = config.getString("title", "Menú");
        int size = config.getInt("size", 27);

        MenuData menuData = new MenuData(menuName, title, size);

        ConfigurationSection items = config.getConfigurationSection("items");
        if (items != null) {
            for (String slotStr : items.getKeys(false)) {
                try {
                    int slot = Integer.parseInt(slotStr);
                    ConfigurationSection itemSection = items.getConfigurationSection(slotStr);
                    if (itemSection == null) continue;

                    ItemStack item = loadItem(itemSection);
                    MenuAction action = loadAction(itemSection);

                    menuData.setItem(slot, item, action);
                } catch (NumberFormatException ignored) {}
            }
        }

        menuManager.registerMenu(menuName, menuData);
        plugin.getLogger().info("Menú cargado: " + menuName);
    }

    private ItemStack loadItem(ConfigurationSection section) {
        String materialName = section.getString("material", "PAPER");
        Material material = Material.valueOf(materialName.toUpperCase());
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (section.contains("name")) {
                meta.setDisplayName(section.getString("name"));
            }
            if (section.contains("lore")) {
                List<String> lore = section.getStringList("lore");
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }

        return item;
    }

    private MenuAction loadAction(ConfigurationSection section) {
        String type = section.getString("action-type", "NONE").toUpperCase();
        String value = section.getString("action-value", "");

        try {
            MenuAction.ActionType actionType = MenuAction.ActionType.valueOf(type);
            return new MenuAction(actionType, value);
        } catch (IllegalArgumentException e) {
            return MenuAction.none();
        }
    }

    private void createDefaultMenus(File menusFolder) {
        createJobMenu(menusFolder);
        createShopMenu(menusFolder);
        createEditorMainMenu(menusFolder);
        createEditorSlotMenu(menusFolder);
    }

    private void createJobMenu(File menusFolder) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "§6Trabajos");
        config.set("size", 27);

        config.set("items.0.material", "DIAMOND_PICKAXE");
        config.set("items.0.name", "§eMINER");
        config.set("items.0.action-type", "COMMAND");
        config.set("items.0.action-value", "job set MINER");

        try {
            config.save(new File(menusFolder, "jobs.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createShopMenu(File menusFolder) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "§6Tienda");
        config.set("size", 27);

        config.set("items.0.material", "DIAMOND");
        config.set("items.0.name", "§bDiamante");
        config.set("items.0.action-type", "MESSAGE");
        config.set("items.0.action-value", "§aDiamante: $100");

        try {
            config.save(new File(menusFolder, "shop.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEditorMainMenu(File menusFolder) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "§6Editor - Principal");
        config.set("size", 27);

        config.set("items.0.material", "BOOK");
        config.set("items.0.name", "§eEditar Items");
        config.set("items.0.action-type", "NONE");

        try {
            config.save(new File(menusFolder, "editor-main.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEditorSlotMenu(File menusFolder) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "§6Editor - Editar Slot");
        config.set("size", 27);

        config.set("items.0.material", "PAPER");
        config.set("items.0.name", "§eNuevo Item");
        config.set("items.0.action-type", "NONE");

        try {
            config.save(new File(menusFolder, "editor-slot.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
