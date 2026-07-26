package com.atlasMC.survivalcore.models;

import com.atlasMC.survivalcore.enums.ClanRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * Dev3 - modelo puro, sin persistencia. Se conecta a BD manana.
 */
public class ClanMember {

    private long clanId;
    private long playerId;
    @NotNull
    private ClanRole role;
    @Nullable
    private Instant joinedAt;

    public ClanMember() {
    }

    public ClanMember(long clanId, long playerId, @NotNull ClanRole role, @Nullable Instant joinedAt) {
        this.clanId = clanId;
        this.playerId = playerId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public long getClanId() { return clanId; }
    public void setClanId(long clanId) { this.clanId = clanId; }

    public long getPlayerId() { return playerId; }
    public void setPlayerId(long playerId) { this.playerId = playerId; }

    @NotNull
    public ClanRole getRole() { return role; }
    public void setRole(@NotNull ClanRole role) { this.role = role; }

    @Nullable
    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(@Nullable Instant joinedAt) { this.joinedAt = joinedAt; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long clanId;
        private long playerId;
        private ClanRole role;
        private Instant joinedAt;

        public Builder clanId(long clanId) { this.clanId = clanId; return this; }
        public Builder playerId(long playerId) { this.playerId = playerId; return this; }
        public Builder role(ClanRole role) { this.role = role; return this; }
        public Builder joinedAt(Instant joinedAt) { this.joinedAt = joinedAt; return this; }

        public ClanMember build() {
            ClanMember obj = new ClanMember();
            obj.clanId = this.clanId;
            obj.playerId = this.playerId;
            obj.role = this.role;
            obj.joinedAt = this.joinedAt;
            return obj;
        }
    }
}
