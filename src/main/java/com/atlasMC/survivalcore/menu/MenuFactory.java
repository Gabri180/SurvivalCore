package com.atlasMC.survivalcore.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuFactory {

    public static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                List<String> loreList = new ArrayList<>();
                for (String line : lore) {
                    loreList.add(line);
                }
                meta.setLore(loreList);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static void addMenuSlot(MenuData menu, int slot, Material material, String name,
                                   MenuAction action, String... lore) {
        ItemStack item = createItem(material, name, lore);
        menu.setItem(slot, item, action);
    }

    public static void addMenuSlot(MenuData menu, int slot, Material material, String name,
                                   String actionType, String value, String... lore) {
        ItemStack item = createItem(material, name, lore);
        MenuAction action = createAction(actionType, value);
        menu.setItem(slot, item, action);
    }

    public static MenuAction createAction(String type, String value) {
        return switch (type.toUpperCase()) {
            case "COMMAND" -> MenuAction.command(value);
            case "OPEN_MENU" -> MenuAction.openMenu(value);
            case "MESSAGE" -> MenuAction.message(value);
            case "CLOSE" -> MenuAction.close();
            default -> MenuAction.none();
        };
    }

    public static MenuData createMenu(String name, String title, int size) {
        return new MenuData(name, title, size);
    }
}
