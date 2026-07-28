package com.atlasMC.survivalcore.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class AdvancedPvPManager {

    private final Map<UUID, Integer> killStreaks = new HashMap<>();
    private final Map<UUID, Integer> totalKills = new HashMap<>();
    private final Map<UUID, Integer> totalDeaths = new HashMap<>();
    private final Map<UUID, Long> firstBloodTime = new HashMap<>();

    public void recordKill(UUID killerUuid, UUID victimUuid) {
        // Incrementar killstreak
        int streak = killStreaks.getOrDefault(killerUuid, 0) + 1;
        killStreaks.put(killerUuid, streak);

        // Incrementar total kills
        totalKills.put(killerUuid, totalKills.getOrDefault(killerUuid, 0) + 1);

        // Incrementar deaths
        totalDeaths.put(victimUuid, totalDeaths.getOrDefault(victimUuid, 0) + 1);

        // Reset killstreak de víctima
        killStreaks.remove(victimUuid);

        // Notificaciones
        Player killer = Bukkit.getPlayer(killerUuid);
        Player victim = Bukkit.getPlayer(victimUuid);

        if (killer != null) {
            String message = getKillStreakMessage(streak);
            killer.sendMessage("§a✓ §6Mataste a §e" + (victim != null ? victim.getName() : "???") + message);
        }

        if (streak > 5) {
            Bukkit.broadcastMessage("§6§l🔥 §r§e" + (killer != null ? killer.getName() : "???") + " §6está en racha de §e" + streak + " kills!");
        }
    }

    public int getKillStreak(UUID playerUuid) {
        return killStreaks.getOrDefault(playerUuid, 0);
    }

    public int getTotalKills(UUID playerUuid) {
        return totalKills.getOrDefault(playerUuid, 0);
    }

    public int getTotalDeaths(UUID playerUuid) {
        return totalDeaths.getOrDefault(playerUuid, 0);
    }

    public double getKillDeathRatio(UUID playerUuid) {
        int kills = getTotalKills(playerUuid);
        int deaths = getTotalDeaths(playerUuid);
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    public List<UUID> getTopKillers(int limit) {
        return totalKills.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void resetStreaks(UUID playerUuid) {
        killStreaks.remove(playerUuid);
    }

    public void resetAllStats() {
        killStreaks.clear();
        totalKills.clear();
        totalDeaths.clear();
    }

    private String getKillStreakMessage(int streak) {
        return switch (streak) {
            case 3 -> " §7[§6Racha x3§7]";
            case 5 -> " §7[§cRacha x5§7]";
            case 10 -> " §7[§4§l¡RACHA x10!§r§7]";
            default -> "";
        };
    }

    public String getStatsString(UUID playerUuid) {
        return "§6Kills: §e" + getTotalKills(playerUuid) +
               " §6Deaths: §e" + getTotalDeaths(playerUuid) +
               " §6K/D: §e" + String.format("%.2f", getKillDeathRatio(playerUuid)) +
               " §6Racha: §e" + getKillStreak(playerUuid);
    }
}
