package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.SurvivalCorePlugin;
import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.menu.MenuAction;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JobCommand implements CommandExecutor, TabExecutor {

    private final IJobManager jobManager;
    private final PlayerCache playerCache;
    private final MenuManager menuManager;

    public JobCommand(IJobManager jobManager, PlayerCache playerCache) {
        this.jobManager = jobManager;
        this.playerCache = playerCache;
        this.menuManager = SurvivalCorePlugin.getInstance().getMenuManager();
        createJobMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando solo puede ser usado por jugadores.");
            return true;
        }

        PlayerProfile profile = playerCache.get(player.getUniqueId());
        if (profile == null) {
            player.sendMessage("§cTu perfil no fue cargado.");
            return true;
        }

        if (args.length == 0) {
            openJobMenu(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "menu" -> openJobMenu(player);
            case "list" -> showJobs(player);
            case "set" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /job set <nombre>");
                } else {
                    setJob(player, args[1], profile);
                }
            }
            case "info" -> showJobInfo(player, profile);
            default -> openJobMenu(player);
        }
        return true;
    }

    private void openJobMenu(Player player) {
        menuManager.openMenu(player, "jobs");
    }

    private void createJobMenu() {
        MenuData menu = MenuFactory.createMenu("jobs", "§6Trabajos Disponibles", 27);

        int slot = 0;
        for (JobType type : JobType.values()) {
            String material = getJobMaterial(type);
            String lore1 = "§7Click para seleccionar";
            String lore2 = "§7Usa /job info para ver stats";

            MenuAction action = MenuAction.command("job set " + type.name().toLowerCase());
            MenuFactory.addMenuSlot(menu, slot, Material.valueOf(material), "§e" + type.name(),
                    action, lore1, lore2);

            slot++;
        }

        // Info slot
        MenuFactory.addMenuSlot(menu, 10, Material.BOOK, "§bMi Trabajo",
                MenuAction.command("job info"), "§7Ver tu trabajo actual");

        menuManager.registerMenu("jobs", menu);
    }

    private String getJobMaterial(JobType type) {
        return switch (type) {
            case MINER -> "DIAMOND_PICKAXE";
            case FISHERMAN -> "FISHING_ROD";
            case LUMBERJACK -> "IRON_AXE";
            case FARMER -> "WOODEN_HOE";
            case HUNTER -> "BOW";
            default -> "PAPER";
        };
    }

    private void showJobs(Player player) {
        player.sendMessage("§6=== Trabajos disponibles ===");
        for (JobType type : JobType.values()) {
            player.sendMessage("§e• §f" + type.name() + " §7- Usa: §f/job set " + type.name().toLowerCase());
        }
    }

    private void setJob(Player player, String jobName, PlayerProfile profile) {
        try {
            JobType type = JobType.valueOf(jobName.toUpperCase());
            jobManager.joinJob(player.getUniqueId(), type);
            profile.setCurrentJob(type.name());
            player.sendMessage("§a✓ Te has unido al trabajo §f" + type.name());
            player.sendMessage("§7Escribe: §f/job info §7para ver tus estadísticas");
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cTrabajo no encontrado: " + jobName);
        }
    }

    private void showJobInfo(Player player, PlayerProfile profile) {
        if (profile.getCurrentJob() == null) {
            player.sendMessage("§cNo tienes un trabajo activo.");
            player.sendMessage("§7Escribe: §f/job set <nombre> §7para escoger uno");
            return;
        }

        player.sendMessage("§6=== Información de Tu Trabajo ===");
        player.sendMessage("§eTrabajo: §f" + profile.getCurrentJob());
        player.sendMessage("§eNivel: §b" + profile.getJobLevel());
        player.sendMessage("§eExperiencia: §6" + profile.getJobExp() + " XP");
        player.sendMessage("§7─────────────────────────────");
        player.sendMessage("§7Gana XP realizando actividades específicas");
        player.sendMessage("§7de tu trabajo. ¡Sigue adelante!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("menu", "list", "set", "info");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            List<String> jobs = new ArrayList<>();
            for (JobType type : JobType.values()) {
                jobs.add(type.name().toLowerCase());
            }
            return jobs;
        }
        return List.of();
    }
}
