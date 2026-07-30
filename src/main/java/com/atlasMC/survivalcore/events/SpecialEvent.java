package com.atlasMC.survivalcore.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class SpecialEvent {
    private String id;
    private String name;
    private EventType type;
    private double multiplier;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private UUID creator;

    public enum EventType {
        DOUBLE_XP("Doble XP"),
        DOUBLE_MONEY("Doble Dinero"),
        BONUS_ARENA("Bonus Arena"),
        SEASONAL("Evento Estacional"),
        CUSTOM("Evento Personalizado");

        private final String displayName;

        EventType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public SpecialEvent(String id, String name, EventType type, double multiplier,
                       LocalDateTime startTime, LocalDateTime endTime, UUID creator) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.multiplier = multiplier;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creator = creator;
        this.active = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public EventType getType() { return type; }
    public double getMultiplier() { return multiplier; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public boolean isActive() { return active; }
    public UUID getCreator() { return creator; }

    public void setActive(boolean active) { this.active = active; }
    public void setName(String name) { this.name = name; }
    public void setMultiplier(double multiplier) { this.multiplier = multiplier; }

    public boolean hasExpired() {
        return LocalDateTime.now().isAfter(endTime);
    }

    public boolean hasStarted() {
        return LocalDateTime.now().isAfter(startTime);
    }

    public long getTimeRemainingMinutes() {
        return java.time.temporal.ChronoUnit.MINUTES.between(LocalDateTime.now(), endTime);
    }
}
