package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IBountyManager;
import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.Bounty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BountyCommand implements CommandExecutor, TabExecutor {

    private final IBountyManager bountyManager;
    private final EconomyAPI economyAPI;
    private final MenuManager menuManager;
    private static final long MIN_BOUNTY = 1000;
    private static final long MAX_BOUNTY = 100000;

    public BountyCommand(IBountyManager bountyManager, EconomyAPI economyAPI, MenuManager menuManager) {
        this.bountyManager = bountyManager;
        this.economyAPI = economyAPI;
        this.menuManager = menuManager;
        createBountyMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "bounty");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /bounty create <jugador> <cantidad>");
                } else {
                    try {
                        long amount = Long.parseLong(args[2]);
                        createBounty(player, args[1], amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cCantidad inválida: " + args[2]);
                    }
                }
            }
            case "list" -> listBounties(player);
            case "mylist" -> listMyBounties(player);
            case "history" -> showHistory(player);
            default -> menuManager.openMenu(player, "bounty");
        }
        return true;
    }

    private void createBounty(Player player, String targetName, long amount) {
        if (amount < MIN_BOUNTY || amount > MAX_BOUNTY) {
            player.sendMessage(String.format("§cRecompensa debe estar entre §6$%d §cy §6$%d§c.", MIN_BOUNTY, MAX_BOUNTY));
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cJugador no encontrado: " + targetName);
            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage("§cNo puedes poner recompensa sobre ti mismo.");
            return;
        }

        long balance = economyAPI.getBalance(player.getUniqueId());
        if (balance < amount) {
            player.sendMessage(String.format("§cNo tienes suficiente dinero.\n§7Requerido: §6$%d\n§7Tienes: §6$%d", amount, balance));
            return;
        }

        economyAPI.removeBalance(player.getUniqueId(), amount);
        Bounty bounty = bountyManager.setBounty(player.getUniqueId(), target.getUniqueId(), amount);

        if (bounty != null) {
            player.sendMessage(String.format("§a✓ ¡Recompensa de §6$%d §aen %s!", amount, targetName));
            target.sendMessage(String.format("§c⚠ ¡Hay una recompensa sobre ti!\n§7Precio: §6$%d", amount));
        } else {
            economyAPI.addBalance(player.getUniqueId(), amount);
            player.sendMessage("§cError al crear la recompensa.");
        }
    }

    private void listBounties(Player player) {
        Collection<Bounty> bounties = bountyManager.getActiveBounties();
        if (bounties.isEmpty()) {
            player.sendMessage("§cNo hay recompensas activas.");
            return;
        }

        player.sendMessage("§6=== Recompensas Activas ===");
        int count = 0;
        for (Bounty bounty : bounties) {
            if (count >= 10) break;
            Player target = Bukkit.getPlayer(bounty.getTargetUuid());
            String targetName = target != null ? target.getName() : "Desconectado";
            player.sendMessage(String.format("§e[ID: §b%d§e] %s - §6$%d", bounty.getId(), targetName, bounty.getReward()));
            count++;
        }
    }

    private void listMyBounties(Player player) {
        Collection<Bounty> bounties = bountyManager.getBountiesCreatedBy(player.getUniqueId());
        if (bounties.isEmpty()) {
            player.sendMessage("§cNo has creado recompensas.");
            return;
        }

        player.sendMessage("§6=== Mis Recompensas ===");
        for (Bounty bounty : bounties) {
            Player target = Bukkit.getPlayer(bounty.getTargetUuid());
            String targetName = target != null ? target.getName() : "Desconectado";
            String status = bounty.isClaimed() ? "§cCobrada" : "§aActiva";
            player.sendMessage(String.format("§e[ID: §b%d§e] %s - §6$%d §7%s", bounty.getId(), targetName, bounty.getReward(), status));
        }
    }

    private void showHistory(Player player) {
        // TODO: Implementar historial de recompensas cobradas
        player.sendMessage("§7Historial de recompensas - Coming soon...");
    }

    private void createBountyMenu() {
        MenuData menu = MenuFactory.createMenu("bounty", "§6Recompensas por Cabeza", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.REDSTONE, "§eRecompensas Activas",
                "bounty list", "§7Ver todas las recompensas");

        MenuFactory.addMenuSlot(menu, 1, Material.IRON_INGOT, "§eMis Recompensas",
                "bounty mylist", "§7Click para ver");

        MenuFactory.addMenuSlot(menu, 2, Material.GOLD_BLOCK, "§eHistorial",
                "bounty history", "§7Ver mis recompensas pagadas");

        MenuFactory.addMenuSlot(menu, 3, Material.DIAMOND_AXE, "§eCrear Recompensa",
                "bounty create", "§7Poner precio a una cabeza");

        menuManager.registerMenu("bounty", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("create", "list", "mylist", "history");
        }
        return List.of();
    }
}
