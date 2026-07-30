package com.atlasMC.survivalcore.achievements;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AchievementManager {
    private final Map<String, Achievement> achievements = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Integer>> playerProgress = new ConcurrentHashMap<>();

    public AchievementManager() {
        initializeAchievements();
    }

    private void initializeAchievements() {
        addAchievement("money_1k", "Primer Millón", "Acumula 1,000,000 de dinero",
                Achievement.AchievementType.MONEY, 1000000, 5000);
        addAchievement("money_10k", "Magnate", "Acumula 10,000,000 de dinero",
                Achievement.AchievementType.MONEY, 10000000, 25000);

        addAchievement("arena_wins_10", "Primer Combate", "Gana 10 combates en arena",
                Achievement.AchievementType.ARENA, 10, 2500);
        addAchievement("arena_wins_100", "Campeón", "Gana 100 combates en arena",
                Achievement.AchievementType.ARENA, 100, 15000);

        addAchievement("clan_create", "Fundador", "Crea tu primer clan",
                Achievement.AchievementType.CLAN, 1, 5000);
        addAchievement("clan_members_10", "Líder Respetado", "Consigue 10 miembros en tu clan",
                Achievement.AchievementType.CLAN, 10, 10000);

        addAchievement("skill_level_50", "Experto", "Sube un skill al nivel 50",
                Achievement.AchievementType.SKILL, 50, 8000);
        addAchievement("all_skills_30", "Polivalente", "Sube todos los skills al nivel 30",
                Achievement.AchievementType.SKILL, 30, 20000);

        addAchievement("mission_complete_50", "Misionero", "Completa 50 misiones",
                Achievement.AchievementType.MISSION, 50, 12000);

        Bukkit.getLogger().info("§a[Achievements] Initialized " + achievements.size() + " achievements");
    }

    private void addAchievement(String id, String name, String description,
                                Achievement.AchievementType type, int requirement, double reward) {
        achievements.put(id, new Achievement(id, name, description, type, requirement, reward));
    }

    public void updatePlayerProgress(UUID uuid, Achievement.AchievementType type, int amount) {
        playerProgress.putIfAbsent(uuid, new HashMap<>());
        Map<String, Integer> progress = playerProgress.get(uuid);

        for (Achievement achievement : getAchievementsByType(type)) {
            if (!achievement.isUnlocked()) {
                int current = progress.getOrDefault(achievement.getId(), 0);
                progress.put(achievement.getId(), current + amount);

                achievement.addProgress(amount);

                if (achievement.isUnlocked()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        onAchievementUnlocked(player, achievement);
                    }
                }
            }
        }
    }

    private void onAchievementUnlocked(Player player, Achievement achievement) {
        String message = "§6§l🏆 ¡LOGRO DESBLOQUEADO! §r§e" + achievement.getName() +
                "§6 | Recompensa: §a$" + achievement.getReward();
        player.sendMessage(message);
        Bukkit.broadcastMessage("§6[Logro] " + player.getName() + " desbloqueó: " + achievement.getName());

        player.playSound(player.getLocation(),
                org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

    public Collection<Achievement> getAchievementsByType(Achievement.AchievementType type) {
        List<Achievement> result = new ArrayList<>();
        achievements.values().forEach(a -> {
            if (a.getType() == type) {
                result.add(a);
            }
        });
        return result;
    }

    public Optional<Achievement> getAchievement(String id) {
        return Optional.ofNullable(achievements.get(id));
    }

    public List<Achievement> getPlayerAchievements(UUID uuid) {
        List<Achievement> result = new ArrayList<>();
        achievements.values().forEach(a -> {
            Map<String, Integer> progress = playerProgress.getOrDefault(uuid, new HashMap<>());
            a.addProgress(progress.getOrDefault(a.getId(), 0) - a.getProgress());
            result.add(a);
        });
        return result;
    }

    public int getPlayerCompletionPercentage(UUID uuid) {
        List<Achievement> all = new ArrayList<>(achievements.values());
        if (all.isEmpty()) return 0;

        long unlockedCount = all.stream().filter(Achievement::isUnlocked).count();
        return (int) ((unlockedCount / (double) all.size()) * 100);
    }

    public String generateAchievementsList(UUID uuid) {
        List<Achievement> achievements = getPlayerAchievements(uuid);
        StringBuilder sb = new StringBuilder();

        sb.append("\n§6╔════════════════════════════════════════╗\n");
        sb.append("§6║         TUS LOGROS          ║\n");
        sb.append("§6╠════════════════════════════════════════╣\n");

        for (Achievement achievement : achievements) {
            String status = achievement.isUnlocked() ? "§a✓" : "§c✗";
            sb.append(status).append(" §e").append(achievement.getName()).append("\n");
            sb.append("  §7").append(achievement.getProgressBar()).append(" ").append(achievement.getPercentage()).append("%\n");
        }

        sb.append("§6╠════════════════════════════════════════╣\n");
        sb.append("§e Completado: ").append(getPlayerCompletionPercentage(uuid)).append("%\n");
        sb.append("§6╚════════════════════════════════════════╝\n");

        return sb.toString();
    }
}
