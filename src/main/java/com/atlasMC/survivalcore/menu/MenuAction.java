package com.atlasMC.survivalcore.menu;

import org.bukkit.entity.Player;

public class MenuAction {
    public enum ActionType {
        COMMAND,      // Ejecuta un comando
        OPEN_MENU,    // Abre otro menú
        CLOSE,        // Cierra el menú
        MESSAGE,      // Envía un mensaje
        NONE          // Sin acción
    }

    private final ActionType type;
    private final String value;

    public MenuAction(ActionType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static MenuAction command(String command) {
        return new MenuAction(ActionType.COMMAND, command);
    }

    public static MenuAction openMenu(String menuName) {
        return new MenuAction(ActionType.OPEN_MENU, menuName);
    }

    public static MenuAction message(String message) {
        return new MenuAction(ActionType.MESSAGE, message);
    }

    public static MenuAction close() {
        return new MenuAction(ActionType.CLOSE, "");
    }

    public static MenuAction none() {
        return new MenuAction(ActionType.NONE, "");
    }

    public ActionType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void execute(Player player, MenuManager menuManager) {
        switch (type) {
            case COMMAND:
                String command = replacePlaceholders(value, player);
                player.performCommand(command);
                break;
            case OPEN_MENU:
                menuManager.openMenu(player, value);
                break;
            case MESSAGE:
                String message = replacePlaceholders(value, player);
                player.sendMessage(message);
                break;
            case CLOSE:
                player.closeInventory();
                break;
            case NONE:
            default:
                break;
        }
    }

    public static String replacePlaceholders(String text, Player player) {
        // Placeholders de jugador
        text = text.replace("%player%", player.getName());
        text = text.replace("%uuid%", player.getUniqueId().toString());
        text = text.replace("%display_name%", player.getDisplayName());
        text = text.replace("%health%", String.format("%.1f", player.getHealth()));
        text = text.replace("%max_health%", String.format("%.1f", player.getMaxHealth()));
        text = text.replace("%hunger%", String.valueOf(player.getFoodLevel()));
        text = text.replace("%saturation%", String.format("%.1f", player.getSaturation()));
        text = text.replace("%level%", String.valueOf(player.getLevel()));
        text = text.replace("%exp%", String.format("%.1f", player.getExp()));
        text = text.replace("%game_mode%", player.getGameMode().name());

        // Placeholders de ubicación
        text = text.replace("%world%", player.getWorld().getName());
        text = text.replace("%x%", String.valueOf((int) player.getLocation().getX()));
        text = text.replace("%y%", String.valueOf((int) player.getLocation().getY()));
        text = text.replace("%z%", String.valueOf((int) player.getLocation().getZ()));
        text = text.replace("%yaw%", String.format("%.1f", player.getLocation().getYaw()));
        text = text.replace("%pitch%", String.format("%.1f", player.getLocation().getPitch()));

        // Placeholders de inventario
        text = text.replace("%held_item%", player.getInventory().getItemInMainHand().getType().name());
        text = text.replace("%off_hand%", player.getInventory().getItemInOffHand().getType().name());

        // Placeholders de tiempo
        long time = System.currentTimeMillis();
        text = text.replace("%time%", String.valueOf(time));
        text = text.replace("%timestamp%", String.valueOf(System.currentTimeMillis() / 1000));

        // Placeholders de ping
        text = text.replace("%ping%", String.valueOf(player.getPing()));

        // Placeholders de servidor
        text = text.replace("%online_players%", String.valueOf(player.getServer().getOnlinePlayers().size()));
        text = text.replace("%max_players%", String.valueOf(player.getServer().getMaxPlayers()));

        // TODO: Placeholders de items (%item_name_<menu>_<slot>%, %item_description_<menu>_<slot>%)
        // Será implementado en versión futura con mejor manejo de MenuManager

        return text;
    }
}
