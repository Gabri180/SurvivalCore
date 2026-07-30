package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.SkillType;

public class PlayerSkill {
    private long playerId;
    private SkillType skillType;
    private int level;
    private long exp;

    public PlayerSkill() {}
    public PlayerSkill(long playerId, SkillType skillType, int level, long exp) {
        this.playerId = playerId;
        this.skillType = skillType;
        this.level = level;
        this.exp = exp;
    }

    public long getPlayerId() { return playerId; }
    public void setPlayerId(long playerId) { this.playerId = playerId; }
    public SkillType getSkillType() { return skillType; }
    public void setSkillType(SkillType skillType) { this.skillType = skillType; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public long getExp() { return exp; }
    public void setExp(long exp) { this.exp = exp; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long playerId;
        private SkillType skillType;
        private int level;
        private long exp;
        public Builder playerId(long playerId) { this.playerId = playerId; return this; }
        public Builder skillType(SkillType skillType) { this.skillType = skillType; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder exp(long exp) { this.exp = exp; return this; }
        public PlayerSkill build() {
            PlayerSkill obj = new PlayerSkill();
            obj.playerId = this.playerId;
            obj.skillType = this.skillType;
            obj.level = this.level;
            obj.exp = this.exp;
            return obj;
        }
    }
}
