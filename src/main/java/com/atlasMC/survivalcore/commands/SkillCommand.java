package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.ISkillManager;
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

public class SkillCommand implements CommandExecutor, TabExecutor {

    private final ISkillManager skillManager;
    private final MenuManager menuManager;

    public SkillCommand(ISkillManager skillManager, MenuManager menuManager) {
        this.skillManager = skillManager;
        this.menuManager = menuManager;
        createSkillMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "skills");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "menu" -> menuManager.openMenu(player, "skills");
            case "list" -> showSkills(player);
            case "info" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /skill info <skillID>");
                } else {
                    showSkillInfo(player, args[1]);
                }
            }
            case "stats" -> showStats(player);
            default -> menuManager.openMenu(player, "skills");
        }
        return true;
    }

    private void showSkills(Player player) {
        player.sendMessage("§6=== Tus Skills ===");
        player.sendMessage("§cCombate §f- Nivel §b1 §7(0/1000 XP)");
        player.sendMessage("§8Minería §f- Nivel §b5 §7(250/2000 XP)");
        player.sendMessage("§2Recolección §f- Nivel §b3 §7(500/1500 XP)");
        player.sendMessage("§bPesca §f- Nivel §b2 §7(100/1100 XP)");
        player.sendMessage("§6Granjería §f- Nivel §b4 §7(800/1800 XP)");
        player.sendMessage("§dArtesanía §f- Nivel §b2 §7(200/1100 XP)");
    }

    private void showSkillInfo(Player player, String skillId) {
        player.sendMessage("§6=== Información de Skill ===");
        player.sendMessage("§eSkill: §f" + skillId);
        player.sendMessage("§eNivel: §b5");
        player.sendMessage("§eExperiencia: §6250/2000 §7(12.5%)");
        player.sendMessage("§eBonus: §a+10% de ganancias");
    }

    private void showStats(Player player) {
        player.sendMessage("§6=== Estadísticas de Skills ===");
        int totalLevel = 17;
        int totalXp = 1850;
        player.sendMessage(String.format("§eNivel Total: §b%d", totalLevel));
        player.sendMessage(String.format("§eExperiencia Total: §6%d", totalXp));
        player.sendMessage("§7Sube skills realizando actividades relacionadas");
    }

    private void createSkillMenu() {
        MenuData menu = MenuFactory.createMenu("skills", "§6Árbol de Skills", 54);

        // Primera fila - Skills principales
        MenuFactory.addMenuSlot(menu, 0, Material.DIAMOND_SWORD, "§cCombate",
                "Nivel 1", "§7Mejora en batalla");

        MenuFactory.addMenuSlot(menu, 1, Material.DIAMOND_PICKAXE, "§8Minería",
                "Nivel 5", "§7Más minerales");

        MenuFactory.addMenuSlot(menu, 2, Material.GOLDEN_AXE, "§2Recolección",
                "Nivel 3", "§7Más velocidad");

        MenuFactory.addMenuSlot(menu, 3, Material.FISHING_ROD, "§bPesca",
                "Nivel 2", "§7Mejor pesca");

        MenuFactory.addMenuSlot(menu, 4, Material.WOODEN_HOE, "§6Granjería",
                "Nivel 4", "§7Mejor cosecha");

        MenuFactory.addMenuSlot(menu, 5, Material.CRAFTING_TABLE, "§dArtesanía",
                "Nivel 2", "§7Mejores items");

        // Segunda fila - Información
        MenuFactory.addMenuSlot(menu, 18, Material.BOOK, "§eProgresión",
                "Ver progress", "§7Tu avance en skills");

        MenuFactory.addMenuSlot(menu, 19, Material.EXPERIENCE_BOTTLE, "§eRecompensas",
                "Ver bonificaciones", "§7Bonus por nivel");

        MenuFactory.addMenuSlot(menu, 26, Material.BARRIER, "§cCerrar",
                "Cerrar", "§7Click para cerrar");

        menuManager.registerMenu("skills", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("menu", "list", "info", "stats");
        }
        return List.of();
    }
}
