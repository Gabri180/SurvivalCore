package com.atlasMC.survivalcore.backup;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BackupManager {
    private final Plugin plugin;
    private final File backupDir;
    private BukkitTask autoBackupTask;
    private final int maxBackups = 20;
    private long backupIntervalMinutes = 120;

    public BackupManager(Plugin plugin) {
        this.plugin = plugin;
        this.backupDir = new File(plugin.getDataFolder(), "backups");
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
    }

    public void startAutoBackup() {
        autoBackupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::performBackup,
                backupIntervalMinutes * 20 * 60,
                backupIntervalMinutes * 20 * 60
        );
    }

    public void stopAutoBackup() {
        if (autoBackupTask != null) {
            autoBackupTask.cancel();
        }
    }

    public void performBackup() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = sdf.format(new Date());
            File backupFile = new File(backupDir, "backup_" + timestamp + ".zip");

            Bukkit.getLogger().info("§a[SurvivalCore] Starting backup: " + backupFile.getName());

            cleanOldBackups();

            Bukkit.getLogger().info("§a[SurvivalCore] Backup completed: " + backupFile.getAbsolutePath());
        } catch (Exception e) {
            Bukkit.getLogger().warning("§c[SurvivalCore] Backup failed: " + e.getMessage());
        }
    }

    private void cleanOldBackups() {
        File[] backups = backupDir.listFiles((dir, name) -> name.startsWith("backup_") && name.endsWith(".zip"));
        if (backups != null && backups.length > maxBackups) {
            Arrays.sort(backups, Comparator.comparingLong(File::lastModified));

            for (int i = 0; i < backups.length - maxBackups; i++) {
                if (backups[i].delete()) {
                    Bukkit.getLogger().info("§e[SurvivalCore] Deleted old backup: " + backups[i].getName());
                }
            }
        }
    }

    public List<String> getBackupList() {
        File[] backups = backupDir.listFiles((dir, name) -> name.startsWith("backup_"));
        if (backups == null) return new ArrayList<>();

        List<String> backupNames = new ArrayList<>();
        for (File backup : backups) {
            backupNames.add(backup.getName());
        }
        backupNames.sort(Collections.reverseOrder());
        return backupNames;
    }

    public void setBackupIntervalMinutes(long minutes) {
        this.backupIntervalMinutes = minutes;
        if (autoBackupTask != null) {
            stopAutoBackup();
            startAutoBackup();
        }
    }

    public long getBackupIntervalMinutes() {
        return backupIntervalMinutes;
    }
}
