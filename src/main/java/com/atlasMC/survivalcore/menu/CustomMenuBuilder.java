package com.atlasMC.survivalcore.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CustomMenuBuilder {

    private final String menuId;
    private String title;
    public int rows = 3;
    private List<CustomMenuItem> items = new ArrayList<>();
    private String backgroundColor = "LIGHT_GRAY_STAINED_GLASS_PANE";
    private boolean fillBackground = true;
    private String particleEffect = null;
    private String permission = null;

    public CustomMenuBuilder(String menuId) {
        this.menuId = menuId;
        this.title = "§6Menú Personalizado";
    }

    public CustomMenuBuilder title(String title) {
        this.title = title;
        return this;
    }

    public CustomMenuBuilder rows(int rows) {
        if (rows >= 1 && rows <= 6) {
            this.rows = rows;
        }
        return this;
    }

    public CustomMenuBuilder backgroundColor(String material) {
        this.backgroundColor = material;
        return this;
    }

    public CustomMenuBuilder fillBackground(boolean fill) {
        this.fillBackground = fill;
        return this;
    }

    public CustomMenuBuilder particleEffect(String particle) {
        this.particleEffect = particle;
        return this;
    }

    public CustomMenuBuilder permission(String perm) {
        this.permission = perm;
        return this;
    }

    public CustomMenuBuilder addItem(int slot, Material material, String displayName,
                                      String action, String actionValue, String... lore) {
        if (slot < 0 || slot >= rows * 9) {
            return this;
        }

        ItemStack item = createItem(material, displayName, lore);
        MenuAction menuAction = MenuFactory.createAction(action, actionValue);
        items.add(new CustomMenuItem(slot, item, menuAction, displayName));
        return this;
    }

    public CustomMenuBuilder addItem(int slot, Material material, String displayName,
                                      MenuAction action, String... lore) {
        if (slot < 0 || slot >= rows * 9) {
            return this;
        }

        ItemStack item = createItem(material, displayName, lore);
        items.add(new CustomMenuItem(slot, item, action, displayName));
        return this;
    }

    public CustomMenuBuilder addButton(int slot, String displayName, String actionType,
                                        String actionValue, String... lore) {
        return addItem(slot, Material.DIAMOND_BLOCK, displayName, actionType, actionValue, lore);
    }

    public CustomMenuBuilder addCloseButton(int slot) {
        return addItem(slot, Material.BARRIER, "§cCerrar", MenuAction.close(), "§7Click para cerrar");
    }

    public CustomMenuBuilder addBackButton(int slot, String menuName) {
        return addItem(slot, Material.ARROW, "§6Volver", MenuAction.openMenu(menuName), "§7Volver al menú anterior");
    }

    public CustomMenuBuilder addSpacer(int slot) {
        try {
            Material bgMaterial = Material.valueOf(backgroundColor);
            ItemStack spacer = new ItemStack(bgMaterial);
            ItemMeta meta = spacer.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§r");
                spacer.setItemMeta(meta);
            }
            items.add(new CustomMenuItem(slot, spacer, MenuAction.none(), "spacer"));
        } catch (IllegalArgumentException e) {
            // Material no válido, ignorar
        }
        return this;
    }

    public CustomMenuBuilder addSpacerRow(int row) {
        for (int i = 0; i < 9; i++) {
            addSpacer(row * 9 + i);
        }
        return this;
    }

    public MenuData build() {
        MenuData menu = new MenuData(menuId, title, rows * 9);

        // Llenar fondo si está habilitado
        if (fillBackground) {
            try {
                Material bgMaterial = Material.valueOf(backgroundColor);
                Set<Integer> occupiedSlots = new HashSet<>();
                for (CustomMenuItem item : items) {
                    occupiedSlots.add(item.slot);
                }

                for (int i = 0; i < rows * 9; i++) {
                    if (!occupiedSlots.contains(i)) {
                        ItemStack bgItem = new ItemStack(bgMaterial);
                        ItemMeta meta = bgItem.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName("§r");
                            bgItem.setItemMeta(meta);
                        }
                        menu.setItem(i, bgItem, MenuAction.none());
                    }
                }
            } catch (IllegalArgumentException e) {
                // Material no válido, ignorar
            }
        }

        // Agregar items personalizados
        for (CustomMenuItem item : items) {
            menu.setItem(item.slot, item.itemStack, item.action);
        }

        // Guardar metadata
        menu.setMetadata("permission", permission);
        menu.setMetadata("particleEffect", particleEffect != null ? particleEffect : "NONE");

        return menu;
    }

    public CustomMenuTemplate toTemplate() {
        return new CustomMenuTemplate(menuId, title, rows, backgroundColor, fillBackground,
                particleEffect != null ? particleEffect : "NONE", permission, items);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                List<String> loreList = new ArrayList<>(Arrays.asList(lore));
                meta.setLore(loreList);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static class CustomMenuItem {
        public int slot;
        public ItemStack itemStack;
        public MenuAction action;
        public String displayName;

        public CustomMenuItem(int slot, ItemStack itemStack, MenuAction action, String displayName) {
            this.slot = slot;
            this.itemStack = itemStack;
            this.action = action;
            this.displayName = displayName;
        }
    }

    public static class CustomMenuTemplate {
        public String menuId;
        public String title;
        public int rows;
        public String backgroundColor;
        public boolean fillBackground;
        public String particleEffect;
        public String permission;
        public List<CustomMenuItem> items;

        public CustomMenuTemplate(String menuId, String title, int rows, String backgroundColor,
                                 boolean fillBackground, String particleEffect, String permission,
                                 List<CustomMenuItem> items) {
            this.menuId = menuId;
            this.title = title;
            this.rows = rows;
            this.backgroundColor = backgroundColor;
            this.fillBackground = fillBackground;
            this.particleEffect = particleEffect;
            this.permission = permission;
            this.items = new ArrayList<>(items);
        }
    }
}
