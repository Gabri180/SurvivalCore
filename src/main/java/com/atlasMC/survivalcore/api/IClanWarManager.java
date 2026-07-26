package com.atlasMC.survivalcore.api;

/**
 * Dev3 - interfaz sin implementacion. Se conecta a BD manana.
 */
public interface IClanWarManager {

    void declareWar(long attackingClanId, long defendingClanId);

    void endWar(long attackingClanId, long defendingClanId);

    boolean isAtWar(long clanIdA, long clanIdB);
}
