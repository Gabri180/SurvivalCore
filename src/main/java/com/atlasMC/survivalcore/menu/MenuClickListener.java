package com.atlasMC.survivalcore.menu;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuClickListener implements Listener {
    private final MenuManager menuManager;
    private final MenuManager menuManagerRef;

    public MenuClickListener(MenuManager menuManager, MenuManager menuManagerRef) {
        this.menuManager = menuManager;
        this.menuManagerRef = menuManagerRef;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        MenuData menuData = menuManagerRef.getPlayerMenu(player.getUniqueId());

        if (menuData == null) {
            return;
        }

        event.setCancelled(true);

        int slot = event.getRawSlot();

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
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        MenuData menuData = menuManagerRef.getPlayerMenu(player.getUniqueId());

        if (menuData == null) {
            return;
        }

        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5f, 0.5f);
    }
}
