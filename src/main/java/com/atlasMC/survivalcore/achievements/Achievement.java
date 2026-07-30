package com.atlasMC.survivalcore.achievements;

import java.util.UUID;

public class Achievement {
    private String id;
    private String name;
    private String description;
    private AchievementType type;
    private int progress;
    private int requirement;
    private double reward;
    private boolean unlocked;

    public enum AchievementType {
        MONEY("Dinero", "Acumula cantidad de dinero"),
        ARENA("Arena", "Gana combates en arenas"),
        CLAN("Clan", "Crea o únete a clanes"),
        SKILL("Skill", "Sube niveles en skills"),
        JOB("Job", "Completa trabajos"),
        AUCTION("Subasta", "Realiza subastas"),
        MISSION("Misión", "Completa misiones"),
        PVP("PvP", "Mata jugadores");

        private final String displayName;
        private final String description;

        AchievementType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    public Achievement(String id, String name, String description, AchievementType type,
                      int requirement, double reward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.requirement = requirement;
        this.reward = reward;
        this.progress = 0;
        this.unlocked = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public AchievementType getType() { return type; }
    public int getProgress() { return progress; }
    public int getRequirement() { return requirement; }
    public double getReward() { return reward; }
    public boolean isUnlocked() { return unlocked; }

    public void addProgress(int amount) {
        this.progress = Math.min(progress + amount, requirement);
        if (progress >= requirement && !unlocked) {
            this.unlocked = true;
        }
    }

    public int getPercentage() {
        return (int) ((progress / (double) requirement) * 100);
    }

    public String getProgressBar() {
        int percentage = getPercentage();
        int bars = percentage / 10;
        StringBuilder sb = new StringBuilder("§a");
        for (int i = 0; i < bars; i++) sb.append("█");
        sb.append("§7");
        for (int i = bars; i < 10; i++) sb.append("█");
        return sb.toString();
    }
}
