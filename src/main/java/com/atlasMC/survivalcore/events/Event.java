package com.atlasMC.survivalcore.events;

import com.atlasMC.survivalcore.enums.EventType;

/**
 * Interfaz para eventos especiales en SurvivalCore.
 * v1.0.19+
 */
public interface Event {
    EventType getEventType();
    long getStartTime();
    long getEndTime();
    double getMultiplier();

    default boolean isActive() {
        long now = System.currentTimeMillis();
        return now >= getStartTime() && now < getEndTime();
    }

    default long getTimeRemainingMs() {
        long now = System.currentTimeMillis();
        if (!isActive()) return 0;
        return getEndTime() - now;
    }
}
