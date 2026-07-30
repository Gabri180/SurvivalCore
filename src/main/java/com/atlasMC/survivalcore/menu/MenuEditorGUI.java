package com.atlasMC.survivalcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuEditorGUI implements Listener {

    private final EditableMenu menu;
    private final Inventory inv;
    private final JavaPlugin plugin;

    public MenuEditorGUI(EditableMenu menu, JavaPlugin plugin) {
        this.menu = menu;
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, menu.getSize(), "§6Editor de Menú");
        for (int i = 0; i < menu.getSize(); i++) {
            ItemStack item = menu.getItem(i);
            if (item != null) inv.setItem(i, item);
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        e.setCancelled(true);

        int slot = e.getRawSlot();
        if (slot >= menu.getSize()) return;

        if (e.isLeftClick()) {
            ItemStack cursor = e.getCursor();
            if (cursor != null && cursor.getType().isItem()) {
                menu.setItem(slot, cursor.clone());
                inv.setItem(slot, cursor);
            }
        } else if (e.isRightClick()) {
            menu.setItem(slot, null);
            inv.setItem(slot, null);
        }
    }
}
