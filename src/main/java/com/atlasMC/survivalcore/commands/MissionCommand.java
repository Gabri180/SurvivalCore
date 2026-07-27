package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IMissionManager;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class MissionCommand implements CommandExecutor, TabExecutor {

    private final IMissionManager missionManager;
    private final MenuManager menuManager;

    public MissionCommand(IMissionManager missionManager, MenuManager menuManager) {
        this.missionManager = missionManager;
        this.menuManager = menuManager;
        createMissionMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "missions");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "menu" -> menuManager.openMenu(player, "missions");
            case "list" -> showMissions(player);
            case "info" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /mission info <misionID>");
                } else {
                    showMissionInfo(player, args[1]);
                }
            }
            case "progress" -> showProgress(player);
            default -> menuManager.openMenu(player, "missions");
        }
        return true;
    }

    private void showMissions(Player player) {
        player.sendMessage("§6=== Tus Misiones ===");
        player.sendMessage("§7Escribe: §f/mission info <id> §7para más detalles");
        player.sendMessage("§7Las misiones se completan automáticamente");
    }

    private void showMissionInfo(Player player, String missionId) {
        player.sendMessage("§6=== Información de Misión ===");
        player.sendMessage("§eMisión: §f" + missionId);
        player.sendMessage("§eRecompensa: §6$5000");
        player.sendMessage("§eProgreso: §a50%");
    }

    private void showProgress(Player player) {
        player.sendMessage("§6=== Tu Progreso Diario ===");
        player.sendMessage("§e• Matar 10 Zombies §a█████░░░░░░ 50%");
        player.sendMessage("§e• Recolectar 64 Madera §a██████████ 100% ✓");
        player.sendMessage("§e• Pescar 5 Items §a██░░░░░░░░░ 20%");
    }

    private void createMissionMenu() {
        MenuData menu = MenuFactory.createMenu("missions", "§6Misiones Diarias", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.DIAMOND_SWORD, "§eKill Missions",
                "Matar mobs", "§7Gana dinero matando");

        MenuFactory.addMenuSlot(menu, 1, Material.CHEST, "§eCollect Missions",
                "Recolectar items", "§7Junta items específicos");

        MenuFactory.addMenuSlot(menu, 2, Material.IRON_PICKAXE, "§eMine Missions",
                "Minar bloques", "§7Extrae ciertos minerales");

        MenuFactory.addMenuSlot(menu, 3, Material.FISHING_ROD, "§eFishing Missions",
                "Pescar", "§7Pesca los mejores items");

        MenuFactory.addMenuSlot(menu, 4, Material.CRAFTING_TABLE, "§eCraft Missions",
                "Craftear items", "§7Crea items específicos");

        MenuFactory.addMenuSlot(menu, 10, Material.BOOK, "§eProgreso Semanal",
                "Ver progreso", "§7Ver tu avance esta semana");

        MenuFactory.addMenuSlot(menu, 26, Material.BARRIER, "§cCerrar",
                "Cerrar", "§7Click para cerrar");

        menuManager.registerMenu("missions", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("menu", "list", "info", "progress");
        }
        return List.of();
    }
}
