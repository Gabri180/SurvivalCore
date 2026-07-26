package com.atlasMC.survivalcore.menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MenuEditorManager {
    private final MenuManager menuManager;
    private final MenuYamlWriter yamlWriter;
    private final Map<Player, EditSession> editSessions = new HashMap<>();

    public MenuEditorManager(MenuManager menuManager, MenuYamlWriter yamlWriter) {
        this.menuManager = menuManager;
        this.yamlWriter = yamlWriter;
    }

    public void startEditSession(Player player, String menuName) {
        MenuData menuData = menuManager.getMenu(menuName);
        if (menuData == null) {
            player.sendMessage("§cMenú no encontrado: " + menuName);
            return;
        }

        EditSession session = new EditSession(menuName, menuData);
        editSessions.put(player, session);
        player.sendMessage("§a╔════════════════════════════════╗");
        player.sendMessage("§a║ §bEdición iniciada para: " + menuName);
        player.sendMessage("§a║ §7Usa: /menu slot <número>");
        player.sendMessage("§a║ §7Luego edita con: /menu material, name, action");
        player.sendMessage("§a║ §7Guarda con: /menu save");
        player.sendMessage("§a╚════════════════════════════════╝");
    }

    public void openEditorMenu(Player player) {
        EditSession session = editSessions.get(player);
        if (session == null) {
            player.sendMessage("§cNo hay sesión de edición activa");
            return;
        }

        player.sendMessage("§e>>> Editor de Menú: " + session.getMenuName());
        player.sendMessage("§7Slots disponibles: 0-" + (session.getMenuData().getSize() - 1));
    }

    public void selectSlot(Player player, int slot) {
        EditSession session = editSessions.get(player);
        if (session == null) return;

        if (slot < 0 || slot >= session.getMenuData().getSize()) {
            player.sendMessage("§cSlot inválido. Máximo: " + (session.getMenuData().getSize() - 1));
            return;
        }

        session.setSelectedSlot(slot);
        player.sendMessage("§a✓ Slot seleccionado: " + slot);
        player.sendMessage("§7Ahora usa: /menu material, /menu name, /menu action");
    }

    public void setMaterial(Player player, String materialName) {
        EditSession session = editSessions.get(player);
        if (session == null) return;

        session.setItemMaterial(materialName);
        player.sendMessage("§aMaterial establecido a: " + materialName);
    }

    public void setItemName(Player player, String name) {
        EditSession session = editSessions.get(player);
        if (session == null) return;

        session.setItemName(name);
        player.sendMessage("§aNombre establecido a: " + name);
    }

    public void setAction(Player player, String actionType, String value) {
        EditSession session = editSessions.get(player);
        if (session == null) return;

        try {
            MenuAction.ActionType type = MenuAction.ActionType.valueOf(actionType.toUpperCase());
            session.setAction(new MenuAction(type, value));
            player.sendMessage("§aAcción establecida a: " + actionType);
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cTipo de acción inválido: " + actionType);
        }
    }

    public void saveMenu(Player player) {
        EditSession session = editSessions.get(player);
        if (session == null) return;

        yamlWriter.saveMenu(session.getMenuData(), session.getMenuName());
        menuManager.registerMenu(session.getMenuName(), session.getMenuData());
        player.sendMessage("§a✓ Menú guardado: " + session.getMenuName());
    }

    public void cancelEdit(Player player) {
        editSessions.remove(player);
        player.sendMessage("§cEdición cancelada");
    }

    public EditSession getSession(Player player) {
        return editSessions.get(player);
    }

    public static class EditSession {
        private final String menuName;
        private final MenuData menuData;
        private int selectedSlot = -1;

        public EditSession(String menuName, MenuData menuData) {
            this.menuName = menuName;
            this.menuData = menuData;
        }

        public void setSelectedSlot(int slot) {
            this.selectedSlot = slot;
        }

        public int getSelectedSlot() {
            return selectedSlot;
        }

        public void setItemMaterial(String materialName) {
            if (selectedSlot < 0) return;
            org.bukkit.Material material = org.bukkit.Material.valueOf(materialName.toUpperCase());
            org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material);
            menuData.setItem(selectedSlot, item);
        }

        public void setItemName(String name) {
            if (selectedSlot < 0) return;
            org.bukkit.inventory.ItemStack item = menuData.getItem(selectedSlot);
            if (item == null) return;

            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                item.setItemMeta(meta);
            }
        }

        public void setAction(MenuAction action) {
            if (selectedSlot < 0) return;
            org.bukkit.inventory.ItemStack item = menuData.getItem(selectedSlot);
            if (item == null) return;

            menuData.setItem(selectedSlot, item, action);
        }

        public String getMenuName() {
            return menuName;
        }

        public MenuData getMenuData() {
            return menuData;
        }
    }
}
