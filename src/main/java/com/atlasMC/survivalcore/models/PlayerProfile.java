package com.atlasMC.survivalcore.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerProfile {
    private long playerId;
    private UUID uuid;
    private String name;
    private long money;
    private long premiumMoney;
    private String rank;
    private int prestige;
    private Map<String, Integer> skills = new HashMap<>();
    private String currentJob;
    private int jobLevel;
    private long jobExp;
    private List<Integer> activeMissions = new ArrayList<>();
    private long lastLogin;
    private long playtime;

    public long getBalance() {
        return money;
    }

    public void addMoney(long amount) {
        this.money += amount;
    }

    public boolean removeMoney(long amount) {
        if (money < amount) {
            return false;
        }
        money -= amount;
        return true;
    }

    public long getPlayerId() { return playerId; }
    public void setPlayerId(long playerId) { this.playerId = playerId; }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getMoney() { return money; }
    public void setMoney(long money) { this.money = money; }

    public long getPremiumMoney() { return premiumMoney; }
    public void setPremiumMoney(long premiumMoney) { this.premiumMoney = premiumMoney; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public int getPrestige() { return prestige; }
    public void setPrestige(int prestige) { this.prestige = prestige; }

    public Map<String, Integer> getSkills() { return skills; }
    public void setSkills(Map<String, Integer> skills) { this.skills = skills; }

    public String getCurrentJob() { return currentJob; }
    public void setCurrentJob(String currentJob) { this.currentJob = currentJob; }

    public int getJobLevel() { return jobLevel; }
    public void setJobLevel(int jobLevel) { this.jobLevel = jobLevel; }

    public long getJobExp() { return jobExp; }
    public void setJobExp(long jobExp) { this.jobExp = jobExp; }

    public List<Integer> getActiveMissions() { return activeMissions; }
    public void setActiveMissions(List<Integer> activeMissions) { this.activeMissions = activeMissions; }

    public long getLastLogin() { return lastLogin; }
    public void setLastLogin(long lastLogin) { this.lastLogin = lastLogin; }

    public long getPlaytime() { return playtime; }
    public void setPlaytime(long playtime) { this.playtime = playtime; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long playerId;
        private UUID uuid;
        private String name;
        private long money;
        private long premiumMoney;
        private String rank;
        private int prestige;
        private Map<String, Integer> skills = new HashMap<>();
        private String currentJob;
        private int jobLevel;
        private long jobExp;
        private List<Integer> activeMissions = new ArrayList<>();
        private long lastLogin;
        private long playtime;

        public Builder playerId(long playerId) { this.playerId = playerId; return this; }
        public Builder uuid(UUID uuid) { this.uuid = uuid; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder money(long money) { this.money = money; return this; }
        public Builder premiumMoney(long premiumMoney) { this.premiumMoney = premiumMoney; return this; }
        public Builder rank(String rank) { this.rank = rank; return this; }
        public Builder prestige(int prestige) { this.prestige = prestige; return this; }
        public Builder skills(Map<String, Integer> skills) { this.skills = skills; return this; }
        public Builder currentJob(String currentJob) { this.currentJob = currentJob; return this; }
        public Builder jobLevel(int jobLevel) { this.jobLevel = jobLevel; return this; }
        public Builder jobExp(long jobExp) { this.jobExp = jobExp; return this; }
        public Builder activeMissions(List<Integer> activeMissions) { this.activeMissions = activeMissions; return this; }
        public Builder lastLogin(long lastLogin) { this.lastLogin = lastLogin; return this; }
        public Builder playtime(long playtime) { this.playtime = playtime; return this; }

        public PlayerProfile build() {
            PlayerProfile profile = new PlayerProfile();
            profile.playerId = this.playerId;
            profile.uuid = this.uuid;
            profile.name = this.name;
            profile.money = this.money;
            profile.premiumMoney = this.premiumMoney;
            profile.rank = this.rank;
            profile.prestige = this.prestige;
            profile.skills = this.skills;
            profile.currentJob = this.currentJob;
            profile.jobLevel = this.jobLevel;
            profile.jobExp = this.jobExp;
            profile.activeMissions = this.activeMissions;
            profile.lastLogin = this.lastLogin;
            profile.playtime = this.playtime;
            return profile;
        }
    }
}
