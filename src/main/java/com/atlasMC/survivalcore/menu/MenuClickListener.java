package com.atlasMC.survivalcore.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class MenuClickListener implements Listener {
    private final MenuManager menuManager;
    private final Map<Inventory, MenuData> registeredMenus = new HashMap<>();

    public MenuClickListener(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void registerInventory(Inventory inv, MenuData menuData) {
        registeredMenus.put(inv, menuData);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        if (clicked == null || !registeredMenus.containsKey(clicked)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        MenuData menuData = registeredMenus.get(clicked);

        if (slot < 0 || slot >= menuData.getSize()) {
            return;
        }

        if (menuData.getItem(slot) == null) {
            return;
        }

        MenuAction action = menuData.getAction(slot);
        action.execute(player, menuManager);
    }
}
