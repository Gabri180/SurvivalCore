package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.MissionType;
import com.atlasMC.survivalcore.enums.MissionFrequency;

public class Mission {
    private long id;
    private MissionType missionType;
    private String description;
    private int requiredCount;
    private long reward;
    private String target;
    private long rewardMoney;
    private MissionFrequency frequency;

    public Mission() {}
    public Mission(long id, MissionType missionType, String description, int requiredCount, long reward, String target, long rewardMoney, MissionFrequency frequency) {
        this.id = id;
        this.missionType = missionType;
        this.description = description;
        this.requiredCount = requiredCount;
        this.reward = reward;
        this.target = target;
        this.rewardMoney = rewardMoney;
        this.frequency = frequency;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public MissionType getMissionType() { return missionType; }
    public void setMissionType(MissionType missionType) { this.missionType = missionType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getRequiredCount() { return requiredCount; }
    public void setRequiredCount(int requiredCount) { this.requiredCount = requiredCount; }
    public long getReward() { return reward; }
    public void setReward(long reward) { this.reward = reward; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public long getRewardMoney() { return rewardMoney; }
    public void setRewardMoney(long rewardMoney) { this.rewardMoney = rewardMoney; }
    public MissionFrequency getFrequency() { return frequency; }
    public void setFrequency(MissionFrequency frequency) { this.frequency = frequency; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long id;
        private MissionType missionType;
        private String description;
        private int requiredCount;
        private long reward;
        private String target;
        private long rewardMoney;
        private MissionFrequency frequency;
        public Builder id(long id) { this.id = id; return this; }
        public Builder missionType(MissionType missionType) { this.missionType = missionType; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder requiredCount(int requiredCount) { this.requiredCount = requiredCount; return this; }
        public Builder reward(long reward) { this.reward = reward; return this; }
        public Builder target(String target) { this.target = target; return this; }
        public Builder rewardMoney(long rewardMoney) { this.rewardMoney = rewardMoney; return this; }
        public Builder frequency(MissionFrequency frequency) { this.frequency = frequency; return this; }
        public Mission build() {
            Mission obj = new Mission();
            obj.id = this.id;
            obj.missionType = this.missionType;
            obj.description = this.description;
            obj.requiredCount = this.requiredCount;
            obj.reward = this.reward;
            obj.target = this.target;
            obj.rewardMoney = this.rewardMoney;
            obj.frequency = this.frequency;
            return obj;
        }
    }
}
