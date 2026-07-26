package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Clan;

import java.util.UUID;

/**
 * Dev3 - interfaz sin implementacion. Se conecta a
 * {@link com.atlasMC.survivalcore.db.ClanRepository} manana.
 */
public interface IClanManager {

    Clan createClan(UUID ownerUuid, String name);

    void disbandClan(long clanId);

    void invite(long clanId, UUID targetUuid);

    void kick(long clanId, UUID targetUuid);

    Clan getClanByPlayer(UUID playerUuid);
}
