package com.atlasMC.survivalcore.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class BlockProtectionManager {

    private final Map<Long, Set<Location>> claimProtectedBlocks = new HashMap<>();

    public void protectBlock(long claimId, Location location) {
        claimProtectedBlocks.computeIfAbsent(claimId, k -> new HashSet<>()).add(location);
    }

    public void unprotectBlock(long claimId, Location location) {
        Set<Location> blocks = claimProtectedBlocks.get(claimId);
        if (blocks != null) {
            blocks.remove(location);
        }
    }

    public boolean isBlockProtected(long claimId, Location location) {
        Set<Location> blocks = claimProtectedBlocks.get(claimId);
        return blocks != null && blocks.contains(location);
    }

    public boolean canEditBlock(Player player, long claimId, Location location) {
        // Los miembros del clan siempre pueden editar
        // TODO: Integrar con ClanManager para verificar membresía

        // Si el bloque está protegido, solo owner puede editar
        if (isBlockProtected(claimId, location)) {
            // TODO: Verificar si es owner del clan
            return true;
        }

        return true;
    }

    public void clearClaimProtection(long claimId) {
        claimProtectedBlocks.remove(claimId);
    }

    public int getProtectedBlockCount(long claimId) {
        Set<Location> blocks = claimProtectedBlocks.get(claimId);
        return blocks != null ? blocks.size() : 0;
    }

    public List<Location> getProtectedBlocks(long claimId) {
        Set<Location> blocks = claimProtectedBlocks.get(claimId);
        return blocks != null ? new ArrayList<>(blocks) : new ArrayList<>();
    }
}
