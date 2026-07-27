package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.managers.LeaderboardManager;
import com.atlasMC.survivalcore.managers.LeaderboardManager.LeaderboardEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Comando /leaderboard para ver rankings globales.
 * v1.0.20+
 */
public class LeaderboardCommand implements CommandExecutor, TabExecutor {

    private static final int DEFAULT_LIMIT = 10;
    private final LeaderboardManager leaderboardManager;

    public LeaderboardCommand(LeaderboardManager leaderboardManager) {
        this.leaderboardManager = leaderboardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Solo jugadores");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "money" -> showMoneyLeaderboard(player);
            case "arena" -> showArenaLeaderboard(player);
            case "clan" -> showClanLeaderboard(player);
            default -> showHelp(sender);
        }

        return true;
    }

    private void showMoneyLeaderboard(Player player) {
        leaderboardManager.getMoneyLeaderboard(DEFAULT_LIMIT, entries -> {
            player.sendMessage("§6═══ TOP 10 Jugadores Ricos ═══");
            for (LeaderboardEntry entry : entries) {
                player.sendMessage(entry.toString());
            }
            leaderboardManager.getPlayerRank(player.getUniqueId(), "money", rank ->
                    player.sendMessage(String.format("§7Tu posición: §b#%d", rank))
            );
        });
    }

    private void showArenaLeaderboard(Player player) {
        leaderboardManager.getArenaLeaderboard(DEFAULT_LIMIT, entries -> {
            player.sendMessage("§6═══ TOP 10 Arenas (ELO) ═══");
            for (LeaderboardEntry entry : entries) {
                player.sendMessage(entry.toString());
            }
            leaderboardManager.getPlayerRank(player.getUniqueId(), "arena", rank ->
                    player.sendMessage(String.format("§7Tu posición: §b#%d", rank))
            );
        });
    }

    private void showClanLeaderboard(Player player) {
        leaderboardManager.getClanLeaderboard(DEFAULT_LIMIT, entries -> {
            player.sendMessage("§6═══ TOP 10 Clanes (Poder) ═══");
            for (LeaderboardEntry entry : entries) {
                player.sendMessage(entry.toString());
            }
        });
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6═══ Comandos Leaderboard ═══");
        sender.sendMessage("§e/leaderboard money §7- Top 10 ricos");
        sender.sendMessage("§e/leaderboard arena §7- Top 10 arenas");
        sender.sendMessage("§e/leaderboard clan §7- Top 10 clanes");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("money");
            completions.add("arena");
            completions.add("clan");
        }
        return completions;
    }
}
