package com.atlasMC.survivalcore.models;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private long id;
    private String name;
    private String world;
    private int maxPlayers;
    private boolean active;
    private List<Long> participants = new ArrayList<>();

    public Arena() {}
    public Arena(long id, String name, String world, int maxPlayers, boolean active, List<Long> participants) {
        this.id = id;
        this.name = name;
        this.world = world;
        this.maxPlayers = maxPlayers;
        this.active = active;
        this.participants = participants;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getWorld() { return world; }
    public void setWorld(String world) { this.world = world; }
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public List<Long> getParticipants() { return participants; }
    public void setParticipants(List<Long> participants) { this.participants = participants; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private long id;
        private String name;
        private String world;
        private int maxPlayers;
        private boolean active;
        private List<Long> participants = new ArrayList<>();
        public Builder id(long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder world(String world) { this.world = world; return this; }
        public Builder maxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder participants(List<Long> participants) { this.participants = participants; return this; }
        public Arena build() {
            Arena obj = new Arena();
            obj.id = this.id;
            obj.name = this.name;
            obj.world = this.world;
            obj.maxPlayers = this.maxPlayers;
            obj.active = this.active;
            obj.participants = this.participants;
            return obj;
        }
    }
}
