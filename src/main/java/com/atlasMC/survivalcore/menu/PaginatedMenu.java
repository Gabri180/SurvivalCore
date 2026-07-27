package com.atlasMC.survivalcore.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class PaginatedMenu {

    private final String menuId;
    private final String baseTitle;
    private final List<PaginatedItem> allItems;
    private final int itemsPerPage;
    private int currentPage = 0;
    private String permission = null;

    public PaginatedMenu(String menuId, String baseTitle, List<PaginatedItem> items, int itemsPerPage) {
        this.menuId = menuId;
        this.baseTitle = baseTitle;
        this.allItems = new ArrayList<>(items);
        this.itemsPerPage = itemsPerPage;
    }

    public PaginatedMenu permission(String perm) {
        this.permission = perm;
        return this;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) allItems.size() / itemsPerPage);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setPage(int page) {
        int maxPage = getTotalPages() - 1;
        this.currentPage = Math.max(0, Math.min(page, maxPage));
    }

    public void nextPage() {
        setPage(currentPage + 1);
    }

    public void previousPage() {
        setPage(currentPage - 1);
    }

    public MenuData buildCurrentPage(int rows) {
        String title = String.format("%s §7[%d/%d]", baseTitle, currentPage + 1, getTotalPages());
        MenuData menu = new MenuData(menuId + "_page_" + currentPage, title, rows * 9);

        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, allItems.size());

        int slot = 0;
        for (int i = start; i < end; i++) {
            if (slot >= rows * 9 - 9) break; // Dejar fila para navegación
            PaginatedItem pItem = allItems.get(i);
            menu.setItem(slot, pItem.itemStack, pItem.action);
            slot++;
        }

        // Agregar botones de navegación en la fila inferior
        int navRow = (rows - 1) * 9;

        if (currentPage > 0) {
            ItemStack prevButton = createNavButton(Material.ARROW, "§6Página Anterior");
            menu.setItem(navRow + 3, prevButton, MenuAction.command("menupage prev"));
        }

        if (currentPage < getTotalPages() - 1) {
            ItemStack nextButton = createNavButton(Material.ARROW, "§6Página Siguiente");
            menu.setItem(navRow + 5, nextButton, MenuAction.command("menupage next"));
        }

        ItemStack closeButton = createNavButton(Material.BARRIER, "§cCerrar");
        menu.setItem(navRow + 8, closeButton, MenuAction.close());

        menu.setMetadata("permission", permission);
        menu.setMetadata("totalPages", getTotalPages());
        menu.setMetadata("currentPage", currentPage);

        return menu;
    }

    private ItemStack createNavButton(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static class PaginatedItem {
        public ItemStack itemStack;
        public MenuAction action;
        public String displayName;

        public PaginatedItem(ItemStack itemStack, MenuAction action, String displayName) {
            this.itemStack = itemStack;
            this.action = action;
            this.displayName = displayName;
        }

        public static PaginatedItem create(Material material, String displayName,
                                          MenuAction action, String... lore) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                if (lore.length > 0) {
                    List<String> loreList = new ArrayList<>(Arrays.asList(lore));
                    meta.setLore(loreList);
                }
                item.setItemMeta(meta);
            }
            return new PaginatedItem(item, action, displayName);
        }
    }

    public static class Builder {
        private String menuId;
        private String baseTitle;
        private List<PaginatedItem> items = new ArrayList<>();
        private int itemsPerPage = 18;
        private String permission;

        public Builder(String menuId, String baseTitle) {
            this.menuId = menuId;
            this.baseTitle = baseTitle;
        }

        public Builder itemsPerPage(int count) {
            this.itemsPerPage = count;
            return this;
        }

        public Builder addItem(Material material, String displayName, MenuAction action, String... lore) {
            items.add(PaginatedItem.create(material, displayName, action, lore));
            return this;
        }

        public Builder addItems(List<PaginatedItem> newItems) {
            items.addAll(newItems);
            return this;
        }

        public Builder permission(String perm) {
            this.permission = perm;
            return this;
        }

        public PaginatedMenu build() {
            return new PaginatedMenu(menuId, baseTitle, items, itemsPerPage)
                    .permission(permission);
        }
    }
}
