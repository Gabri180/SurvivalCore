package com.atlasMC.survivalcore.managers;

import com.atlasMC.survivalcore.models.ClanBank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanBankManager {

    private final Map<Long, ClanBank> banks = new HashMap<>();

    public ClanBank createBank(long clanId) {
        ClanBank bank = ClanBank.builder()
                .clanId(clanId)
                .balance(0)
                .maxSlots(36)
                .usedSlots(0)
                .build();

        banks.put(clanId, bank);
        return bank;
    }

    public boolean deposit(long clanId, UUID playerUuid, long amount) {
        ClanBank bank = banks.get(clanId);
        if (bank == null || !bank.canDeposit(amount)) return false;

        bank.setBalance(bank.getBalance() + amount);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Depositaste §e$" + amount + " §6al banco del clan");
        }

        return true;
    }

    public boolean withdraw(long clanId, UUID playerUuid, long amount) {
        ClanBank bank = banks.get(clanId);
        if (bank == null || !bank.canWithdraw(amount)) return false;

        bank.setBalance(bank.getBalance() - amount);

        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a✓ §6Retiraste §e$" + amount + " §6del banco del clan");
        }

        return true;
    }

    public long getBalance(long clanId) {
        ClanBank bank = banks.get(clanId);
        return bank != null ? bank.getBalance() : 0;
    }

    public ClanBank getBank(long clanId) {
        return banks.get(clanId);
    }

    public void addSlot(long clanId) {
        ClanBank bank = banks.get(clanId);
        if (bank != null && bank.getMaxSlots() < 108) {
            bank.setMaxSlots(bank.getMaxSlots() + 9);
        }
    }
}
