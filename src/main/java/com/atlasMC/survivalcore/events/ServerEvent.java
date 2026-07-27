package com.atlasMC.survivalcore.events;

import com.atlasMC.survivalcore.enums.EventType;

/**
 * Evento especial con multiplicador temporal.
 * v1.0.19+
 */
public class ServerEvent implements Event {

    private final long id;
    private final EventType eventType;
    private final double multiplier;
    private final long startTime;
    private final long endTime;

    public ServerEvent(long id, EventType eventType, double multiplier, long startTime, long endTime) {
        this.id = id;
        this.eventType = eventType;
        this.multiplier = multiplier;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public double getMultiplier() {
        return multiplier;
    }

    public long getId() {
        return id;
    }
}
