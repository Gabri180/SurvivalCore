package com.atlasMC.survivalcore.models;

import java.time.Instant;

public class ClanAlliance {
    private long id;
    private long clanA;
    private long clanB;
    private Instant createdAt;
    private boolean active;

    public ClanAlliance() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getClanA() { return clanA; }
    public void setClanA(long clanA) { this.clanA = clanA; }
    public long getClanB() { return clanB; }
    public void setClanB(long clanB) { this.clanB = clanB; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long id;
        private long clanA;
        private long clanB;
        private Instant createdAt;
        private boolean active;

        public Builder id(long id) { this.id = id; return this; }
        public Builder clanA(long clanA) { this.clanA = clanA; return this; }
        public Builder clanB(long clanB) { this.clanB = clanB; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public ClanAlliance build() {
            ClanAlliance obj = new ClanAlliance();
            obj.id = this.id;
            obj.clanA = this.clanA;
            obj.clanB = this.clanB;
            obj.createdAt = this.createdAt;
            obj.active = this.active;
            return obj;
        }
    }
}
