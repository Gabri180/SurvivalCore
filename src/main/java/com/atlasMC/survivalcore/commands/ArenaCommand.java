package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IArenaManager;
import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.menu.MenuAction;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.Arena;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaCommand implements CommandExecutor, TabExecutor {

    private final IArenaManager arenaManager;
    private final EconomyAPI economyAPI;
    private final PlayerCache playerCache;
    private final MenuManager menuManager;

    public ArenaCommand(IArenaManager arenaManager, EconomyAPI economyAPI, PlayerCache playerCache, MenuManager menuManager) {
        this.arenaManager = arenaManager;
        this.economyAPI = economyAPI;
        this.playerCache = playerCache;
        this.menuManager = menuManager;
        createArenaMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "arenas");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /arena join <id>");
                } else {
                    joinArena(player, args[1]);
                }
            }
            case "leave" -> leaveArena(player);
            case "info" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /arena info <id>");
                } else {
                    showArenaInfo(player, args[1]);
                }
            }
            case "ranking" -> showRanking(player);
            default -> menuManager.openMenu(player, "arenas");
        }
        return true;
    }

    private void joinArena(Player player, String arenaId) {
        Arena arena = arenaManager.getArena(arenaId);
        if (arena == null) {
            player.sendMessage("§cArena no encontrada: " + arenaId);
            return;
        }

        if (!arena.isActive()) {
            player.sendMessage("§cEsta arena no está activa.");
            return;
        }

        if (arena.getParticipants().size() >= arena.getMaxPlayers()) {
            player.sendMessage("§cLa arena está llena.");
            return;
        }

        long balance = economyAPI.getBalance(player.getUniqueId());
        if (balance < arena.getEntryFee()) {
            player.sendMessage(String.format("§cNo tienes suficiente dinero.\n§7Requerido: §6$%d\n§7Tienes: §6$%d", arena.getEntryFee(), balance));
            return;
        }

        economyAPI.removeBalance(player.getUniqueId(), arena.getEntryFee());
        arenaManager.joinArena(player.getUniqueId(), arenaId);
        player.sendMessage(String.format("§a✓ ¡Te uniste a %s!\n§7Entrada: §6$%d | §7Premio: §a$%d", arena.getName(), arena.getEntryFee(), arena.getWinReward()));
    }

    private void leaveArena(Player player) {
        Arena arena = arenaManager.getPlayerArena(player.getUniqueId());
        if (arena == null) {
            player.sendMessage("§cNo estás en una arena.");
            return;
        }

        arenaManager.leaveArena(player.getUniqueId());
        player.sendMessage("§c✗ Saliste de la arena " + arena.getName());
    }

    private void showArenaInfo(Player player, String arenaId) {
        Arena arena = arenaManager.getArena(arenaId);
        if (arena == null) {
            player.sendMessage("§cArena no encontrada: " + arenaId);
            return;
        }

        int rank = arenaManager.getArenaRank(player.getUniqueId(), arenaId);
        int wins = arenaManager.getArenaWins(player.getUniqueId(), arenaId);
        int losses = arenaManager.getArenaLosses(player.getUniqueId(), arenaId);

        player.sendMessage("§6=== Información de Arena ===");
        player.sendMessage("§eNombre: §f" + arena.getName());
        player.sendMessage(String.format("§eJugadores: §b%d/%d", arena.getParticipants().size(), arena.getMaxPlayers()));
        player.sendMessage(String.format("§eEntrada: §6$%d", arena.getEntryFee()));
        player.sendMessage(String.format("§ePremio: §a$%d", arena.getWinReward()));
        player.sendMessage("§6=== Tu Estadística ===");
        player.sendMessage(String.format("§eRanking: §b#%d", rank));
        player.sendMessage(String.format("§eVictorias: §a%d", wins));
        player.sendMessage(String.format("§eDerrota: §c%d", losses));
    }

    private void showRanking(Player player) {
        player.sendMessage("§6=== Top 10 Ranking de Arena ===");
        // TODO: Implementar ranking del servidor (requiere método en ArenaManager)
        player.sendMessage("§7Coming soon...");
    }

    private void createArenaMenu() {
        MenuData menu = MenuFactory.createMenu("arenas", "§6Arenas PvP", 27);

        int slot = 0;
        for (Arena arena : arenaManager.getAllArenas()) {
            int players = arena.getParticipants().size();
            int maxPlayers = arena.getMaxPlayers();
            String lore1 = "§7Click para unirse";
            String lore2 = String.format("§7Jugadores: §b%d/%d", players, maxPlayers);
            String lore3 = String.format("§7Entrada: §6$%d", arena.getEntryFee());
            String lore4 = String.format("§7Premio: §a$%d", arena.getWinReward());

            MenuAction action = MenuAction.command("arena join " + arena.getId());
            MenuFactory.addMenuSlot(menu, slot, Material.DIAMOND_SWORD, "§e" + arena.getName(),
                    action, lore1, lore2, lore3, lore4);

            slot++;
            if (slot >= 9) break;
        }

        menuManager.registerMenu("arenas", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("join", "leave", "info", "ranking");
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("info"))) {
            List<String> arenaIds = new ArrayList<>();
            for (Arena arena : arenaManager.getAllArenas()) {
                arenaIds.add(arena.getId());
            }
            return arenaIds;
        }
        return List.of();
    }
}
