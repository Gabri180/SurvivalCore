package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.SurvivalCorePlugin;
import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.enums.JobType;
import com.atlasMC.survivalcore.menu.MenuAction;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JobCommand implements CommandExecutor, TabExecutor {

    private final IJobManager jobManager;
    private final PlayerCache playerCache;

    public JobCommand(IJobManager jobManager, PlayerCache playerCache) {
        this.jobManager = jobManager;
        this.playerCache = playerCache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando solo puede ser usado por jugadores.");
            return true;
        }

        Player player = (Player) sender;

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
        MenuManager menuManager = SurvivalCorePlugin.getInstance().getMenuManager();

        MenuData menuData = menuManager.getMenu("jobs");
        if (menuData != null) {
            menuManager.openMenu(player, "jobs");
            return;
        }

        Inventory menu = org.bukkit.Bukkit.createInventory(null, 27, "§6Trabajos - Selecciona tu Trabajo");

        int slot = 0;
        for (JobType type : JobType.values()) {
            addJobItem(menu, slot, type);
            slot++;
        }

        player.openInventory(menu);
    }

    private void addJobItem(Inventory inv, int slot, JobType type) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e" + type.name());
            List<String> lore = new ArrayList<>();
            lore.add("§7Click para seleccionar");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        inv.setItem(slot, item);
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
            player.sendMessage("§aTe has unido al trabajo §f" + type.name());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cTrabajo no encontrado: " + jobName);
        }
    }

    private void showJobInfo(Player player, PlayerProfile profile) {
        if (profile.getCurrentJob() == null) {
            player.sendMessage("§cNo tienes un trabajo activo.");
            return;
        }
        player.sendMessage("§6=== Tu trabajo ===");
        player.sendMessage("§eTrabajo: §f" + profile.getCurrentJob());
        player.sendMessage("§eNivel: §f" + profile.getJobLevel());
        player.sendMessage("§eExperiencia: §f" + profile.getJobExp());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("list", "set", "info");
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
