package com.atlasMC.survivalcore.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuEditorUI {

    private final MenuManager menuManager;
    private final Player player;

    public MenuEditorUI(MenuManager menuManager, Player player) {
        this.menuManager = menuManager;
        this.player = player;
    }

    // Mostrar todos los menús disponibles
    public void openMenuList() {
        Map<String, MenuData> allMenus = menuManager.getAllMenus();

        if (allMenus.isEmpty()) {
            player.sendMessage("§cNo hay menús creados.");
            return;
        }

        CustomMenuBuilder builder = new CustomMenuBuilder("_editor_list_" + System.currentTimeMillis())
            .title("§6Menú Editor - Selecciona Menú")
            .rows(6)
            .backgroundColor("GRAY_STAINED_GLASS_PANE")
            .fillBackground(true);

        int slot = 0;
        for (String menuId : allMenus.keySet()) {
            if (slot >= 45) break;

            MenuData menu = allMenus.get(menuId);
            ItemStack item = createMenuItem(menuId, menu);
            builder.addItem(slot, item.getType(), item.getItemMeta().getDisplayName(), "EDITOR_SELECT:" + menuId, "");
            slot += 2;
        }

        MenuData editorMenu = builder.build();
        String editorMenuId = "_editor_list_" + player.getName();
        menuManager.registerMenu(editorMenuId, editorMenu);
        menuManager.openMenu(player, editorMenuId);
    }

    // Mostrar items de un menú
    public void openItemList(String menuId) {
        MenuData menu = menuManager.getMenu(menuId);
        if (menu == null) {
            player.sendMessage("§cMenú no encontrado: " + menuId);
            return;
        }

        CustomMenuBuilder builder = new CustomMenuBuilder("_editor_items_" + menuId)
            .title("§6Items de: " + menuId)
            .rows(6)
            .backgroundColor("DARK_GRAY_STAINED_GLASS_PANE")
            .fillBackground(true);

        int slot = 0;
        for (int i = 0; i < menu.getSize() && slot < 45; i++) {
            ItemStack item = menu.getItem(i);
            if (item == null) continue;

            ItemStack displayItem = createItemEditButton(item, i, menuId);
            builder.addItem(slot, displayItem.getType(), displayItem.getItemMeta().getDisplayName(), "EDITOR_EDIT:" + menuId + ":" + i, "");
            slot += 2;
        }

        // Botón atrás
        builder.addBackButton(45, "EDITOR_BACK");

        MenuData editorMenu = builder.build();
        String editorMenuId = "_editor_items_" + player.getName();
        menuManager.registerMenu(editorMenuId, editorMenu);
        menuManager.openMenu(player, editorMenuId);
    }

    // Menú para ver/editar información de un item
    public void openItemInfo(String menuId, int itemSlot) {
        MenuData menu = menuManager.getMenu(menuId);
        if (menu == null) {
            player.sendMessage("§cMenú no encontrado: " + menuId);
            return;
        }

        ItemStack item = menu.getItem(itemSlot);
        MenuAction action = menu.getAction(itemSlot);

        if (item == null) {
            player.sendMessage("§cItem no encontrado en slot " + itemSlot);
            return;
        }

        CustomMenuBuilder builder = new CustomMenuBuilder("_editor_info_" + menuId + "_" + itemSlot)
            .title("§6Info Item (Slot " + itemSlot + ")")
            .rows(3)
            .backgroundColor("BLACK_STAINED_GLASS_PANE")
            .fillBackground(true);

        // Mostrar información del item
        ItemMeta meta = item.getItemMeta();
        String itemName = meta != null ? meta.getDisplayName() : "Item";

        builder.addButton(0, "§e📝 Nombre", "EDITOR_SHOW:" + menuId + ":" + itemSlot, itemName);
        builder.addButton(2, "§e⚙️ Acción", "EDITOR_SHOW:" + menuId + ":" + itemSlot, action.getType().name());
        builder.addButton(4, "§e📖 Valor Acción", "EDITOR_SHOW:" + menuId + ":" + itemSlot, action.getValue());
        builder.addButton(6, "§e🔄 Recargar", "EDITOR_SHOW:" + menuId + ":" + itemSlot, "");
        builder.addButton(11, "§e🗑️ Eliminar", "DELETE_ITEM:" + menuId + ":" + itemSlot, "");

        builder.addBackButton(18, "EDITOR_ITEMS:" + menuId);

        MenuData editorMenu = builder.build();
        String editorMenuId = "_editor_info_" + player.getName();
        menuManager.registerMenu(editorMenuId, editorMenu);
        menuManager.openMenu(player, editorMenuId);

        // Mostrar información en chat
        showItemInfo(menuId, itemSlot, item, action);
    }

    private void showItemInfo(String menuId, int slot, ItemStack item, MenuAction action) {
        ItemMeta meta = item.getItemMeta();
        player.sendMessage("§6═══ Información del Item ═══");
        player.sendMessage("§7Slot: §f" + slot);
        if (meta != null) {
            player.sendMessage("§7Nombre: §f" + meta.getDisplayName());
        }
        player.sendMessage("§7Material: §f" + item.getType().name());
        player.sendMessage("§7Acción: §f" + action.getType().name());
        if (!action.getValue().isEmpty()) {
            player.sendMessage("§7Valor: §f" + action.getValue());
        }
        player.sendMessage("§7");
        player.sendMessage("§6Para editar usa los comandos:");
        player.sendMessage("§e  /custommenu item " + menuId + " " + slot + " <material> <nombre>");
        player.sendMessage("§e  /custommenu itemaction " + menuId + " " + slot + " <acción> <valor>");
    }

    // Crear item para mostrar en lista de menús
    private ItemStack createMenuItem(String menuId, MenuData menu) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e" + menuId);
            List<String> lore = new ArrayList<>();
            lore.add("§7Tamaño: §f" + menu.getSize());
            lore.add("§7Título: §f" + menu.getTitle());
            lore.add("");
            lore.add("§aClickea para editar items");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    // Crear item para mostrar en lista de items
    private ItemStack createItemEditButton(ItemStack original, int slot, String menuId) {
        ItemStack item = new ItemStack(original.getType());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ItemMeta originalMeta = original.getItemMeta();
            if (originalMeta != null && originalMeta.hasDisplayName()) {
                meta.setDisplayName("§b[" + slot + "] " + originalMeta.getDisplayName());
            } else {
                meta.setDisplayName("§b[" + slot + "] Item");
            }
            List<String> lore = new ArrayList<>();
            lore.add("§7Material: §f" + original.getType().name());
            lore.add("");
            lore.add("§aClickea para editar");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }
}
