package com.atlasMC.survivalcore.menu;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class MenuClickListener implements Listener {
    private final MenuManager menuManager;
    private final Map<Inventory, MenuData> registeredMenus = new WeakHashMap<>();

    public MenuClickListener(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void registerInventory(Inventory inv, MenuData menuData) {
        registeredMenus.put(inv, menuData);
    }

    public void unregisterInventory(Inventory inv) {
        registeredMenus.remove(inv);
    }

    @EventHandler(priority = EventPriority.HIGH)
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

        try {
            MenuAction action = menuData.getAction(slot);
            if (action != null && action.getType() != MenuAction.ActionType.NONE) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                action.execute(player, menuManager);
            }
        } catch (Exception e) {
            player.sendMessage("§cError al ejecutar acción del menú: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory() == null || !registeredMenus.containsKey(event.getInventory())) {
            return;
        }

        event.setCancelled(true);

        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5f, 0.5f);
        }
    }
}
