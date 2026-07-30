package com.atlasMC.survivalcore.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class QualityOfLifeManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Boolean> afkStatus = new HashMap<>();
    private final Map<UUID, Long> lastAction = new HashMap<>();
    private static final long AFK_TIMEOUT = 5 * 60 * 1000; // 5 minutos

    public QualityOfLifeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startAFKChecker();
    }

    private void startAFKChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Long lastAction = QualityOfLifeManager.this.lastAction.getOrDefault(player.getUniqueId(), now);
                    boolean isAFK = (now - lastAction) > AFK_TIMEOUT;

                    if (isAFK && !afkStatus.getOrDefault(player.getUniqueId(), false)) {
                        afkStatus.put(player.getUniqueId(), true);
                        Bukkit.broadcastMessage("§7[AFK] §e" + player.getName() + " §7se fue a dormir");
                    } else if (!isAFK && afkStatus.getOrDefault(player.getUniqueId(), false)) {
                        afkStatus.put(player.getUniqueId(), false);
                        Bukkit.broadcastMessage("§a[BACK] §e" + player.getName() + " §avolvió");
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 60); // Chequear cada minuto
    }

    public void recordAction(UUID playerUuid) {
        lastAction.put(playerUuid, System.currentTimeMillis());
    }

    public boolean isAFK(UUID playerUuid) {
        return afkStatus.getOrDefault(playerUuid, false);
    }

    public void showMotd(Player player) {
        player.sendTitle("§6§lSurvivalCore", "§eBienvenido al servidor", 10, 70, 20);
        player.sendMessage("§6════════════════════════════════════");
        player.sendMessage("§6Bienvenido §e" + player.getName() + "§6!");
        player.sendMessage("§7Comandos útiles:");
        player.sendMessage("§e  /help §7- Ayuda general");
        player.sendMessage("§e  /stats §7- Tus estadísticas");
        player.sendMessage("§e  /clan §7- Sistema de clanes");
        player.sendMessage("§e  /leaderboard §7- Rankings");
        player.sendMessage("§6════════════════════════════════════");
    }

    public void showServerStatus() {
        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();
        long uptimeMilsec = System.currentTimeMillis(); // En producción, usar tiempo real
        int upDays = (int) (uptimeMilsec / (1000 * 60 * 60 * 24));

        Bukkit.broadcastMessage("§6╔════════════════════════════════════╗");
        Bukkit.broadcastMessage("§6║ §6Status del Servidor");
        Bukkit.broadcastMessage("§6║ §7Jugadores: §e" + online + "§7/§e" + max);
        Bukkit.broadcastMessage("§6║ §7Uptime: §e" + upDays + "§7d");
        Bukkit.broadcastMessage("§6╚════════════════════════════════════╝");
    }

    public void highlightPlayer(Player player) {
        player.setGlowing(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setGlowing(false), 5 * 20);
    }

    public void teleportPlayerWithMessage(Player player, Player target) {
        if (target == null || !target.isOnline()) {
            player.sendMessage("§c✗ Jugador no encontrado");
            return;
        }

        player.teleport(target);
        player.sendMessage("§a✓ §6Teleportado a §e" + target.getName());
        target.sendMessage("§6§e" + player.getName() + " §6se teletransportó a ti");
    }
}
