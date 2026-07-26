package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.models.Claim;
import com.atlasMC.survivalcore.models.SiegeCharge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code claims} y {@code siege_charges} para el modulo de
 * Raideo (Dev3). Listo para usar: solo falta que IClaimManager llame a estos metodos.
 */
public class ClaimRepository {

    private static final Logger LOGGER = Logger.getLogger(ClaimRepository.class.getName());

    private final DatabaseManager databaseManager;

    public ClaimRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveClaim(Claim claim) {
        String sql = """
                INSERT INTO claims (id, owner_id, clan_id, x1, z1, x2, z2, world, power, last_damaged, damage_immunity_until)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    power = VALUES(power),
                    last_damaged = VALUES(last_damaged),
                    damage_immunity_until = VALUES(damage_immunity_until)
                """;
        databaseManager.executeAsync(sql, null,
                claim.getId() == 0 ? null : claim.getId(),
                claim.getOwnerId(),
                claim.getClanId(),
                claim.getX1(), claim.getZ1(), claim.getX2(), claim.getZ2(),
                claim.getWorld(),
                claim.getPower(),
                claim.getLastDamaged() != null ? Timestamp.from(claim.getLastDamaged()) : null,
                claim.getDamageImmunityUntil() != null ? Timestamp.from(claim.getDamageImmunityUntil()) : null
        );
    }

    public void loadClaim(long claimId, Consumer<Claim> callback) {
        databaseManager.queryAsync("SELECT * FROM claims WHERE id = ?", this::mapClaim, results ->
                callback.accept(results.isEmpty() ? null : results.get(0)), claimId);
    }

    public void loadClaimsByOwner(long ownerId, Consumer<List<Claim>> callback) {
        databaseManager.queryAsync("SELECT * FROM claims WHERE owner_id = ?", this::mapClaim, callback, ownerId);
    }

    public void logSiegeCharge(SiegeCharge charge) {
        String sql = """
                INSERT INTO siege_charges (attacker_id, claim_id, damage, used_at)
                VALUES (?, ?, ?, ?)
                """;
        databaseManager.executeAsync(sql, null,
                charge.getAttackerId(), charge.getClaimId(), charge.getDamage(),
                charge.getUsedAt() != null ? Timestamp.from(charge.getUsedAt()) : null);
    }

    public void countChargesToday(long attackerId, Consumer<Integer> callback) {
        String sql = """
                SELECT COUNT(*) AS total FROM siege_charges
                WHERE attacker_id = ? AND used_at >= CURDATE()
                """;
        databaseManager.queryAsync(sql, rs -> {
            try {
                return rs.getInt("total");
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }, results -> callback.accept(results.isEmpty() ? 0 : results.get(0)), attackerId);
    }

    private Claim mapClaim(ResultSet rs) {
        try {
            Timestamp lastDamaged = rs.getTimestamp("last_damaged");
            Timestamp immunityUntil = rs.getTimestamp("damage_immunity_until");
            long clanId = rs.getLong("clan_id");
            Long clanIdBoxed = rs.wasNull() ? null : clanId;
            return Claim.builder()
                    .id(rs.getLong("id"))
                    .ownerId(rs.getLong("owner_id"))
                    .clanId(clanIdBoxed)
                    .x1(rs.getInt("x1"))
                    .z1(rs.getInt("z1"))
                    .x2(rs.getInt("x2"))
                    .z2(rs.getInt("z2"))
                    .world(rs.getString("world"))
                    .power(rs.getInt("power"))
                    .lastDamaged(lastDamaged != null ? lastDamaged.toInstant() : null)
                    .damageImmunityUntil(immunityUntil != null ? immunityUntil.toInstant() : null)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de claims", e);
            throw new IllegalStateException(e);
        }
    }
}
