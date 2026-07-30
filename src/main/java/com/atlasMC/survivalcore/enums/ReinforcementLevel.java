package com.atlasMC.survivalcore.enums;

public enum ReinforcementLevel {
    NONE(0, "§r", 0),
    IRON(1, "§7§l⚒", 5000),
    DIAMOND(2, "§b§l◆", 15000),
    OBSIDIAN(3, "§5§l⬢", 50000);

    final int level;
    final String icon;
    final long cost;

    ReinforcementLevel(int level, String icon, long cost) {
        this.level = level;
        this.icon = icon;
        this.cost = cost;
    }

    public int getLevel() { return level; }
    public String getIcon() { return icon; }
    public long getCost() { return cost; }
    public double getDamageReduction() { return level * 0.25; }

    public ReinforcementLevel upgrade() {
        return switch (this) {
            case NONE -> IRON;
            case IRON -> DIAMOND;
            case DIAMOND -> OBSIDIAN;
            case OBSIDIAN -> OBSIDIAN;
        };
    }
}
