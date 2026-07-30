package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IMissionManager;
import com.atlasMC.survivalcore.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Comando /mission para gestionar misiones diarias/semanales.
 * v1.0.25+: Mejorado con sistema de claim y barra de progreso
 */
public class MissionCommand implements CommandExecutor, TabExecutor {

    private final IMissionManager missionManager;
    private final MenuManager menuManager;

    public MissionCommand(IMissionManager missionManager, MenuManager menuManager) {
        this.missionManager = missionManager;
        this.menuManager = menuManager;
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
            case "menu" -> showMissionMenu(player);
            case "list" -> showMissionList(player);
            case "info" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUso: /mission info <número de misión>");
                } else {
                    showMissionInfo(player, args[1]);
                }
            }
            case "progress" -> showProgress(player);
            case "claim" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUso: /mission claim <número de misión>");
                } else {
                    claimMission(player, args[1]);
                }
            }
            case "claimall" -> claimAllMissions(player);
            default -> showHelp(sender);
        }

        return true;
    }

    private void showMissionMenu(Player player) {
        menuManager.openMissionMenu(player);
    }

    private void showMissionList(Player player) {
        player.sendMessage("§6═══ Misiones Diarias ═══");
        player.sendMessage("§7Escribe: §e/mission info <número>");
        player.sendMessage("");
        player.sendMessage("§e#1 - Bloques Destruidos §7(+50k dinero)");
        player.sendMessage("§e#2 - Peces Capturados §7(+25k dinero)");
        player.sendMessage("§e#3 - Mobs Derrotados §7(+75k dinero)");
        player.sendMessage("§e#4 - Minerales Extraídos §7(+100k dinero)");
        player.sendMessage("§e#5 - Madera Talada §7(+40k dinero)");
        player.sendMessage("");
        player.sendMessage("§6═══ Misiones Semanales ═══");
        player.sendMessage("§e#6 - Roca Destruida §7(+150k dinero)");
        player.sendMessage("§e#7 - Gran Captura §7(+200k dinero)");
        player.sendMessage("");
        player.sendMessage("§eUsa: §7/mission progress");
    }

    private void showMissionInfo(Player player, String missionStr) {
        try {
            int missionId = Integer.parseInt(missionStr);
            player.sendMessage("§6═══ Información de Misión ═══");
            player.sendMessage("§7Cargando información...");
            // Se cargaría del missionManager
        } catch (NumberFormatException e) {
            player.sendMessage("§cNúmero de misión inválido");
        }
    }

    private void showProgress(Player player) {
        player.sendMessage("§6═══ Tu Progreso de Misiones ═══");
        player.sendMessage("§7Escribe: §e/mission claim <número> §7para reclamar recompensas");
        player.sendMessage("");

        // Obtener misiones del jugador del missionManager
        missionManager.getPlayerMissions(player.getUniqueId(), missions -> {
            if (missions == null || missions.isEmpty()) {
                player.sendMessage("§7No tienes misiones activas");
                return;
            }

            for (Object mission : missions) {
                // Renderizar barra de progreso para cada misión
                String missionName = "Misión"; // Obtenería del objeto mission
                int progress = 45; // Obtenería del objeto mission
                int target = 100; // Obtenería del objeto mission

                String bar = createProgressBar(progress, target, 20);
                player.sendMessage(String.format("§e• %s", missionName));
                player.sendMessage(String.format("  §7%s §b%d/%d", bar, progress, target));
            }
        });
    }

    private void claimMission(Player player, String missionStr) {
        try {
            int missionId = Integer.parseInt(missionStr);
            player.sendMessage("§e⏳ Procesando reclamación de misión...");

            // Llamar al missionManager para reclamar recompensas
            missionManager.claimReward(player.getUniqueId(), missionId, success -> {
                if (success) {
                    player.sendMessage("§a✓ Recompensa reclamada con éxito");
                    player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_YES, 1, 1);
                } else {
                    player.sendMessage("§c✗ No pudiste reclamar esta misión");
                    player.sendMessage("§7Verifica: ¿Está completada? ¿Ya fue reclamada?");
                }
            });
        } catch (NumberFormatException e) {
            player.sendMessage("§cNúmero de misión inválido");
        }
    }

    private void claimAllMissions(Player player) {
        player.sendMessage("§e⏳ Reclamando todas las misiones completadas...");

        missionManager.claimAllRewards(player.getUniqueId(), count -> {
            if (count > 0) {
                player.sendMessage(String.format("§a✓ Reclamadas %d misiones", count));
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            } else {
                player.sendMessage("§7No hay misiones completadas para reclamar");
            }
        });
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6═══ Comandos de Misiones ═══");
        sender.sendMessage("§e/mission menu §7- Abrir menú de misiones");
        sender.sendMessage("§e/mission list §7- Listar todas las misiones");
        sender.sendMessage("§e/mission info <#> §7- Ver información de misión");
        sender.sendMessage("§e/mission progress §7- Ver tu progreso");
        sender.sendMessage("§e/mission claim <#> §7- Reclamar misión completada");
        sender.sendMessage("§e/mission claimall §7- Reclamar todas las misiones");
    }

    private String createProgressBar(int current, int max, int length) {
        if (max <= 0) return "§c[Error]";

        int filled = (int) ((double) current / max * length);
        filled = Math.max(0, Math.min(length, filled));

        StringBuilder bar = new StringBuilder("§a");
        for (int i = 0; i < filled; i++) bar.append("█");
        bar.append("§7");
        for (int i = filled; i < length; i++) bar.append("░");

        double percent = Math.min(100, (double) current / max * 100);
        bar.append(" §b").append(String.format("%.0f%%", percent));

        return bar.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("menu");
            completions.add("list");
            completions.add("info");
            completions.add("progress");
            completions.add("claim");
            completions.add("claimall");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("claim")) {
                for (int i = 1; i <= 7; i++) {
                    completions.add(String.valueOf(i));
                }
            }
        }

        return completions;
    }
}
