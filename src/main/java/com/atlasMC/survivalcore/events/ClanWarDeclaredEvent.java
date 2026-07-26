package com.atlasMC.survivalcore.events;


/**
 * Emitido via EventAPI cuando un clan declara guerra a otro.
 */
public class ClanWarDeclaredEvent {

    private final long attackingClanId;
    private final long defendingClanId;

    public ClanWarDeclaredEvent(long attackingClanId, long defendingClanId) {
        this.attackingClanId = attackingClanId;
        this.defendingClanId = defendingClanId;
    }

    public long getAttackingClanId() { return attackingClanId; }
    public long getDefendingClanId() { return defendingClanId; }
}
