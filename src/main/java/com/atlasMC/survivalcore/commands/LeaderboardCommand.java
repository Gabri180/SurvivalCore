package com.atlasMC.survivalcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaderboardCommand implements CommandExecutor, TabCompleter {

    public LeaderboardCommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String type = args[0].toUpperCase();
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cPágina inválida");
                return true;
            }
        }

        displayLeaderboard(sender, type, page);
        return true;
    }

    private void displayLeaderboard(CommandSender sender, String type, int page) {
        sender.sendMessage("");
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e" + type + "§6 - Página " + page + "§6                 ║");
        sender.sendMessage("§6╠════════════════════════════════════════╣");
        sender.sendMessage("§6║                                        ║");
        sender.sendMessage("§f #1 - Player1                    §8| §e1000000");
        sender.sendMessage("§7 #2 - Player2                    §8| §e950000");
        sender.sendMessage("§c #3 - Player3                    §8| §e900000");
        sender.sendMessage("§f #4 - Player4                    §8| §e850000");
        sender.sendMessage("§f #5 - Player5                    §8| §e800000");
        sender.sendMessage("§f #6 - Player6                    §8| §e750000");
        sender.sendMessage("§f #7 - Player7                    §8| §e700000");
        sender.sendMessage("§f #8 - Player8                    §8| §e650000");
        sender.sendMessage("§f #9 - Player9                    §8| §e600000");
        sender.sendMessage("§f #10 - Player10                   §8| §e550000");
        sender.sendMessage("§6║                                        ║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        if (sender instanceof Player player) {
            sender.sendMessage("§eTu posición: §f#15 - " + player.getName() + " (450000)");
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== Leaderboards ===");
        sender.sendMessage("§e/leaderboard money [página]");
        sender.sendMessage("§e/leaderboard arena [página]");
        sender.sendMessage("§e/leaderboard clan [página]");
        sender.sendMessage("§e/leaderboard skill [página]");
        sender.sendMessage("§e/leaderboard job [página]");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                       @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("money", "arena", "clan", "skill", "job");
        }
        if (args.length == 2) {
            return Arrays.asList("1", "2", "3", "4", "5");
        }
        return new ArrayList<>();
    }
}