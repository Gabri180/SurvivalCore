package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.IEventManager;
import com.atlasMC.survivalcore.events.SpecialEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventManagerImpl implements IEventManager {
    private final Map<String, SpecialEvent> events = new ConcurrentHashMap<>();

    @Override
    public void createEvent(String id, String name, SpecialEvent.EventType type,
                           double multiplier, LocalDateTime start, LocalDateTime end, UUID creator) {
        SpecialEvent event = new SpecialEvent(id, name, type, multiplier, start, end, creator);
        events.put(id, event);
    }

    @Override
    public void deleteEvent(String id) {
        events.remove(id);
    }

    @Override
    public void startEvent(String id) {
        Optional<SpecialEvent> event = getEvent(id);
        event.ifPresent(e -> e.setActive(true));
    }

    @Override
    public void stopEvent(String id) {
        Optional<SpecialEvent> event = getEvent(id);
        event.ifPresent(e -> e.setActive(false));
    }

    @Override
    public Optional<SpecialEvent> getEvent(String id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public List<SpecialEvent> getActiveEvents() {
        checkAndUpdateEvents();
        return events.values().stream()
                .filter(SpecialEvent::isActive)
                .filter(e -> !e.hasExpired())
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecialEvent> getAllEvents() {
        return new ArrayList<>(events.values());
    }

    @Override
    public List<SpecialEvent> getEventsByType(SpecialEvent.EventType type) {
        return events.values().stream()
                .filter(e -> e.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public double getMultiplierForPlayer(UUID uuid) {
        double multiplier = 1.0;
        for (SpecialEvent event : getActiveEvents()) {
            if (event.getType() == SpecialEvent.EventType.DOUBLE_XP ||
                event.getType() == SpecialEvent.EventType.DOUBLE_MONEY ||
                event.getType() == SpecialEvent.EventType.CUSTOM) {
                multiplier *= event.getMultiplier();
            }
        }
        return multiplier;
    }

    @Override
    public boolean isEventActive(String id) {
        return getEvent(id)
                .map(e -> e.isActive() && !e.hasExpired())
                .orElse(false);
    }

    @Override
    public void checkAndUpdateEvents() {
        events.values().forEach(event -> {
            if (event.isActive() && event.hasExpired()) {
                event.setActive(false);
            }
        });
    }

    @Override
    public void saveEvents() {
    }

    @Override
    public void loadEvents() {
    }
}
