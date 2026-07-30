package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.events.SpecialEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventManager {
    void createEvent(String id, String name, SpecialEvent.EventType type,
                     double multiplier, LocalDateTime start, LocalDateTime end, UUID creator);

    void deleteEvent(String id);
    void startEvent(String id);
    void stopEvent(String id);

    Optional<SpecialEvent> getEvent(String id);
    List<SpecialEvent> getActiveEvents();
    List<SpecialEvent> getAllEvents();
    List<SpecialEvent> getEventsByType(SpecialEvent.EventType type);

    double getMultiplierForPlayer(UUID uuid);
    boolean isEventActive(String id);

    void checkAndUpdateEvents();
    void saveEvents();
    void loadEvents();
}
