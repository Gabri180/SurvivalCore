package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.IClanManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.ClanRepository;
import com.atlasMC.survivalcore.enums.ClanRole;
import com.atlasMC.survivalcore.models.Clan;
import com.atlasMC.survivalcore.models.ClanMember;
import com.atlasMC.survivalcore.models.PlayerProfile;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanManagerImpl implements IClanManager {

    private final ClanRepository clanRepository;
    private final PlayerCache playerCache;
    private final Map<Long, Clan> clansCache = new HashMap<>();

    public ClanManagerImpl(ClanRepository clanRepository, PlayerCache playerCache) {
        this.clanRepository = clanRepository;
        this.playerCache = playerCache;
        clanRepository.loadAllClans(clans -> {
            clansCache.clear();
            clans.forEach(c -> clansCache.put(c.getId(), c));
        });
    }

    @Override
    public Clan createClan(UUID ownerUuid, String name) {
        PlayerProfile profile = playerCache.get(ownerUuid);
        if (profile == null) return null;
        Clan clan = Clan.builder()
                .name(name)
                .ownerId(profile.getPlayerId())
                .money(0)
                .power(0)
                .createdAt(Instant.now())
                .build();
        clanRepository.saveClan(clan, id -> {
            clan.setId(id);
            clansCache.put(id, clan);
        });
        return clan;
    }

    @Override
    public void disbandClan(long clanId) {
        clansCache.remove(clanId);
    }

    @Override
    public void invite(long clanId, UUID targetUuid) {
        PlayerProfile profile = playerCache.get(targetUuid);
        if (profile == null) return;
        Clan clan = clansCache.get(clanId);
        if (clan == null) return;
        ClanMember member = ClanMember.builder()
                .clanId(clanId)
                .playerId(profile.getPlayerId())
                .role(ClanRole.MEMBER)
                .joinedAt(Instant.now())
                .build();
        clanRepository.addMember(member);
        clan.getMembers().add(member);
    }

    @Override
    public void kick(long clanId, UUID targetUuid) {
        PlayerProfile profile = playerCache.get(targetUuid);
        if (profile == null) return;
        clanRepository.removeMember(clanId, profile.getPlayerId());
        Clan clan = clansCache.get(clanId);
        if (clan != null) clan.getMembers().removeIf(m -> m.getPlayerId() == profile.getPlayerId());
    }

    @Override
    public Clan getClanByPlayer(UUID playerUuid) {
        PlayerProfile profile = playerCache.get(playerUuid);
        if (profile == null) return null;
        return clansCache.values().stream()
                .filter(c -> c.hasMember(profile.getPlayerId()))
                .findFirst()
                .orElse(null);
    }
}
