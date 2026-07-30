package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.ClanRole;
import com.atlasMC.survivalcore.models.Clan;
import com.atlasMC.survivalcore.models.ClanMember;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code clans} y {@code clan_members} para el modulo de
 * Clanes (Dev3). Listo para usar: solo falta que IClanManager llame a estos metodos.
 */
public class ClanRepository {

    private static final Logger LOGGER = Logger.getLogger(ClanRepository.class.getName());

    private final DatabaseManager databaseManager;

    public ClanRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveClan(Clan clan, Consumer<Long> callback) {
        String sql = """
                INSERT INTO clans (id, name, owner_id, money, power, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    money = VALUES(money),
                    power = VALUES(power)
                """;
        databaseManager.executeAsync(sql, affected -> {
            if (callback != null) {
                callback.accept(clan.getId());
            }
        },
                clan.getId() == 0 ? null : clan.getId(),
                clan.getName(),
                clan.getOwnerId(),
                clan.getMoney(),
                clan.getPower(),
                clan.getCreatedAt() != null ? Timestamp.from(clan.getCreatedAt()) : null
        );
    }

    public void loadClan(long clanId, Consumer<Clan> callback) {
        String sql = "SELECT * FROM clans WHERE id = ?";
        databaseManager.queryAsync(sql, this::mapClan, results ->
                callback.accept(results.isEmpty() ? null : results.get(0)), clanId);
    }

    public void loadAllClans(Consumer<List<Clan>> callback) {
        databaseManager.queryAsync("SELECT * FROM clans", this::mapClan, callback);
    }

    public void addMember(ClanMember member) {
        String sql = """
                INSERT INTO clan_members (clan_id, player_id, role, joined_at)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE role = VALUES(role)
                """;
        databaseManager.executeAsync(sql, null,
                member.getClanId(), member.getPlayerId(), member.getRole().name(),
                member.getJoinedAt() != null ? Timestamp.from(member.getJoinedAt()) : null);
    }

    public void removeMember(long clanId, long playerId) {
        databaseManager.executeAsync(
                "DELETE FROM clan_members WHERE clan_id = ? AND player_id = ?",
                null, clanId, playerId);
    }

    public void loadMembers(long clanId, Consumer<List<ClanMember>> callback) {
        String sql = "SELECT * FROM clan_members WHERE clan_id = ?";
        databaseManager.queryAsync(sql, this::mapMember, callback, clanId);
    }

    private Clan mapClan(ResultSet rs) {
        try {
            Timestamp createdAt = rs.getTimestamp("created_at");
            return Clan.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .ownerId(rs.getLong("owner_id"))
                    .money(rs.getLong("money"))
                    .power(rs.getInt("power"))
                    .createdAt(createdAt != null ? createdAt.toInstant() : null)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de clans", e);
            throw new IllegalStateException(e);
        }
    }

    private ClanMember mapMember(ResultSet rs) {
        try {
            Timestamp joinedAt = rs.getTimestamp("joined_at");
            return ClanMember.builder()
                    .clanId(rs.getLong("clan_id"))
                    .playerId(rs.getLong("player_id"))
                    .role(ClanRole.valueOf(rs.getString("role")))
                    .joinedAt(joinedAt != null ? joinedAt.toInstant() : null)
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de clan_members", e);
            throw new IllegalStateException(e);
        }
    }
}
