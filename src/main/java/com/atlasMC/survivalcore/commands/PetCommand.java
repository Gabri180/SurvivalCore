package com.atlasMC.survivalcore.commands;

import com.atlasMC.survivalcore.enums.PetType;
import com.atlasMC.survivalcore.managers.PetManager;
import com.atlasMC.survivalcore.models.Pet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PetCommand implements CommandExecutor, TabCompleter {

    private final PetManager petManager;

    public PetCommand(PetManager petManager) {
        this.petManager = petManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c✗ Este comando solo es para jugadores");
            return true;
        }

        if (!petManager.isEnabled()) {
            player.sendMessage("§c✗ Sistema de mascotas deshabilitado");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                handleCreate(player, args);
                break;
            case "list":
                handleList(player);
                break;
            case "summon":
                handleSummon(player, args);
                break;
            case "dismiss":
                handleDismiss(player);
                break;
            case "info":
                handleInfo(player);
                break;
            case "menu":
                handleMenu(player);
                break;
            default:
                showHelp(player);
        }

        return true;
    }

    private void handleCreate(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§c✗ Uso: /pet create <tipo> <nombre>");
            player.sendMessage("§7Tipos: " + String.join(", ", getPetTypeNames()));
            return;
        }

        try {
            PetType type = PetType.valueOf(args[1].toUpperCase());
            String petName = args[2];

            if (petManager.createPet(player.getUniqueId(), type, petName) == null) {
                player.sendMessage("§c✗ No pudiste crear la mascota");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("§c✗ Tipo de mascota inválido");
        }
    }

    private void handleList(Player player) {
        List<Pet> pets = petManager.getPlayerPets(player.getUniqueId());
        if (pets.isEmpty()) {
            player.sendMessage("§7No tienes mascotas");
            return;
        }

        player.sendMessage("§6════ TUS MASCOTAS ════");
        for (Pet pet : pets) {
            String status = pet.isActive() ? "§a✓ ACTIVA" : "§7Inactiva";
            player.sendMessage("§e" + pet.getName() + " §7- " + pet.getType().getDisplayName() + " §7[Lvl. " + pet.getLevel() + "] " + status);
        }
        player.sendMessage("§6═══════════════════════");
    }

    private void handleSummon(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c✗ Uso: /pet summon <nombre>");
            return;
        }

        String petName = args[1];
        List<Pet> pets = petManager.getPlayerPets(player.getUniqueId());
        Pet pet = pets.stream()
                .filter(p -> p.getName().equalsIgnoreCase(petName))
                .findFirst()
                .orElse(null);

        if (pet == null) {
            player.sendMessage("§c✗ Mascota no encontrada");
            return;
        }

        petManager.summonPet(player.getUniqueId(), pet.getId());
    }

    private void handleDismiss(Player player) {
        Pet pet = petManager.getActivePet(player.getUniqueId());
        if (pet == null) {
            player.sendMessage("§7No tienes una mascota activa");
            return;
        }

        petManager.dismissPet(player.getUniqueId());
    }

    private void handleInfo(Player player) {
        Pet pet = petManager.getActivePet(player.getUniqueId());
        if (pet == null) {
            player.sendMessage("§7No tienes una mascota activa");
            return;
        }

        player.sendMessage("§6════ INFO MASCOTA ════");
        player.sendMessage(petManager.getPetStats(pet));
        player.sendMessage("§6═══════════════════════");
    }

    private void handleMenu(Player player) {
        // Placeholder para menú de mascotas
        player.sendMessage("§6Menú de mascotas (próximamente en /menu edit)");
    }

    private void showHelp(Player player) {
        player.sendMessage("§6════ MASCOTAS ════");
        player.sendMessage("§e/pet create <tipo> <nombre> §7- Crear mascota");
        player.sendMessage("§e/pet list §7- Listar tus mascotas");
        player.sendMessage("§e/pet summon <nombre> §7- Invocar mascota");
        player.sendMessage("§e/pet dismiss §7- Despedir mascota");
        player.sendMessage("§e/pet info §7- Info de mascota activa");
        player.sendMessage("§e/pet menu §7- Menú de mascotas");
        player.sendMessage("§6═════════════════════");
    }

    private List<String> getPetTypeNames() {
        List<String> names = new ArrayList<>();
        for (PetType type : PetType.values()) {
            names.add(type.name());
        }
        return names;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("create");
            completions.add("list");
            completions.add("summon");
            completions.add("dismiss");
            completions.add("info");
            completions.add("menu");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            for (PetType type : PetType.values()) {
                completions.add(type.name().toLowerCase());
            }
        }

        return completions;
    }
}
