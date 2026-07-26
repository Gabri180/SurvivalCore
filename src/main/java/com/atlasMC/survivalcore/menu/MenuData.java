package com.atlasMC.survivalcore.menu;

import org.bukkit.inventory.ItemStack;

public class MenuData {
    private final String name;
    private final String title;
    private final int size;
    private final MenuAction[] actions;
    private final ItemStack[] items;

    public MenuData(String name, String title, int size) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.actions = new MenuAction[size];
        this.items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            this.actions[i] = MenuAction.none();
        }
    }

    public void setItem(int slot, ItemStack item, MenuAction action) {
        if (slot < 0 || slot >= size) return;
        items[slot] = item;
        actions[slot] = action != null ? action : MenuAction.none();
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, MenuAction.none());
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= size) return null;
        return items[slot];
    }

    public MenuAction getAction(int slot) {
        if (slot < 0 || slot >= size) return MenuAction.none();
        return actions[slot];
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }
}
