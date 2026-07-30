package com.atlasMC.survivalcore.events;

import com.atlasMC.survivalcore.db.EventRepository;
import com.atlasMC.survivalcore.enums.EventType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestiona eventos especiales del servidor.
 * v1.0.19+
 */
public class EventManager {

    private final JavaPlugin plugin;
    private final EventRepository eventRepository;
    private final List<ServerEvent> activeEvents = new CopyOnWriteArrayList<>();
    private final Map<EventType, ServerEvent> eventsByType = new HashMap<>();

    public EventManager(JavaPlugin plugin, EventRepository eventRepository) {
        this.plugin = plugin;
        this.eventRepository = eventRepository;
        startEventCheckTask();
        refreshActiveEvents();
    }

    /**
     * Obtiene el multiplicador para un tipo de evento.
     * Retorna 1.0 si no hay evento activo.
     */
    public double getEventMultiplier(EventType eventType) {
        ServerEvent event = eventsByType.get(eventType);
        if (event != null && event.isActive()) {
            return event.getMultiplier();
        }
        return 1.0;
    }

    /**
     * Obtiene el evento activo para un tipo específico.
     */
    public ServerEvent getActiveEvent(EventType eventType) {
        ServerEvent event = eventsByType.get(eventType);
        return event != null && event.isActive() ? event : null;
    }

    /**
     * Obtiene todos los eventos activos.
     */
    public List<ServerEvent> getActiveEvents() {
        return new ArrayList<>(activeEvents);
    }

    /**
     * Crea un nuevo evento.
     */
    public void startEvent(EventType eventType, double multiplier, int durationMinutes, long createdByPlayerId) {
        long now = System.currentTimeMillis();
        long endTime = now + (durationMinutes * 60 * 1000L);

        eventRepository.createEvent(eventType, multiplier, now, endTime, createdByPlayerId, eventId -> {
            ServerEvent event = new ServerEvent(eventId, eventType, multiplier, now, endTime);
            eventsByType.put(eventType, event);
            activeEvents.add(event);

            broadcastEventStart(event);
            scheduleEventEnd(event);

            plugin.getLogger().info("✓ Evento iniciado: " + eventType.getDisplayName() +
                    " (multiplicador: " + multiplier + "x por " + durationMinutes + " minutos)");
        });
    }

    /**
     * Refresca eventos activos desde BD.
     */
    public void refreshActiveEvents() {
        eventRepository.getActiveEvents(events -> {
            activeEvents.clear();
            eventsByType.clear();

            for (ServerEvent event : events) {
                activeEvents.add(event);
                eventsByType.put(event.getEventType(), event);
            }
        });
    }

    private void startEventCheckTask() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long now = System.currentTimeMillis();
            List<ServerEvent> toRemove = new ArrayList<>();

            for (ServerEvent event : activeEvents) {
                if (!event.isActive()) {
                    toRemove.add(event);
                    eventsByType.remove(event.getEventType());
                }
            }

            activeEvents.removeAll(toRemove);
        }, 20L * 60, 20L * 30); // Chequea cada 30 segundos
    }

    private void broadcastEventStart(ServerEvent event) {
        String message = String.format("§6✨ ¡EVENTO ACTIVADO! %s §6(Multiplicador: §e%.1fx§6) §6Por %d minutos",
                event.getEventType().getDisplayName(),
                event.getMultiplier(),
                (event.getEndTime() - System.currentTimeMillis()) / (60 * 1000));

        Bukkit.broadcastMessage(message);
    }

    private void scheduleEventEnd(ServerEvent event) {
        long delayTicks = (event.getEndTime() - System.currentTimeMillis()) / 50; // 50ms = 1 tick

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            String message = String.format("§c✗ Evento finalizado: %s",
                    event.getEventType().getDisplayName());

            Bukkit.broadcastMessage(message);
        }, Math.max(1, delayTicks));
    }
}
