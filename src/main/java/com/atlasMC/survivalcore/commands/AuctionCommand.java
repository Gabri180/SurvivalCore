package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.api.IAuctionManager;
import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.menu.MenuData;
import com.atlasMC.survivalcore.menu.MenuFactory;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.models.Auction;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuctionCommand implements CommandExecutor, TabExecutor {

    private final IAuctionManager auctionManager;
    private final EconomyAPI economyAPI;
    private final MenuManager menuManager;

    public AuctionCommand(IAuctionManager auctionManager, EconomyAPI economyAPI, MenuManager menuManager) {
        this.auctionManager = auctionManager;
        this.economyAPI = economyAPI;
        this.menuManager = menuManager;
        createAuctionMenu();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return true;
        }

        if (args.length == 0) {
            menuManager.openMenu(player, "auction");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "sell" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUso: /auction sell <precio>");
                } else {
                    try {
                        long price = Long.parseLong(args[1]);
                        sellItem(player, price);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cPrecio inválido: " + args[1]);
                    }
                }
            }
            case "list" -> listAuctions(player);
            case "mylist" -> listMyAuctions(player);
            case "bid" -> {
                if (args.length < 3) {
                    player.sendMessage("§cUso: /auction bid <id> <cantidad>");
                } else {
                    try {
                        long auctionId = Long.parseLong(args[1]);
                        long amount = Long.parseLong(args[2]);
                        placeBid(player, auctionId, amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cValores inválidos.");
                    }
                }
            }
            case "claim" -> claimItems(player);
            default -> menuManager.openMenu(player, "auction");
        }
        return true;
    }

    private void sellItem(Player player, long price) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("§cDebes tener un item en la mano para vender.");
            return;
        }

        if (price < 100 || price > 1000000) {
            player.sendMessage("§cPrecio debe estar entre §6$100 §cy §6$1,000,000§c.");
            return;
        }

        String itemName = item.getType().name();
        Auction auction = auctionManager.listItem(player.getUniqueId(), itemName, item.getAmount(), price);

        if (auction != null) {
            player.getInventory().setItemInMainHand(null);
            player.sendMessage(String.format("§a✓ Item listado a §6$%d por 24 horas\n§7ID: §b%d", price, auction.getId()));
        } else {
            player.sendMessage("§cError al listar el item.");
        }
    }

    private void listAuctions(Player player) {
        Collection<Auction> auctions = auctionManager.getActiveAuctions();
        if (auctions.isEmpty()) {
            player.sendMessage("§cNo hay subastas activas.");
            return;
        }

        player.sendMessage("§6=== Subastas Activas ===");
        int count = 0;
        for (Auction auction : auctions) {
            if (count >= 10) break;
            String bidderName = auctionManager.getHighestBidder(auction.getId()) != null ? "§bAlguien" : "§7Sin pujas";
            player.sendMessage(String.format("§e[ID: §b%d§e] %s - §6$%d %s",
                auction.getId(), auction.getItemName(), auction.getCurrentBid(), bidderName));
            count++;
        }
    }

    private void listMyAuctions(Player player) {
        Collection<Auction> auctions = auctionManager.getPlayerAuctions(player.getUniqueId());
        if (auctions.isEmpty()) {
            player.sendMessage("§cNo tienes subastas activas.");
            return;
        }

        player.sendMessage("§6=== Mis Subastas ===");
        for (Auction auction : auctions) {
            String bidderName = auctionManager.getHighestBidder(auction.getId()) != null ? "§bAlguien" : "§7Sin pujas";
            player.sendMessage(String.format("§e[ID: §b%d§e] %s - §6$%d %s",
                auction.getId(), auction.getItemName(), auction.getCurrentBid(), bidderName));
        }
    }

    private void placeBid(Player player, long auctionId, long amount) {
        Auction auction = auctionManager.getAuction(auctionId);
        if (auction == null) {
            player.sendMessage("§cSubasta no encontrada: " + auctionId);
            return;
        }

        if (amount <= auction.getCurrentBid()) {
            player.sendMessage(String.format("§cTu puja debe ser mayor a §6$%d§c.", auction.getCurrentBid()));
            return;
        }

        long balance = economyAPI.getBalance(player.getUniqueId());
        if (balance < amount) {
            player.sendMessage(String.format("§cNo tienes suficiente dinero.\n§7Requerido: §6$%d\n§7Tienes: §6$%d", amount, balance));
            return;
        }

        if (auctionManager.placeBid(auctionId, player.getUniqueId(), amount)) {
            player.sendMessage(String.format("§a✓ Puja de §6$%d colocada en subasta §b#%d", amount, auctionId));
        } else {
            player.sendMessage("§cError al colocar la puja.");
        }
    }

    private void claimItems(Player player) {
        // TODO: Implementar reclamación de items ganados
        player.sendMessage("§7Sistema de reclamación - Coming soon...");
    }

    private void createAuctionMenu() {
        MenuData menu = MenuFactory.createMenu("auction", "§6Casa de Subastas", 27);

        MenuFactory.addMenuSlot(menu, 0, Material.DIAMOND, "§eSubastas Activas",
                "auction list", "§7Ver todas las subastas");

        MenuFactory.addMenuSlot(menu, 1, Material.EMERALD, "§eMis Subastas",
                "auction mylist", "§7Click para ver");

        MenuFactory.addMenuSlot(menu, 2, Material.GOLD_INGOT, "§eMis Pujas",
                "auction bids", "§7Ver historial");

        MenuFactory.addMenuSlot(menu, 3, Material.ENDER_PEARL, "§eVender Item",
                "auction sell", "§7Poner item a la venta");

        menuManager.registerMenu("auction", menu);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("sell", "list", "mylist", "bid", "claim");
        }
        return List.of();
    }
}
