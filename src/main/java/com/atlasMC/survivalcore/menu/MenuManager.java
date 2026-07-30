package com.atlasMC.survivalcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {
    private final JavaPlugin plugin;
    private final Map<String, MenuData> menus = new HashMap<>();
    private final Map<UUID, String> playerMenus = new HashMap<>();
    private final MenuClickListener clickListener;

    public MenuManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.clickListener = new MenuClickListener(this, this);
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
                inv.setItem(i, menuData.getItem(i).clone());
            }
        }

        playerMenus.put(player.getUniqueId(), menuName.toLowerCase());
        player.openInventory(inv);
    }

    public MenuData getMenu(String menuName) {
        return menus.get(menuName.toLowerCase());
    }

    public MenuData getPlayerMenu(UUID playerUuid) {
        String menuName = playerMenus.get(playerUuid);
        return menuName != null ? menus.get(menuName) : null;
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

    public void openMissionMenu(Player player) {
        CustomMenuBuilder builder = new CustomMenuBuilder("_missions_" + player.getUniqueId())
            .title("§6Misiones")
            .rows(6)
            .backgroundColor("DARK_GRAY_STAINED_GLASS_PANE")
            .fillBackground(true);

        builder.addCloseButton(53);

        MenuData menuData = builder.build();
        String menuId = "_missions_" + player.getUniqueId();
        registerMenu(menuId, menuData);
        openMenu(player, menuId);
    }
}
