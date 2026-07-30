package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.enums.PetType;
import com.atlasMC.survivalcore.models.Pet;
import com.atlasMC.survivalcore.models.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.*;

public class PetManager {

    private boolean enabled = false;
    private final Map<UUID, List<Pet>> playerPets = new HashMap<>();
    private final Map<UUID, Pet> activePets = new HashMap<>();
    private final EconomyAPI economyAPI;
    private final PlayerCache playerCache;
    private long nextPetId = 1;

    public PetManager(EconomyAPI economyAPI, PlayerCache playerCache) {
        this.economyAPI = economyAPI;
        this.playerCache = playerCache;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            Bukkit.getLogger().info("✅ Pet System ENABLED");
        } else {
            Bukkit.getLogger().info("🔒 Pet System DISABLED (puede habilitarse en config)");
        }
    }

    public boolean isEnabled() { return enabled; }

    public Pet createPet(UUID playerUuid, PetType type, String petName) {
        if (!enabled) {
            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null) {
                player.sendMessage("§c✗ Sistema de mascotas deshabilitado");
            }
            return null;
        }

        PlayerProfile profile = playerCache.get(playerUuid);
        if (profile == null || profile.getBalance() < type.getCost()) {
            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null) {
                player.sendMessage("§c✗ No tienes suficiente dinero. Necesitas §e$" + type.getCost());
            }
            return null;
        }

        economyAPI.removeBalance(playerUuid, type.getCost());

        Pet pet = Pet.builder()
                .id(nextPetId++)
                .ownerUuid(playerUuid)
                .type(type)
                .name(petName)
                .level(1)
                .exp(0)
                .maxHealth(type.getMaxHealth())
                .health(type.getMaxHealth())
                .active(false)
                .createdAt(Instant.now())
                .build();

        playerPets.computeIfAbsent(playerUuid, k -> new ArrayList<>()).add(pet);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Mascota creada: §e" + petName + " §7(§f" + type.getDisplayName() + "§7)");
        }

        return pet;
    }

    public boolean summonPet(UUID playerUuid, long petId) {
        if (!enabled) return false;

        Pet pet = getPet(playerUuid, petId);
        if (pet == null) return false;

        Pet currentActive = activePets.get(playerUuid);
        if (currentActive != null) {
            currentActive.setActive(false);
        }

        pet.setActive(true);
        activePets.put(playerUuid, pet);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Mascota invocada: §e" + pet.getName());
        }

        return true;
    }

    public void dismissPet(UUID playerUuid) {
        Pet pet = activePets.remove(playerUuid);
        if (pet != null) {
            pet.setActive(false);

            Player player = Bukkit.getPlayer(playerUuid);
            if (player != null) {
                player.sendMessage("§7Mascota despedida");
            }
        }
    }

    public void addPetExp(UUID playerUuid, long amount) {
        Pet pet = activePets.get(playerUuid);
        if (pet != null) {
            int oldLevel = pet.getLevel();
            pet.addExp(amount);

            if (pet.getLevel() > oldLevel) {
                Player player = Bukkit.getPlayer(playerUuid);
                if (player != null) {
                    player.sendMessage("§a✓ §e" + pet.getName() + " §6subió a nivel §e" + pet.getLevel());
                }
            }
        }
    }

    public Pet getActivePet(UUID playerUuid) {
        return activePets.get(playerUuid);
    }

    public Pet getPet(UUID playerUuid, long petId) {
        List<Pet> pets = playerPets.get(playerUuid);
        if (pets == null) return null;
        return pets.stream()
                .filter(p -> p.getId() == petId)
                .findFirst()
                .orElse(null);
    }

    public List<Pet> getPlayerPets(UUID playerUuid) {
        return playerPets.getOrDefault(playerUuid, new ArrayList<>());
    }

    public int getPetCount(UUID playerUuid) {
        return playerPets.getOrDefault(playerUuid, new ArrayList<>()).size();
    }

    public String getPetStats(Pet pet) {
        return "§6" + pet.getName() + " §7(§e" + pet.getType().getDisplayName() + "§7)\n" +
               "§7Nivel: §e" + pet.getLevel() + "\n" +
               "§7Salud: §e" + pet.getHealth() + "§7/§e" + pet.getMaxHealth() + " §7[" + String.format("%.0f%%", pet.getHealthPercent()) + "]\n" +
               "§7Exp: §e" + pet.getExp() + "§7/§e" + pet.getExpNeeded();
    }
}
