package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IClanManager;
import com.atlasMC.survivalcore.api.IClanWarManager;
import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.menu.MenuAction;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.Clan;
import com.atlasMC.survivalcore.models.ClanMember;
import com.atlasMC.survivalcore.enums.ClanRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClanCommand implements CommandExecutor, TabExecutor {

    private final IClanManager clanManager;
    private final IClanWarManager clanWarManager;
    private final EconomyAPI economyAPI;
    private final MenuManager menuManager;
    private static final long CREATION_COST = 10000;

    public ClanCommand(IClanManager clanManager, IClanWarManager clanWarManager, EconomyAPI economyAPI, MenuManager menuManager) {
        this.clanManager = clanManager;
        this.clanWarManager = clanWarManager;
        this.economyAPI = economyAPI;
        this.menuManager = menuManager;
        createClanMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "clans");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /clan create <nombre>");
                } else {
                    createClan(player, args[1]);
                }
            }
            case "invite" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /clan invite <jugador>");
                } else {
                    invitePlayer(player, args[1]);
                }
            }
            case "accept" -> acceptInvite(player);
            case "leave" -> leaveClan(player);
            case "info" -> showClanInfo(player);
            case "members" -> showClanMembers(player);
            case "bank" -> showClanBank(player);
            default -> menuManager.openMenu(player, "clans");
        }
        return true;
    }

    private void createClan(Player player, String clanName) {
        Clan existingClan = clanManager.getClanByPlayer(player.getUniqueId());
        if (existingClan != null) {
            player.sendMessage("§cYa estás en un clan.");
            return;
        }

        long balance = economyAPI.getBalance(player.getUniqueId());
        if (balance < CREATION_COST) {
            player.sendMessage(String.format("§cNo tienes suficiente dinero.\n§7Requerido: §6$%d\n§7Tienes: §6$%d", CREATION_COST, balance));
            return;
        }

        economyAPI.removeBalance(player.getUniqueId(), CREATION_COST);
        Clan clan = clanManager.createClan(player.getUniqueId(), clanName);
        player.sendMessage(String.format("§a✓ ¡Clan creado: %s!\n§7Dinero: §6$0 | §7Miembros: §b1 | §7Poder: §b0", clanName));
    }

    private void invitePlayer(Player player, String targetName) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cNo estás en un clan.");
            return;
        }

        ClanRole role = clanManager.getPlayerRole(clan.getId(), player.getUniqueId());
        if (role != ClanRole.OWNER && role != ClanRole.OFFICER) {
            player.sendMessage("§cNo tienes permiso para invitar jugadores.");
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cJugador no encontrado: " + targetName);
            return;
        }

        Clan targetClan = clanManager.getClanByPlayer(target.getUniqueId());
        if (targetClan != null) {
            player.sendMessage("§cEse jugador ya está en un clan.");
            return;
        }

        clanManager.invite(clan.getId(), target.getUniqueId());
        player.sendMessage("§a✓ Invitación enviada a " + targetName);
        target.sendMessage(String.format("§e%s te invitó al clan: %s\n§7Escribe: §f/clan accept", player.getName(), clan.getName()));
    }

    private void acceptInvite(Player player) {
        // TODO: Implementar sistema de invitaciones pendientes
        player.sendMessage("§7Sistema de invitaciones - Coming soon...");
    }

    private void leaveClan(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cNo estás en un clan.");
            return;
        }

        ClanRole role = clanManager.getPlayerRole(clan.getId(), player.getUniqueId());
        if (role == ClanRole.OWNER) {
            player.sendMessage("§cNo puedes salir siendo propietario. Transfiere el liderazgo primero.");
            return;
        }

        clanManager.kick(clan.getId(), player.getUniqueId());
        player.sendMessage("§c✗ Saliste del clan " + clan.getName());
    }

    private void showClanInfo(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cNo estás en un clan.");
            return;
        }

        long money = clanManager.getClanMoney(clan.getId());
        player.sendMessage("§6=== Información del Clan ===");
        player.sendMessage("§eNombre: §f" + clan.getName());
        player.sendMessage(String.format("§eTesorería: §6$%d", money));
        player.sendMessage(String.format("§ePoder: §b%d", clan.getPower()));
        player.sendMessage(String.format("§eMiembros: §b%d", clan.getMembers().size()));
    }

    private void showClanMembers(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cNo estás en un clan.");
            return;
        }

        player.sendMessage("§6=== Miembros del Clan ===");
        player.sendMessage(String.format("§e• Total de miembros: §b%d", clan.getMembers().size()));
        for (ClanMember member : clan.getMembers()) {
            player.sendMessage(String.format("§e• Miembro §7[§b%s§7]", member.getRole().name()));
        }
    }

    private void showClanBank(Player player) {
        Clan clan = clanManager.getClanByPlayer(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cNo estás en un clan.");
            return;
        }

        long money = clanManager.getClanMoney(clan.getId());
        player.sendMessage("§6=== Tesorería del Clan ===");
        player.sendMessage(String.format("§eDinero disponible: §6$%d", money));
    }

    private void createClanMenu() {
        MenuData menu = MenuFactory.createMenu("clans", "§6Clanes", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.SHIELD, "§eCrear Clan",
                MenuAction.command("clan create"), "§7Click para crear");

        MenuFactory.addMenuSlot(menu, 1, Material.BOOK, "§eMi Clan",
                MenuAction.command("clan info"), "§7Ver información");

        MenuFactory.addMenuSlot(menu, 2, Material.BLAZE_POWDER, "§eGuerras",
                MenuAction.command("clan wars"), "§7Ver guerras activas");

        MenuFactory.addMenuSlot(menu, 3, Material.PLAYER_HEAD, "§eMiembros",
                MenuAction.command("clan members"), "§7Ver miembros");

        MenuFactory.addMenuSlot(menu, 4, Material.EMERALD_BLOCK, "§eTesorería",
                MenuAction.command("clan bank"), "§7Ver dinero del clan");

        menuManager.registerMenu("clans", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("create", "invite", "accept", "leave", "info", "members", "bank");
        }
        return List.of();
    }
}
