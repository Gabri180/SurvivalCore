package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.IClanWarManager;
import com.atlasMC.survivalcore.db.ClanWarRepository;

import java.util.HashMap;
import java.util.Map;

public class ClanWarManagerImpl implements IClanWarManager {

    private final ClanWarRepository warRepository;
    private final Map<Long, ClanWarRepository.ClanWarRow> warsCache = new HashMap<>();

    public ClanWarManagerImpl(ClanWarRepository warRepository) {
        this.warRepository = warRepository;
        warRepository.loadActiveWars(wars -> {
            warsCache.clear();
            wars.forEach(w -> warsCache.put(w.id(), w));
        });
    }

    @Override
    public void declareWar(long attackingClanId, long defendingClanId) {
        warRepository.startWar(attackingClanId, defendingClanId, id ->
                warRepository.loadActiveWars(wars -> {
                    warsCache.clear();
                    wars.forEach(w -> warsCache.put(w.id(), w));
                }));
    }

    @Override
    public void endWar(long attackingClanId, long defendingClanId) {
        warsCache.values().stream()
                .filter(w -> w.attackingClanId() == attackingClanId && w.defendingClanId() == defendingClanId && w.active())
                .findFirst()
                .ifPresent(w -> {
                    warRepository.endWar(w.id(), null);
                    warsCache.remove(w.id());
                });
    }

    @Override
    public boolean isAtWar(long clanIdA, long clanIdB) {
        return warsCache.values().stream()
                .anyMatch(w -> w.active() && (
                        (w.attackingClanId() == clanIdA && w.defendingClanId() == clanIdB) ||
                        (w.attackingClanId() == clanIdB && w.defendingClanId() == clanIdA)));
    }
}
