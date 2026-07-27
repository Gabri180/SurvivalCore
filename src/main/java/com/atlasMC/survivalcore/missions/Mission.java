package com.atlasMC.survivalcore.missions;

import java.util.UUID;

public class Mission {

    private String id;
    private String name;
    private String description;
    private MissionType type;
    private int target;
    private long reward;
    private MissionFrequency frequency;
    private boolean completed;
    private int progress;

    public Mission(String id, String name, String description, MissionType type,
                   int target, long reward, MissionFrequency frequency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.target = target;
        this.reward = reward;
        this.frequency = frequency;
        this.completed = false;
        this.progress = 0;
    }

    public void addProgress(int amount) {
        this.progress = Math.min(progress + amount, target);
        if (progress >= target) {
            this.completed = true;
        }
    }

    public int getPercentage() {
        return (int) ((double) progress / target * 100);
    }

    public boolean isCompleted() {
        return completed;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public MissionType getType() { return type; }
    public int getTarget() { return target; }
    public long getReward() { return reward; }
    public MissionFrequency getFrequency() { return frequency; }
    public int getProgress() { return progress; }

    public String getProgressBar() {
        int percentage = getPercentage();
        int bars = percentage / 10;
        StringBuilder bar = new StringBuilder("§a");
        for (int i = 0; i < bars; i++) bar.append("█");
        bar.append("§7");
        for (int i = bars; i < 10; i++) bar.append("█");
        bar.append(String.format(" §f%d%%", percentage));
        return bar.toString();
    }

    public enum MissionType {
        KILL("Matar"),
        COLLECT("Recolectar"),
        TRAVEL("Viajar"),
        CRAFT("Craftear"),
        MINE("Minar"),
        FISH("Pescar");

        private final String displayName;

        MissionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum MissionFrequency {
        DAILY("Diaria"),
        WEEKLY("Semanal"),
        MONTHLY("Mensual"),
        PERMANENT("Permanente");

        private final String displayName;

        MissionFrequency(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
