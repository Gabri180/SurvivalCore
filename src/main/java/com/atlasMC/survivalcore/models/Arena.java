package com.atlasMC.survivalcore.models;

import java.util.*;

public class Arena {
    private String id;
    private String name;
    private String world;
    private int maxPlayers;
    private boolean active;
    private long entryFee;
    private long winReward;
    private List<UUID> participants = new ArrayList<>();
    private Map<UUID, Integer> playerStats = new HashMap<>();

    public Arena() {}

    public Arena(String id, String name, String world, int maxPlayers, boolean active, long entryFee, long winReward) {
        this.id = id;
        this.name = name;
        this.world = world;
        this.maxPlayers = maxPlayers;
        this.active = active;
        this.entryFee = entryFee;
        this.winReward = winReward;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWorld() { return world; }
    public void setWorld(String world) { this.world = world; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getEntryFee() { return entryFee; }
    public void setEntryFee(long entryFee) { this.entryFee = entryFee; }

    public long getWinReward() { return winReward; }
    public void setWinReward(long winReward) { this.winReward = winReward; }

    public List<UUID> getParticipants() { return participants; }
    public void setParticipants(List<UUID> participants) { this.participants = participants; }

    public Map<UUID, Integer> getPlayerStats() { return playerStats; }
    public void addParticipant(UUID uuid) {
        if (!participants.contains(uuid)) {
            participants.add(uuid);
        }
    }

    public void removeParticipant(UUID uuid) {
        participants.remove(uuid);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String name;
        private String world;
        private int maxPlayers;
        private boolean active;
        private long entryFee;
        private long winReward;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder world(String world) { this.world = world; return this; }
        public Builder maxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder entryFee(long entryFee) { this.entryFee = entryFee; return this; }
        public Builder winReward(long winReward) { this.winReward = winReward; return this; }

        public Arena build() {
            return new Arena(id, name, world, maxPlayers, active, entryFee, winReward);
        }
    }
}
