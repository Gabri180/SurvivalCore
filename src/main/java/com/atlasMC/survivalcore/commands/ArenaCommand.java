package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IArenaManager;
import com.atlasMC.survivalcore.menu.MenuAction;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.Arena;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class ArenaCommand implements CommandExecutor, TabExecutor {

    private final IArenaManager arenaManager;
    private final MenuManager menuManager;

    public ArenaCommand(IArenaManager arenaManager, MenuManager menuManager) {
        this.arenaManager = arenaManager;
        this.menuManager = menuManager;
        createArenaMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        menuManager.openMenu(player, "arenas");
        return true;
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
        return List.of();
    }
}
