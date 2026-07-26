package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.JobType;

public class PlayerJob {
    private long playerId;
    private String jobName;
    private int level;
    private long exp;
    private JobType jobType;
    private long moneyEarned;
    private long lastPaid;

    public PlayerJob() {}
    public PlayerJob(long playerId, String jobName, int level, long exp, JobType jobType, long moneyEarned, long lastPaid) {
        this.playerId = playerId;
        this.jobName = jobName;
        this.level = level;
        this.exp = exp;
        this.jobType = jobType;
        this.moneyEarned = moneyEarned;
        this.lastPaid = lastPaid;
    }

    public long getPlayerId() { return playerId; }
    public void setPlayerId(long playerId) { this.playerId = playerId; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public long getExp() { return exp; }
    public void setExp(long exp) { this.exp = exp; }
    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }
    public long getMoneyEarned() { return moneyEarned; }
    public void setMoneyEarned(long moneyEarned) { this.moneyEarned = moneyEarned; }
    public long getLastPaid() { return lastPaid; }
    public void setLastPaid(long lastPaid) { this.lastPaid = lastPaid; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long playerId;
        private String jobName;
        private int level;
        private long exp;
        private JobType jobType;
        private long moneyEarned;
        private long lastPaid;
        public Builder playerId(long playerId) { this.playerId = playerId; return this; }
        public Builder jobName(String jobName) { this.jobName = jobName; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder exp(long exp) { this.exp = exp; return this; }
        public Builder jobType(JobType jobType) { this.jobType = jobType; return this; }
        public Builder moneyEarned(long moneyEarned) { this.moneyEarned = moneyEarned; return this; }
        public Builder lastPaid(long lastPaid) { this.lastPaid = lastPaid; return this; }
        public PlayerJob build() {
            PlayerJob obj = new PlayerJob();
            obj.playerId = this.playerId;
            obj.jobName = this.jobName;
            obj.level = this.level;
            obj.exp = this.exp;
            obj.jobType = this.jobType;
            obj.moneyEarned = this.moneyEarned;
            obj.lastPaid = this.lastPaid;
            return obj;
        }
    }
}
