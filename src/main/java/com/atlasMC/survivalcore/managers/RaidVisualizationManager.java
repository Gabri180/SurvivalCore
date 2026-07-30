package com.atlasMC.survivalcore.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class RaidVisualizationManager {

    private final JavaPlugin plugin;

    public RaidVisualizationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void showClaimBorders(Player player, Location min, Location max) {
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 100;

            @Override
            public void run() {
                if (ticks++ >= maxTicks) {
                    cancel();
                    return;
                }

                // Mostrar líneas de partículas en los bordes del claim
                drawCubeBorder(player, min, max);
            }

            private void drawCubeBorder(Player player, Location min, Location max) {
                // Línea inferior frontal
                drawLine(player, min, new Location(min.getWorld(), max.getX(), min.getY(), min.getZ()), 10);
                // Línea inferior derecha
                drawLine(player, new Location(min.getWorld(), max.getX(), min.getY(), min.getZ()),
                        new Location(min.getWorld(), max.getX(), min.getY(), max.getZ()), 10);
                // Línea inferior trasera
                drawLine(player, new Location(min.getWorld(), max.getX(), min.getY(), max.getZ()),
                        new Location(min.getWorld(), min.getX(), min.getY(), max.getZ()), 10);
                // Línea inferior izquierda
                drawLine(player, new Location(min.getWorld(), min.getX(), min.getY(), max.getZ()),
                        min, 10);

                // Línea superior frontal
                drawLine(player, new Location(min.getWorld(), min.getX(), max.getY(), min.getZ()),
                        new Location(min.getWorld(), max.getX(), max.getY(), min.getZ()), 10);
                // Línea superior derecha
                drawLine(player, new Location(min.getWorld(), max.getX(), max.getY(), min.getZ()),
                        new Location(min.getWorld(), max.getX(), max.getY(), max.getZ()), 10);
                // Línea superior trasera
                drawLine(player, new Location(min.getWorld(), max.getX(), max.getY(), max.getZ()),
                        new Location(min.getWorld(), min.getX(), max.getY(), max.getZ()), 10);
                // Línea superior izquierda
                drawLine(player, new Location(min.getWorld(), min.getX(), max.getY(), max.getZ()),
                        new Location(min.getWorld(), min.getX(), max.getY(), min.getZ()), 10);

                // Líneas verticales
                drawLine(player, min, new Location(min.getWorld(), min.getX(), max.getY(), min.getZ()), 10);
                drawLine(player, new Location(min.getWorld(), max.getX(), min.getY(), min.getZ()),
                        new Location(min.getWorld(), max.getX(), max.getY(), min.getZ()), 10);
                drawLine(player, new Location(min.getWorld(), max.getX(), min.getY(), max.getZ()),
                        max, 10);
                drawLine(player, new Location(min.getWorld(), min.getX(), min.getY(), max.getZ()),
                        new Location(min.getWorld(), min.getX(), max.getY(), max.getZ()), 10);
            }

            private void drawLine(Player player, Location from, Location to, int steps) {
                for (int i = 0; i <= steps; i++) {
                    double t = (double) i / steps;
                    Location point = from.clone();
                    point.setX(from.getX() + (to.getX() - from.getX()) * t);
                    point.setY(from.getY() + (to.getY() - from.getY()) * t);
                    point.setZ(from.getZ() + (to.getZ() - from.getZ()) * t);

                    player.spawnParticle(Particle.END_ROD, point, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    public void showRaidInProgress(Player player, Location claimCenter) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks++ >= 1200) cancel();

                player.spawnParticle(Particle.FLAME, claimCenter.add(0, 2, 0), 10,
                        5, 5, 5, 0.1);
            }
        }.runTaskTimer(plugin, 0, 5);
    }
}
