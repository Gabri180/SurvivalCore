package com.atlasMC.survivalcore.menu;

import org.bukkit.inventory.ItemStack;

public interface EditableMenu {
    ItemStack getItem(int slot);
    void setItem(int slot, ItemStack item);
    void save();
    void reload();
    int getSize();
}
