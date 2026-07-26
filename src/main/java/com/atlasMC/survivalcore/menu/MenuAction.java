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
                player.performCommand(value);
                break;
            case OPEN_MENU:
                menuManager.openMenu(player, value);
                break;
            case MESSAGE:
                player.sendMessage(value);
                break;
            case CLOSE:
                player.closeInventory();
                break;
            case NONE:
            default:
                break;
        }
    }
}
