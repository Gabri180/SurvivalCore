package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.scheduler.BackupScheduler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class BackupCommand implements CommandExecutor, TabExecutor {

    private final BackupScheduler backupScheduler;

    public BackupCommand(BackupScheduler backupScheduler) {
        this.backupScheduler = backupScheduler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("survivalcore.admin")) {
            sender.sendMessage("§cNo tienes permiso");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("now")) {
            sender.sendMessage("§e⏳ Ejecutando backup de base de datos...");
            backupScheduler.performBackupNow();
            sender.sendMessage("§a✓ Backup iniciado (se completará en segundo plano)");
            return true;
        }

        sender.sendMessage("§cUso: /sc backup [now]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("now");
        }
        return List.of();
    }
}
