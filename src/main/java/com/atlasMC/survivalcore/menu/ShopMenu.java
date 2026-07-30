package com.atlasMC.survivalcore.menu;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ShopMenu implements EditableMenu {

    private final ItemStack[] items = new ItemStack[54];
    private final JavaPlugin plugin;

    public ShopMenu(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        var config = plugin.getConfig().getConfigurationSection("shop.items");
        if (config == null) return;
        // TODO: cargar desde shop.yml si existe
    }

    @Override
    public ItemStack getItem(int slot) {
        return items[slot];
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        items[slot] = item;
    }

    @Override
    public void save() {
        // Guardar a YAML o BD
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public int getSize() {
        return 54;
    }
}
