package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.enums.EventType;
import com.atlasMC.survivalcore.events.EventManager;
import com.atlasMC.survivalcore.events.ServerEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Comando /event para gestionar eventos especiales.
 * v1.0.19+
 */
public class EventCommand implements CommandExecutor, TabExecutor {

    private final EventManager eventManager;

    public EventCommand(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("survivalcore.event.admin")) {
            sender.sendMessage("§cNo tienes permiso");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "info" -> showActiveEvent(sender);
            case "list" -> listEvents(sender);
            case "schedule" -> {
                if (args.length < 3) {
                    sender.sendMessage("§cUso: /event schedule <tipo> <minutos>");
                    showTypes(sender);
                } else {
                    scheduleEvent(sender, args[1], args[2]);
                }
            }
            default -> showHelp(sender);
        }

        return true;
    }

    private void showActiveEvent(CommandSender sender) {
        List<ServerEvent> events = eventManager.getActiveEvents();
        if (events.isEmpty()) {
            sender.sendMessage("§7No hay eventos activos.");
            return;
        }

        sender.sendMessage("§6=== Eventos Activos ===");
        for (ServerEvent event : events) {
            long remainingMs = event.getTimeRemainingMs();
            long minutes = remainingMs / (60 * 1000);
            long seconds = (remainingMs % (60 * 1000)) / 1000;

            sender.sendMessage(String.format("§e• %s §7(%.1fx) - §eQuedan: §b%d:%02d",
                    event.getEventType().getDisplayName(),
                    event.getMultiplier(),
                    minutes, seconds));
        }
    }

    private void listEvents(CommandSender sender) {
        sender.sendMessage("§6=== Tipos de Evento Disponibles ===");
        for (EventType type : EventType.values()) {
            sender.sendMessage(String.format("§e• %s §7(multiplicador por defecto: %.1fx)",
                    type.getDisplayName(), type.getDefaultMultiplier()));
        }
    }

    private void scheduleEvent(CommandSender sender, String typeStr, String minutesStr) {
        EventType eventType = EventType.fromString(typeStr);
        if (eventType == null) {
            sender.sendMessage("§cTipo de evento no válido: " + typeStr);
            showTypes(sender);
            return;
        }

        try {
            int minutes = Integer.parseInt(minutesStr);
            if (minutes < 1 || minutes > 1440) {
                sender.sendMessage("§cLa duración debe estar entre 1 y 1440 minutos");
                return;
            }

            long adminId = sender instanceof Player player ? player.getUniqueId().hashCode() : 0;
            eventManager.startEvent(eventType, eventType.getDefaultMultiplier(), minutes, adminId);
            sender.sendMessage(String.format("§a✓ Evento programado: %s por %d minutos",
                    eventType.getDisplayName(), minutes));
        } catch (NumberFormatException e) {
            sender.sendMessage("§cDuración inválida. Debe ser un número entre 1 y 1440");
        }
    }

    private void showTypes(CommandSender sender) {
        sender.sendMessage("§7Tipos válidos:");
        for (EventType type : EventType.values()) {
            sender.sendMessage("§7  - " + type.getKey());
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6=== Comandos de Eventos ===");
        sender.sendMessage("§e/event info §7- Ver evento activo");
        sender.sendMessage("§e/event list §7- Listar tipos de eventos");
        sender.sendMessage("§e/event schedule <tipo> <minutos> §7- Programar evento");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("info");
            completions.add("list");
            completions.add("schedule");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("schedule")) {
            for (EventType type : EventType.values()) {
                completions.add(type.getKey());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("schedule")) {
            completions.add("60");
            completions.add("120");
            completions.add("360");
        }

        return completions;
    }
}
