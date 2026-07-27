package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.Clan;
import com.atlasMC.survivalcore.enums.ClanRole;

import java.util.Collection;
import java.util.UUID;

public interface IClanManager {

    Clan createClan(UUID ownerUuid, String name);

    void disbandClan(long clanId);

    void invite(long clanId, UUID targetUuid);

    void kick(long clanId, UUID targetUuid);

    Clan getClanByPlayer(UUID playerUuid);

    void addClanMoney(long clanId, long amount);

    void removeClanMoney(long clanId, long amount);

    long getClanMoney(long clanId);

    void setPlayerRole(long clanId, UUID playerUuid, ClanRole role);

    ClanRole getPlayerRole(long clanId, UUID playerUuid);

    void createAlliance(long clan1Id, long clan2Id);

    void breakAlliance(long clan1Id, long clan2Id);

    Collection<Long> getAlliedClans(long clanId);

    Collection<Clan> getAllClans();

    Clan getClanById(long clanId);
}
