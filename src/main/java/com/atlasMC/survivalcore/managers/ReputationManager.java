package com.atlasMC.survivalcore.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ReputationManager {

    private final Map<UUID, Integer> reputationScores = new HashMap<>();
    private static final int OUTLAW_THRESHOLD = -100;
    private static final int HERO_THRESHOLD = 100;

    public enum ReputationLevel {
        OUTLAW(-100, "§4§l⚔"),
        NEUTRAL(0, "§7§l⚪"),
        HERO(100, "§a§l⭐");

        final int threshold;
        final String displayIcon;

        ReputationLevel(int threshold, String displayIcon) {
            this.threshold = threshold;
            this.displayIcon = displayIcon;
        }
    }

    public void addReputation(UUID playerUuid, int amount) {
        int current = reputationScores.getOrDefault(playerUuid, 0);
        int newRep = Math.max(-200, Math.min(200, current + amount));
        reputationScores.put(playerUuid, newRep);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            String message = amount > 0 ? "§a+" + amount : "§c" + amount;
            player.sendMessage("§6Reputación: " + message + " (Total: " + newRep + ")");
        }
    }

    public ReputationLevel getLevel(UUID playerUuid) {
        int rep = reputationScores.getOrDefault(playerUuid, 0);
        if (rep >= HERO_THRESHOLD) return ReputationLevel.HERO;
        if (rep <= OUTLAW_THRESHOLD) return ReputationLevel.OUTLAW;
        return ReputationLevel.NEUTRAL;
    }

    public int getReputation(UUID playerUuid) {
        return reputationScores.getOrDefault(playerUuid, 0);
    }

    public boolean isOutlaw(UUID playerUuid) {
        return getLevel(playerUuid) == ReputationLevel.OUTLAW;
    }

    public boolean isHero(UUID playerUuid) {
        return getLevel(playerUuid) == ReputationLevel.HERO;
    }

    public double getPriceMultiplier(UUID buyerUuid) {
        ReputationLevel level = getLevel(buyerUuid);
        return switch (level) {
            case OUTLAW -> 1.3;   // 30% más caro
            case NEUTRAL -> 1.0;   // Precio normal
            case HERO -> 0.85;     // 15% descuento
        };
    }

    public boolean canUseAuction(UUID playerUuid) {
        return !isOutlaw(playerUuid);
    }

    public void recordPvPVictory(UUID winnerUuid, UUID loserUuid) {
        addReputation(winnerUuid, 5);
        addReputation(loserUuid, -3);
    }

    public void recordBountyKill(UUID killerUuid, UUID targetUuid) {
        addReputation(killerUuid, 10);
        addReputation(targetUuid, -15);
    }

    public void recordTournamentVictory(UUID winnerUuid) {
        addReputation(winnerUuid, 20);
    }

    public Map<UUID, Integer> getTopReputations(int limit) {
        return reputationScores.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }
}
