package com.atlasMC.survivalcore.enums;

public enum PetType {
    WOLF("§6Lobo", "🐺", 5000, 100),
    DRAGON("§5Dragón", "🐉", 20000, 200),
    PHOENIX("§c🔥 Fénix", "🔥", 25000, 250),
    DEMON("§4Demonio", "👿", 15000, 180),
    ANGEL("§e⭐ Ángel", "✨", 18000, 190),
    CAT("§7Gato", "🐱", 3000, 50),
    FAIRY("§b🧚 Hada", "✨", 8000, 120);

    final String displayName;
    final String icon;
    final long cost;
    final int maxHealth;

    PetType(String displayName, String icon, long cost, int maxHealth) {
        this.displayName = displayName;
        this.icon = icon;
        this.cost = cost;
        this.maxHealth = maxHealth;
    }

    public String getDisplayName() { return displayName; }
    public String getIcon() { return icon; }
    public long getCost() { return cost; }
    public int getMaxHealth() { return maxHealth; }
}
