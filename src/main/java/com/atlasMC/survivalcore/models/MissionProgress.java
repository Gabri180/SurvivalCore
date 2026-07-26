package com.atlasMC.survivalcore.models;

import org.jetbrains.annotations.Nullable;
import java.time.Instant;

public class MissionProgress {
    private long playerId;
    private long missionId;
    private int progress;
    private boolean completed;
    @Nullable
    private Instant completedAt;

    public MissionProgress() {}
    public MissionProgress(long playerId, long missionId, int progress, boolean completed, @Nullable Instant completedAt) {
        this.playerId = playerId;
        this.missionId = missionId;
        this.progress = progress;
        this.completed = completed;
        this.completedAt = completedAt;
    }

    public long getPlayerId() { return playerId; }
    public void setPlayerId(long playerId) { this.playerId = playerId; }
    public long getMissionId() { return missionId; }
    public void setMissionId(long missionId) { this.missionId = missionId; }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    @Nullable
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(@Nullable Instant completedAt) { this.completedAt = completedAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long playerId;
        private long missionId;
        private int progress;
        private boolean completed;
        private Instant completedAt;
        public Builder playerId(long playerId) { this.playerId = playerId; return this; }
        public Builder missionId(long missionId) { this.missionId = missionId; return this; }
        public Builder progress(int progress) { this.progress = progress; return this; }
        public Builder completed(boolean completed) { this.completed = completed; return this; }
        public Builder completedAt(Instant completedAt) { this.completedAt = completedAt; return this; }
        public MissionProgress build() {
            MissionProgress obj = new MissionProgress();
            obj.playerId = this.playerId;
            obj.missionId = this.missionId;
            obj.progress = this.progress;
            obj.completed = this.completed;
            obj.completedAt = this.completedAt;
            return obj;
        }
    }
}
