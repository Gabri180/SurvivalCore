package com.atlasMC.survivalcore.menu;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MenuYamlWriter {
    private final JavaPlugin plugin;

    public MenuYamlWriter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void saveMenu(MenuData menuData, String fileName) {
        // No guardar menús temporales del editor
        if (fileName.startsWith("_")) {
            return;
        }

        File menusFolder = new File(plugin.getDataFolder(), "menus");
        if (!menusFolder.exists()) {
            menusFolder.mkdirs();
        }

        YamlConfiguration config = new YamlConfiguration();
        config.set("title", menuData.getTitle());
        config.set("size", menuData.getSize());

        for (int i = 0; i < menuData.getSize(); i++) {
            ItemStack item = menuData.getItem(i);
            if (item == null) continue;

            String slotPath = "items." + i;
            config.set(slotPath + ".material", item.getType().name());

            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                config.set(slotPath + ".name", meta.getDisplayName());
            }

            if (meta != null && meta.hasLore()) {
                config.set(slotPath + ".lore", meta.getLore());
            }

            MenuAction action = menuData.getAction(i);
            if (action != null && action.getType() != MenuAction.ActionType.NONE) {
                config.set(slotPath + ".action-type", action.getType().name());
                config.set(slotPath + ".action-value", action.getValue());
            }
        }

        try {
            File file = new File(menusFolder, fileName + ".yml");
            config.save(file);
        } catch (Exception e) {
            plugin.getLogger().severe("Error al guardar menú: " + e.getMessage());
        }
    }
}
