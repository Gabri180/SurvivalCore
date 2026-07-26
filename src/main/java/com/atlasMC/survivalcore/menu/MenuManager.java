package com.atlasMC.survivalcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private final JavaPlugin plugin;
    private final Map<String, MenuData> menus = new HashMap<>();
    private final MenuClickListener clickListener;

    public MenuManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.clickListener = new MenuClickListener(this);
        plugin.getServer().getPluginManager().registerEvents(clickListener, plugin);
    }

    public void registerMenu(String name, MenuData menuData) {
        menus.put(name.toLowerCase(), menuData);
    }

    public void openMenu(Player player, String menuName) {
        MenuData menuData = menus.get(menuName.toLowerCase());
        if (menuData == null) {
            player.sendMessage("§cMenú no encontrado: " + menuName);
            return;
        }

        Inventory inv = Bukkit.createInventory(null, menuData.getSize(), menuData.getTitle());
        for (int i = 0; i < menuData.getSize(); i++) {
            if (menuData.getItem(i) != null) {
                inv.setItem(i, menuData.getItem(i));
            }
        }

        clickListener.registerInventory(inv, menuData);
        player.openInventory(inv);
    }

    public MenuData getMenu(String menuName) {
        return menus.get(menuName.toLowerCase());
    }

    public Map<String, MenuData> getAllMenus() {
        return new HashMap<>(menus);
    }

    public void removeMenu(String menuName) {
        menus.remove(menuName.toLowerCase());
    }

    public MenuClickListener getClickListener() {
        return clickListener;
    }
}
