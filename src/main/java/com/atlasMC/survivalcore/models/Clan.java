package com.atlasMC.survivalcore.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

public class Clan {

    private long id;
    @NotNull
    private String name;
    private long ownerId;
    private long money;
    private int power;
    @Nullable
    private Instant createdAt;

    @NotNull
    private List<ClanMember> members = new ArrayList<>();
    @NotNull
    private List<Long> alliedClans = new ArrayList<>();

    public Clan() {
    }

    public Clan(long id, @NotNull String name, long ownerId, long money, int power, @Nullable Instant createdAt, @NotNull List<ClanMember> members) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.money = money;
        this.power = power;
        this.createdAt = createdAt;
        this.members = members;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @NotNull
    public String getName() { return name; }
    public void setName(@NotNull String name) { this.name = name; }

    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    public long getMoney() { return money; }
    public void setMoney(long money) { this.money = money; }
    public void addMoney(long amount) { this.money += amount; }
    public void removeMoney(long amount) { this.money = Math.max(0, this.money - amount); }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    @Nullable
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(@Nullable Instant createdAt) { this.createdAt = createdAt; }

    @NotNull
    public List<ClanMember> getMembers() { return members; }
    public void setMembers(@NotNull List<ClanMember> members) { this.members = members; }

    @NotNull
    public List<Long> getAlliedClans() { return alliedClans; }
    public void setAlliedClans(@NotNull List<Long> alliedClans) { this.alliedClans = alliedClans; }
    public void addAlly(long clanId) {
        if (!alliedClans.contains(clanId)) {
            alliedClans.add(clanId);
        }
    }
    public void removeAlly(long clanId) { alliedClans.remove(clanId); }

    public boolean hasMember(long playerId) {
        return members.stream().anyMatch(m -> m.getPlayerId() == playerId);
    }

    public boolean isAlly(long clanId) {
        return alliedClans.contains(clanId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String name;
        private long ownerId;
        private long money;
        private int power;
        private Instant createdAt;
        private List<ClanMember> members = new ArrayList<>();
        private List<Long> alliedClans = new ArrayList<>();

        public Builder id(long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder ownerId(long ownerId) { this.ownerId = ownerId; return this; }
        public Builder money(long money) { this.money = money; return this; }
        public Builder power(int power) { this.power = power; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder members(List<ClanMember> members) { this.members = members; return this; }
        public Builder alliedClans(List<Long> alliedClans) { this.alliedClans = alliedClans; return this; }

        public Clan build() {
            Clan obj = new Clan();
            obj.id = this.id;
            obj.name = this.name;
            obj.ownerId = this.ownerId;
            obj.money = this.money;
            obj.power = this.power;
            obj.createdAt = this.createdAt;
            obj.members = this.members;
            obj.alliedClans = this.alliedClans;
            return obj;
        }
    }
}
