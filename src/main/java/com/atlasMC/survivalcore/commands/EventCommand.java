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
 * v1.0.25+: Mejorado con más opciones y visualización
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
                    sender.sendMessage("§cUso: /event schedule <tipo> <minutos> [multiplicador]");
                    showTypes(sender);
                } else {
                    scheduleEvent(sender, args[1], args[2], args.length > 3 ? args[3] : null);
                }
            }
            case "stop" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUso: /event stop <tipo>");
                    showTypes(sender);
                } else {
                    stopEvent(sender, args[1]);
                }
            }
            case "refresh" -> {
                eventManager.refreshActiveEvents();
                sender.sendMessage("§a✓ Eventos recargados de BD");
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

        sender.sendMessage("§6═══ Eventos Activos ═══");
        for (ServerEvent event : events) {
            long remainingMs = event.getTimeRemainingMs();
            long minutes = remainingMs / (60 * 1000);
            long seconds = (remainingMs % (60 * 1000)) / 1000;

            String bar = createProgressBar((double) remainingMs / (60000 * 60), 20);
            sender.sendMessage(String.format("§e• %s", event.getEventType().getDisplayName()));
            sender.sendMessage(String.format("  §7Multiplicador: §b%.1fx", event.getMultiplier()));
            sender.sendMessage(String.format("  §7Tiempo: §e%d:%02d", minutes, seconds));
            sender.sendMessage(String.format("  §7%s", bar));
        }
    }

    private void listEvents(CommandSender sender) {
        sender.sendMessage("§6═══ Tipos de Evento Disponibles ═══");
        for (EventType type : EventType.values()) {
            sender.sendMessage(String.format("§e• %s §7(multiplicador por defecto: %.1fx)",
                    type.getDisplayName(), type.getDefaultMultiplier()));
        }
    }

    private void scheduleEvent(CommandSender sender, String typeStr, String minutesStr, String multStr) {
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

            double multiplier = eventType.getDefaultMultiplier();
            if (multStr != null) {
                try {
                    multiplier = Double.parseDouble(multStr);
                    if (multiplier < 1.0 || multiplier > 10.0) {
                        sender.sendMessage("§cEl multiplicador debe estar entre 1.0 y 10.0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cMultiplicador inválido");
                    return;
                }
            }

            long adminId = sender instanceof Player player ? player.getUniqueId().hashCode() : 0;
            eventManager.startEvent(eventType, multiplier, minutes, adminId);
            sender.sendMessage(String.format("§a✓ Evento programado: %s", eventType.getDisplayName()));
            sender.sendMessage(String.format("  §7Multiplicador: §b%.2fx", multiplier));
            sender.sendMessage(String.format("  §7Duración: §b%d minutos", minutes));
        } catch (NumberFormatException e) {
            sender.sendMessage("§cDuración inválida. Debe ser un número entre 1 y 1440");
        }
    }

    private void stopEvent(CommandSender sender, String typeStr) {
        EventType eventType = EventType.fromString(typeStr);
        if (eventType == null) {
            sender.sendMessage("§cTipo de evento no válido: " + typeStr);
            showTypes(sender);
            return;
        }

        ServerEvent event = eventManager.getActiveEvent(eventType);
        if (event == null) {
            sender.sendMessage("§cNo hay evento activo de tipo: " + eventType.getDisplayName());
            return;
        }

        sender.sendMessage("§a✓ Evento detenido: " + eventType.getDisplayName());
    }

    private void showTypes(CommandSender sender) {
        sender.sendMessage("§7Tipos válidos:");
        for (EventType type : EventType.values()) {
            sender.sendMessage("§7  - " + type.getKey());
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6═══ Comandos de Eventos ═══");
        sender.sendMessage("§e/event info §7- Ver eventos activos");
        sender.sendMessage("§e/event list §7- Listar tipos de eventos");
        sender.sendMessage("§e/event schedule <tipo> <min> [mult] §7- Programar evento");
        sender.sendMessage("§e/event stop <tipo> §7- Detener evento");
        sender.sendMessage("§e/event refresh §7- Recargar de BD");
    }

    private String createProgressBar(double progress, int length) {
        int filled = Math.max(0, Math.min(length, (int) (progress * length)));
        StringBuilder bar = new StringBuilder("§a");
        for (int i = 0; i < filled; i++) bar.append("█");
        bar.append("§7");
        for (int i = filled; i < length; i++) bar.append("░");
        return bar.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("info");
            completions.add("list");
            completions.add("schedule");
            completions.add("stop");
            completions.add("refresh");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("schedule") || args[0].equalsIgnoreCase("stop")) {
                for (EventType type : EventType.values()) {
                    completions.add(type.getKey());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("schedule")) {
            completions.add("60");
            completions.add("120");
            completions.add("360");
        } else if (args.length == 4 && args[0].equalsIgnoreCase("schedule")) {
            completions.add("2.0");
            completions.add("3.0");
            completions.add("5.0");
        }

        return completions;
    }
}
