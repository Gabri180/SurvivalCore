package com.atlasMC.survivalcore.models;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Season {
    private long id;
    private int seasonNumber;
    private Instant startDate;
    private Instant endDate;
    private long rewardPool;
    private Map<Integer, Long> rewards = new HashMap<>();

    public Season() {}

    public Season(long id, int seasonNumber, Instant startDate, Instant endDate, long rewardPool, Map<Integer, Long> rewards) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardPool = rewardPool;
        this.rewards = rewards;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }

    public long getRewardPool() { return rewardPool; }
    public void setRewardPool(long rewardPool) { this.rewardPool = rewardPool; }

    public Map<Integer, Long> getRewards() { return rewards; }
    public void setRewards(Map<Integer, Long> rewards) { this.rewards = rewards; }

    public boolean isActive() {
        Instant now = Instant.now();
        return (startDate == null || now.isAfter(startDate)) && (endDate == null || now.isBefore(endDate));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private int seasonNumber;
        private Instant startDate;
        private Instant endDate;
        private long rewardPool;
        private Map<Integer, Long> rewards = new HashMap<>();

        public Builder id(long id) { this.id = id; return this; }
        public Builder seasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; return this; }
        public Builder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public Builder endDate(Instant endDate) { this.endDate = endDate; return this; }
        public Builder rewardPool(long rewardPool) { this.rewardPool = rewardPool; return this; }
        public Builder rewards(Map<Integer, Long> rewards) { this.rewards = rewards; return this; }

        public Season build() {
            Season obj = new Season();
            obj.id = this.id;
            obj.seasonNumber = this.seasonNumber;
            obj.startDate = this.startDate;
            obj.endDate = this.endDate;
            obj.rewardPool = this.rewardPool;
            obj.rewards = this.rewards;
            return obj;
        }
    }
}
