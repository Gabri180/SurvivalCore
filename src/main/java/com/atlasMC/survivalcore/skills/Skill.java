package com.atlasMC.survivalcore.skills;

public class Skill {

    private String id;
    private String name;
    private String description;
    private SkillCategory category;
    private int level;
    private long experience;
    private long experienceRequired;

    public Skill(String id, String name, String description, SkillCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.level = 1;
        this.experience = 0;
        this.experienceRequired = 1000;
    }

    public void addExperience(long amount) {
        this.experience += amount;
        while (experience >= experienceRequired && level < 100) {
            experience -= experienceRequired;
            level++;
            experienceRequired = (long) (experienceRequired * 1.1);
        }
    }

    public int getPercentageToNextLevel() {
        return (int) ((double) experience / experienceRequired * 100);
    }

    public long getExperienceToNextLevel() {
        return experienceRequired - experience;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public SkillCategory getCategory() { return category; }
    public int getLevel() { return level; }
    public long getExperience() { return experience; }
    public long getExperienceRequired() { return experienceRequired; }

    public enum SkillCategory {
        COMBAT("Combate", "§c"),
        MINING("Minería", "§8"),
        FORAGING("Recolección", "§2"),
        FISHING("Pesca", "§b"),
        FARMING("Granjería", "§6"),
        CRAFTING("Artesanía", "§d");

        private final String displayName;
        private final String color;

        SkillCategory(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getColor() {
            return color;
        }
    }
}
