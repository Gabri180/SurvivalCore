package com.atlasMC.survivalcore.menu;

import com.atlasMC.survivalcore.enums.PetType;
import com.atlasMC.survivalcore.managers.PetManager;
import com.atlasMC.survivalcore.models.Pet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PetMenuUI {

    private final PetManager petManager;
    private final MenuManager menuManager;
    private final Player player;

    public PetMenuUI(PetManager petManager, MenuManager menuManager, Player player) {
        this.petManager = petManager;
        this.menuManager = menuManager;
        this.player = player;
    }

    public void openPetShop() {
        CustomMenuBuilder builder = new CustomMenuBuilder("_pet_shop_" + player.getUniqueId())
                .title("§6§l🐾 Tienda de Mascotas")
                .rows(6)
                .backgroundColor("DARK_GRAY_STAINED_GLASS_PANE")
                .fillBackground(true);

        int slot = 1;
        for (PetType type : PetType.values()) {
            ItemStack item = createPetItem(type);
            builder.addItem(slot, item.getType(), item.getItemMeta().getDisplayName(), MenuAction.none());
            slot += 2;
        }

        builder.addCloseButton(53);

        MenuData menuData = builder.build();
        String menuId = "_pet_shop_" + player.getUniqueId();
        menuManager.registerMenu(menuId, menuData);
        menuManager.openMenu(player, menuId);
    }

    public void openMyPets() {
        List<Pet> pets = petManager.getPlayerPets(player.getUniqueId());

        CustomMenuBuilder builder = new CustomMenuBuilder("_my_pets_" + player.getUniqueId())
                .title("§6§l🐾 Mis Mascotas")
                .rows(6)
                .backgroundColor("DARK_GRAY_STAINED_GLASS_PANE")
                .fillBackground(true);

        int slot = 1;
        for (Pet pet : pets) {
            ItemStack item = createMyPetItem(pet);
            builder.addItem(slot, item.getType(), item.getItemMeta().getDisplayName(), MenuAction.none());
            slot += 2;
        }

        if (pets.isEmpty()) {
            player.sendMessage("§7No tienes mascotas. Compra una en la tienda!");
        }

        builder.addCloseButton(53);

        MenuData menuData = builder.build();
        String menuId = "_my_pets_" + player.getUniqueId();
        menuManager.registerMenu(menuId, menuData);
        menuManager.openMenu(player, menuId);
    }

    private ItemStack createPetItem(PetType type) {
        ItemStack item = new ItemStack(Material.DRAGON_EGG);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(type.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("§7Precio: §e$" + type.getCost());
            lore.add("§7Salud: §e" + type.getMaxHealth());
            lore.add("");
            lore.add("§aClickea para comprar");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    private ItemStack createMyPetItem(Pet pet) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§e" + pet.getName() + " " + pet.getType().getIcon());
            List<String> lore = new ArrayList<>();
            lore.add("§7Tipo: " + pet.getType().getDisplayName());
            lore.add("§7Nivel: §e" + pet.getLevel());
            lore.add("§7Salud: §e" + pet.getHealth() + "§7/§e" + pet.getMaxHealth());
            String status = pet.isActive() ? "§a✓ ACTIVA" : "§7Inactiva";
            lore.add("§7Estado: " + status);
            lore.add("");
            lore.add("§aClickea para invocar");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }
}
