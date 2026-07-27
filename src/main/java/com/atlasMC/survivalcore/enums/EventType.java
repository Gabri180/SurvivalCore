package com.atlasMC.survivalcore.enums;

/**
 * Tipos de eventos especiales disponibles en SurvivalCore.
 * v1.0.19+
 */
public enum EventType {
    DOUBLE_XP("DOUBLE_XP", "§6Doble XP", 2.0),
    DOUBLE_MONEY("DOUBLE_MONEY", "§6Doble Dinero", 2.0);

    private final String key;
    private final String displayName;
    private final double defaultMultiplier;

    EventType(String key, String displayName, double defaultMultiplier) {
        this.key = key;
        this.displayName = displayName;
        this.defaultMultiplier = defaultMultiplier;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultMultiplier() {
        return defaultMultiplier;
    }

    public static EventType fromString(String str) {
        try {
            return EventType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
