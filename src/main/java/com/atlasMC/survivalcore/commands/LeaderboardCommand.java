package com.atlasMC.survivalcore.commands;

<<<<<<< HEAD
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
=======
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
 * v1.0.25+: Mejorado con paginación y más tipos de rankings
 */
public class LeaderboardCommand implements CommandExecutor, TabExecutor {

    private static final int DEFAULT_LIMIT = 10;
    private static final int PAGE_SIZE = 10;
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

        String type = args[0].toLowerCase();
>>>>>>> 7c0a3aef7a38e92568532e5d4f6f1b1fd2222ab9
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
<<<<<<< HEAD
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
=======
                page = 1;
            }
        }

        switch (type) {
            case "money" -> showMoneyLeaderboard(player, page);
            case "arena" -> showArenaLeaderboard(player, page);
            case "clan" -> showClanLeaderboard(player, page);
            case "skill" -> {
                if (args.length < 2) {
                    showSkillHelp(sender);
                } else {
                    showSkillLeaderboard(player, args[1], 1);
                }
            }
            case "job" -> {
                if (args.length < 2) {
                    showJobHelp(sender);
                } else {
                    showJobLeaderboard(player, args[1], 1);
                }
            }
            default -> showHelp(sender);
        }

        return true;
    }

    private void showMoneyLeaderboard(Player player, int page) {
        leaderboardManager.getLeaderboardPage("money", page, PAGE_SIZE, entries -> {
            player.sendMessage("§6═══ TOP 10 Jugadores Ricos (Página " + page + ") ═══");
            if (entries.isEmpty()) {
                player.sendMessage("§7Sin datos disponibles");
                return;
            }
            for (LeaderboardEntry entry : entries) {
                player.sendMessage(entry.toString());
            }
            leaderboardManager.getPlayerRank(player.getUniqueId(), "money", rank ->
                    player.sendMessage(String.format("§7Tu posición: §b#%d", rank))
            );
        });
    }

    private void showArenaLeaderboard(Player player, int page) {
        leaderboardManager.getArenaLeaderboard(DEFAULT_LIMIT, entries -> {
            player.sendMessage("§6═══ TOP 10 Arenas (ELO) ═══");
            if (entries.isEmpty()) {
                player.sendMessage("§7Sin datos disponibles");
                return;
            }
            for (LeaderboardEntry entry : entries) {
                String record = String.format("§6#%d §e%s §7→ §bELO: %,d", 
                    entry.rank, entry.playerName, entry.value);
                player.sendMessage(record);
            }
            leaderboardManager.getPlayerRank(player.getUniqueId(), "arena", rank ->
                    player.sendMessage(String.format("§7Tu posición: §b#%d", rank))
            );
        });
    }

    private void showClanLeaderboard(Player player, int page) {
        leaderboardManager.getClanLeaderboard(DEFAULT_LIMIT, entries -> {
            player.sendMessage("§6═══ TOP 10 Clanes (Poder) ═══");
            if (entries.isEmpty()) {
                player.sendMessage("§7Sin datos disponibles");
                return;
            }
            for (LeaderboardEntry entry : entries) {
                String record = String.format("§6#%d §e%s §7→ §bPoder: %,d", 
                    entry.rank, entry.playerName, entry.value);
                player.sendMessage(record);
            }
        });
    }

    private void showSkillLeaderboard(Player player, String skillType, int page) {
        player.sendMessage("§6═══ TOP 10 Skills: " + skillType.toUpperCase() + " ═══");
        player.sendMessage("§7Usar: /leaderboard skill <tipo>");
        showSkillHelp(player);
    }

    private void showJobLeaderboard(Player player, String jobType, int page) {
        player.sendMessage("§6═══ TOP 10 Jobs: " + jobType.toUpperCase() + " ═══");
        player.sendMessage("§7Usar: /leaderboard job <tipo>");
        showJobHelp(player);
    }

    private void showSkillHelp(CommandSender sender) {
        sender.sendMessage("§6Tipos de Skills:");
        sender.sendMessage("§7  - mining §7/ farming §7/ fishing §7/ combat");
    }

    private void showJobHelp(CommandSender sender) {
        sender.sendMessage("§6Tipos de Jobs:");
        sender.sendMessage("§7  - miner §7/ farmer §7/ fisherman §7/ warrior");
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6═══ Comandos Leaderboard ═══");
        sender.sendMessage("§e/leaderboard money [página] §7- Top jugadores ricos");
        sender.sendMessage("§e/leaderboard arena [página] §7- Top arenas (ELO)");
        sender.sendMessage("§e/leaderboard clan [página] §7- Top clanes");
        sender.sendMessage("§e/leaderboard skill <tipo> §7- Ranking de skills");
        sender.sendMessage("§e/leaderboard job <tipo> §7- Ranking de jobs");
        sender.sendMessage("§7Alias: /lb, /ranking");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("money");
            completions.add("arena");
            completions.add("clan");
            completions.add("skill");
            completions.add("job");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("skill")) {
                completions.add("mining");
                completions.add("farming");
                completions.add("fishing");
                completions.add("combat");
            } else if (args[0].equalsIgnoreCase("job")) {
                completions.add("miner");
                completions.add("farmer");
                completions.add("fisherman");
                completions.add("warrior");
            } else {
                completions.add("1");
                completions.add("2");
                completions.add("3");
            }
        }
        return completions;
    }
}
>>>>>>> 7c0a3aef7a38e92568532e5d4f6f1b1fd2222ab9
