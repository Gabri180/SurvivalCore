package com.atlasMC.survivalcore.scheduler;

import com.atlasMC.survivalcore.cache.PlayerCache;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Maneja backup automático de BD MySQL cada 2 horas + limpieza de caché.
 * v1.0.18+
 */
public class BackupScheduler {

    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final PlayerCache playerCache;
    private final Path backupDir;
    private final String mysqldumpPath;
    private boolean mysqldumpAvailable = false;

    public BackupScheduler(JavaPlugin plugin, FileConfiguration config, PlayerCache playerCache) {
        this.plugin = plugin;
        this.config = config;
        this.playerCache = playerCache;
        this.backupDir = Paths.get(plugin.getDataFolder().getAbsolutePath(), "backups");
        this.mysqldumpPath = config.getString("backup.mysqldump-path", "mysqldump");

        createBackupDirectory();
        checkMysqldumpAvailability();
        startBackupSchedule();
        startCacheCleanup();
    }

    private void createBackupDirectory() {
        try {
            Files.createDirectories(backupDir);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "No se pudo crear directorio de backups", e);
        }
    }

    /**
     * Verifica si mysqldump está disponible en el sistema.
     */
    private void checkMysqldumpAvailability() {
        try {
            ProcessBuilder pb = new ProcessBuilder(mysqldumpPath, "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            mysqldumpAvailable = (exitCode == 0);

            if (mysqldumpAvailable) {
                plugin.getLogger().info("✓ mysqldump encontrado en: " + mysqldumpPath);
            } else {
                plugin.getLogger().warning("✗ mysqldump no encontrado. Backups deshabilitados.");
                plugin.getLogger().warning("  Configura 'backup.mysqldump-path' en config.yml");
                plugin.getLogger().warning("  En macOS: /usr/local/bin/mysqldump");
                plugin.getLogger().warning("  En Linux: /usr/bin/mysqldump o /bin/mysqldump");
            }
        } catch (Exception e) {
            mysqldumpAvailable = false;
            plugin.getLogger().warning("✗ Error al verificar mysqldump: " + e.getMessage());
            plugin.getLogger().warning("  Configura la ruta correcta en backup.mysqldump-path");
        }
    }

    /**
     * Ejecuta backup automático cada 2 horas.
     */
    private void startBackupSchedule() {
        if (!config.getBoolean("backup.enabled", true)) {
            plugin.getLogger().info("ℹ Backups automáticos deshabilitados en config.yml");
            return;
        }

        if (!mysqldumpAvailable) {
            plugin.getLogger().warning("⚠ Backups deshabilitados: mysqldump no disponible");
            return;
        }

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::performBackup,
                0L,
                20L * 60 * config.getInt("backup.interval-minutes", 120)
        );
    }

    /**
     * Limpia caché cada 5 minutos.
     */
    private void startCacheCleanup() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,
                () -> {
                    playerCache.invalidateExpired();
                    plugin.getLogger().finer("Caché de jugadores limpiado");
                },
                20L * 60 * 5,
                20L * 60 * 5
        );
    }

    /**
     * Ejecuta mysqldump de forma async.
     */
    private void performBackup() {
        if (!mysqldumpAvailable) {
            return;
        }

        try {
            String filename = String.format("backup-%s.sql",
                    new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()));
            Path backupFile = backupDir.resolve(filename);

            String host = config.getString("database.host", "localhost");
            int port = config.getInt("database.port", 3306);
            String database = config.getString("database.database", "survivalcore");
            String user = config.getString("database.user", "root");
            String password = config.getString("database.password", "");

            List<String> command = new ArrayList<>();
            command.add(mysqldumpPath);
            command.add("-h" + host);
            command.add("-P" + port);
            command.add("-u" + user);
            if (!password.isEmpty()) {
                command.add("-p" + password);
            }
            command.add("--single-transaction");
            command.add("--quick");
            command.add("--lock-tables=false");
            command.add(database);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectOutput(backupFile.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                long fileSize = Files.size(backupFile) / 1024; // KB
                plugin.getLogger().info("✓ Backup completado: " + backupFile +
                        " (" + fileSize + " KB)");
                rotateBackups();
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    StringBuilder error = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                    plugin.getLogger().warning("✗ Backup fallido: " + error);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error durante backup: " + e.getMessage(), e);
        }
    }

    /**
     * Mantiene solo los últimos N backups.
     */
    private void rotateBackups() {
        try {
            File[] files = backupDir.toFile().listFiles();
            if (files == null || files.length <= 10) return;

            List<File> backupFiles = new ArrayList<>(Arrays.asList(files));
            backupFiles.sort((a, b) -> Long.compare(b.lastModified(), a.lastModified()));

            for (int i = 10; i < backupFiles.size(); i++) {
                if (backupFiles.get(i).delete()) {
                    plugin.getLogger().finer("Backup antiguo eliminado: " + backupFiles.get(i).getName());
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error rotando backups", e);
        }
    }

    /**
     * Ejecuta backup manualmente (usado por comando).
     */
    public void performBackupNow() {
        if (!mysqldumpAvailable) {
            plugin.getLogger().warning("✗ Backup no disponible: mysqldump no encontrado");
            return;
        }
        performBackup();
    }
}
