package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.models.ClanAlliance;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.util.*;

public class ClanAllianceManager {

    private final Map<Long, ClanAlliance> alliances = new HashMap<>();
    private long nextAllianceId = 1;

    public ClanAlliance createAlliance(long clanA, long clanB) {
        ClanAlliance alliance = ClanAlliance.builder()
                .id(nextAllianceId++)
                .clanA(clanA)
                .clanB(clanB)
                .createdAt(Instant.now())
                .active(true)
                .build();

        alliances.put(alliance.getId(), alliance);
        Bukkit.broadcastMessage("§6§l⚔ §r§6Nuevas alianza formada!");
        return alliance;
    }

    public void endAlliance(long allianceId) {
        ClanAlliance alliance = alliances.get(allianceId);
        if (alliance != null) {
            alliance.setActive(false);
            Bukkit.broadcastMessage("§7La alianza ha sido disuelta");
        }
    }

    public List<Long> getAlliesOf(long clanId) {
        List<Long> allies = new ArrayList<>();
        for (ClanAlliance alliance : alliances.values()) {
            if (!alliance.isActive()) continue;
            if (alliance.getClanA() == clanId) {
                allies.add(alliance.getClanB());
            } else if (alliance.getClanB() == clanId) {
                allies.add(alliance.getClanA());
            }
        }
        return allies;
    }

    public boolean areAllied(long clanA, long clanB) {
        return alliances.values().stream()
                .filter(ClanAlliance::isActive)
                .anyMatch(a -> (a.getClanA() == clanA && a.getClanB() == clanB) ||
                              (a.getClanA() == clanB && a.getClanB() == clanA));
    }

    public Collection<ClanAlliance> getAlliances() {
        return alliances.values().stream()
                .filter(ClanAlliance::isActive)
                .toList();
    }
}
