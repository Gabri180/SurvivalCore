package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.models.Bounty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code bounties}.
 */
public class BountyRepository {

    private static final Logger LOGGER = Logger.getLogger(BountyRepository.class.getName());

    private final DatabaseManager databaseManager;

    public BountyRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createBounty(Bounty bounty, Consumer<Long> onId) {
        String sql = """
                INSERT INTO bounties (target_uuid, reward, created_by, created_at)
                VALUES (?, ?, ?, ?)
                """;
        databaseManager.executeInsertAsync(sql, onId, error ->
                        LOGGER.severe("Error creando bounty: " + error.getMessage()),
                bounty.getTargetUuid().toString(),
                bounty.getReward(),
                bounty.getCreatedBy(),
                Timestamp.from(bounty.getCreatedAt())
        );
    }

    public void loadActiveBounties(Consumer<List<Bounty>> callback) {
        databaseManager.queryAsync(
                "SELECT * FROM bounties WHERE claimed_by IS NULL", this::mapRow, callback);
    }

    public void loadActiveBountiesForTarget(UUID targetUuid, Consumer<List<Bounty>> callback) {
        databaseManager.queryAsync(
                "SELECT * FROM bounties WHERE claimed_by IS NULL AND target_uuid = ?",
                this::mapRow, callback, targetUuid.toString());
    }

    public void claimBounty(long bountyId, long claimedById) {
        databaseManager.executeAsync(
                "UPDATE bounties SET claimed_by = ?, claimed_at = NOW() WHERE id = ?",
                null, claimedById, bountyId);
    }

    public void cancelBounty(long bountyId) {
        databaseManager.executeAsync("DELETE FROM bounties WHERE id = ?", null, bountyId);
    }

    private Bounty mapRow(ResultSet rs) {
        try {
            Timestamp createdAt = rs.getTimestamp("created_at");
            Timestamp claimedAt = rs.getTimestamp("claimed_at");
            long claimedBy = rs.getLong("claimed_by");
            boolean claimedByNull = rs.wasNull();
            return Bounty.builder()
                    .id(rs.getLong("id"))
                    .targetUuid(UUID.fromString(rs.getString("target_uuid")))
                    .reward(rs.getLong("reward"))
                    .createdBy(rs.getLong("created_by"))
                    .createdAt(createdAt != null ? createdAt.toInstant() : null)
                    .claimedBy(claimedByNull ? null : claimedBy)
                    .claimedAt(claimedAt != null ? claimedAt.toInstant() : null)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de bounties", e);
            throw new IllegalStateException(e);
        }
    }
}
