package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.TournamentStatus;
import java.time.Instant;
import java.util.*;

public class Tournament {
    private long id;
    private String name;
    private TournamentStatus status;
    private int maxPlayers;
    private List<UUID> participants = new ArrayList<>();
    private UUID winner;
    private long prizePool;
    private Instant createdAt;
    private Instant startedAt;
    private Instant endedAt;

    public Tournament() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TournamentStatus getStatus() { return status; }
    public void setStatus(TournamentStatus status) { this.status = status; }
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public List<UUID> getParticipants() { return participants; }
    public void setParticipants(List<UUID> participants) { this.participants = participants; }
    public UUID getWinner() { return winner; }
    public void setWinner(UUID winner) { this.winner = winner; }
    public long getPrizePool() { return prizePool; }
    public void setPrizePool(long prizePool) { this.prizePool = prizePool; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    public Instant getEndedAt() { return endedAt; }
    public void setEndedAt(Instant endedAt) { this.endedAt = endedAt; }

    public boolean isFull() { return participants.size() >= maxPlayers; }
    public boolean canJoin() { return status == TournamentStatus.OPEN && !isFull(); }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long id;
        private String name;
        private TournamentStatus status;
        private int maxPlayers;
        private List<UUID> participants = new ArrayList<>();
        private UUID winner;
        private long prizePool;
        private Instant createdAt;
        private Instant startedAt;
        private Instant endedAt;

        public Builder id(long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder status(TournamentStatus status) { this.status = status; return this; }
        public Builder maxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; return this; }
        public Builder participants(List<UUID> participants) { this.participants = participants; return this; }
        public Builder winner(UUID winner) { this.winner = winner; return this; }
        public Builder prizePool(long prizePool) { this.prizePool = prizePool; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder startedAt(Instant startedAt) { this.startedAt = startedAt; return this; }
        public Builder endedAt(Instant endedAt) { this.endedAt = endedAt; return this; }

        public Tournament build() {
            Tournament obj = new Tournament();
            obj.id = this.id;
            obj.name = this.name;
            obj.status = this.status;
            obj.maxPlayers = this.maxPlayers;
            obj.participants = this.participants;
            obj.winner = this.winner;
            obj.prizePool = this.prizePool;
            obj.createdAt = this.createdAt;
            obj.startedAt = this.startedAt;
            obj.endedAt = this.endedAt;
            return obj;
        }
    }
}
