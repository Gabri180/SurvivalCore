package com.atlasMC.survivalcore.listeners;

import com.atlasMC.survivalcore.menu.MenuEditorUI;
import com.atlasMC.survivalcore.menu.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuEditorListener implements Listener {

    private final MenuManager menuManager;
    private final Map<String, MenuEditorUI> activeEditors = new HashMap<>();

    public MenuEditorListener(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String title = event.getView().getTitle();
        if (!title.contains("Menu Editor") && !title.contains("Items de:") && !title.contains("Info Item")) {
            return;
        }

        event.setCancelled(true);

        MenuEditorUI editor = activeEditors.computeIfAbsent(player.getName(),
            k -> new MenuEditorUI(menuManager, player));

        String clickedItemName = event.getCurrentItem() != null &&
                                 event.getCurrentItem().getItemMeta() != null ?
                                 event.getCurrentItem().getItemMeta().getDisplayName() : "";

        handleEditorClick(player, editor, clickedItemName, title);
    }

    private void handleEditorClick(Player player, MenuEditorUI editor, String itemName, String title) {
        // Botón Volver (aparece en todos los menús)
        if (itemName.contains("Volver")) {
            if (title.contains("Items de:")) {
                editor.openMenuList();
            } else if (title.contains("Info Item")) {
                String menuId = extractMenuIdFromTitle(title);
                if (menuId != null) {
                    editor.openItemList(menuId);
                }
            }
            return;
        }

        // Menú de selección de menús
        if (title.contains("Menu Editor")) {
            if (itemName.startsWith("§6") && !itemName.contains("Ayuda")) {
                // Extraer el ID del menú del nombre (formato: §6§lID)
                String menuId = itemName.replaceAll("§[0-9a-f]", "").replaceAll("§l", "").trim();
                if (!menuId.isEmpty()) {
                    editor.openItemList(menuId);
                }
            }
            return;
        }

        // Menú de items
        if (title.contains("Items de:")) {
            String menuId = extractMenuIdFromTitle(title);
            if (menuId != null && itemName.startsWith("§b")) {
                // Extraer el slot del nombre del item
                Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
                Matcher matcher = pattern.matcher(itemName);
                if (matcher.find()) {
                    int slot = Integer.parseInt(matcher.group(1));
                    editor.openItemInfo(menuId, slot);
                }
            }
            return;
        }

        // Menú de información del item
        if (title.contains("Info Item")) {
            handleItemInfoClick(player, editor, itemName, title);
            return;
        }
    }

    private void handleItemInfoClick(Player player, MenuEditorUI editor, String itemName, String title) {
        if (itemName.contains("Atrás")) {
            String menuId = extractMenuIdFromTitle(title);
            if (menuId != null) {
                editor.openItemList(menuId);
            }
            return;
        }

        if (itemName.contains("Eliminar")) {
            String menuId = extractMenuIdFromTitle(title);
            int slot = extractSlotFromTitle(title);
            if (menuId != null) {
                player.sendMessage("§6Para eliminar el item, usa:");
                player.sendMessage("§e  /custommenu delitem " + menuId + " " + slot);
            }
            return;
        }

        if (itemName.contains("Recargar")) {
            String menuId = extractMenuIdFromTitle(title);
            int slot = extractSlotFromTitle(title);
            if (menuId != null) {
                editor.openItemInfo(menuId, slot);
            }
            return;
        }

        // Para otros botones, mostrar instrucciones
        String menuId = extractMenuIdFromTitle(title);
        int slot = extractSlotFromTitle(title);
        if (menuId != null) {
            player.sendMessage("§6Para editar este item, usa los siguientes comandos:");
            player.sendMessage("§e  Nombre: §f/custommenu item " + menuId + " " + slot + " <material> <nombre>");
            player.sendMessage("§e  Acción: §f/custommenu itemaction " + menuId + " " + slot + " <comando/mensaje/etc>");
            player.sendMessage("§7Ejemplos:");
            player.sendMessage("§e  /custommenu item " + menuId + " " + slot + " DIAMOND §a✓ Diamante");
            player.sendMessage("§e  /custommenu itemaction " + menuId + " " + slot + " say Hola!");
        }
    }

    private String extractMenuIdFromTitle(String title) {
        if (title.contains("Items de:")) {
            String[] parts = title.split(": ");
            if (parts.length > 1) {
                return parts[1].replaceAll("§[0-9a-f]", "").trim();
            }
        }
        if (title.contains("Info Item")) {
            // Formato: §6Info Item (Slot 0) - menúId
            Pattern pattern = Pattern.compile("Info Item \\(Slot \\d+\\) - (\\w+)");
            Matcher matcher = pattern.matcher(title.replaceAll("§[0-9a-f]", ""));
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private int extractSlotFromTitle(String title) {
        Pattern pattern = Pattern.compile("Slot (\\d+)");
        Matcher matcher = pattern.matcher(title);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
