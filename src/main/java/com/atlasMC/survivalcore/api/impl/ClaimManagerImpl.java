package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.IClaimManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.ClaimRepository;
import com.atlasMC.survivalcore.models.Claim;
import com.atlasMC.survivalcore.models.SiegeCharge;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClaimManagerImpl implements IClaimManager {

    private final ClaimRepository claimRepository;
    private final PlayerCache playerCache;
    private final FileConfiguration config;
    private final Map<String, Claim> claimsCache = new HashMap<>();

    public ClaimManagerImpl(ClaimRepository claimRepository, PlayerCache playerCache, FileConfiguration config) {
        this.claimRepository = claimRepository;
        this.playerCache = playerCache;
        this.config = config;
    }

    @Override
    public Claim getClaimAt(String world, int x, int z) {
        return claimsCache.values().stream()
                .filter(c -> c.getWorld().equals(world) && c.contains(x, z))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isWithinAttackWindow() {
        String window = config.getString("raideo.attack-window", "20:00-23:00");
        String[] parts = window.split("-");
        LocalTime start = LocalTime.parse(parts[0]);
        LocalTime end = LocalTime.parse(parts[1]);
        LocalTime now = LocalTime.now();
        return !now.isBefore(start) && now.isBefore(end);
    }

    @Override
    public boolean useSiegeCharge(UUID attackerUuid, long claimId) {
        if (!isWithinAttackWindow()) return false;

        Claim claim = claimsCache.values().stream()
                .filter(c -> c.getId() == claimId)
                .findFirst()
                .orElse(null);
        if (claim == null || claim.isImmune()) return false;

        int damage = config.getInt("raideo.siege-charge-damage", 200);
        claim.applyDamage(damage);
        claimRepository.saveClaim(claim);

        notifyOwnerUnderAttack(claimId, attackerUuid);
        return true;
    }

    @Override
    public void notifyOwnerUnderAttack(long claimId, UUID attackerUuid) {
        Claim claim = claimsCache.values().stream()
                .filter(c -> c.getId() == claimId)
                .findFirst()
                .orElse(null);
        if (claim == null) return;

        UUID ownerUuid = playerCache.findUuidByPlayerId(claim.getOwnerId());
        if (ownerUuid != null) {
            Player owner = Bukkit.getPlayer(ownerUuid);
            if (owner != null) {
                owner.sendTitle("§c§lALERTA", "§cTu claim esta siendo atacado", 0, 60, 0);
            }
        }
    }

    @Override
    public boolean canBreak(UUID playerUuid, Claim claim) {
        return claim.getOwnerId() == playerCache.get(playerUuid).getPlayerId();
    }

    @Override
    public boolean canPlace(UUID playerUuid, Claim claim) {
        return claim.getOwnerId() == playerCache.get(playerUuid).getPlayerId();
    }
}
