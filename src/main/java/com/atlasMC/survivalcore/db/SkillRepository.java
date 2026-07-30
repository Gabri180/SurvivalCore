package com.atlasMC.survivalcore.db;

import com.atlasMC.survivalcore.enums.SkillType;
import com.atlasMC.survivalcore.models.PlayerSkill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Persistencia de {@code player_skills} para el modulo de Skills (Hauch).
 * Listo para usar: solo falta que ISkillManager llame a estos metodos.
 */
public class SkillRepository {

    private static final Logger LOGGER = Logger.getLogger(SkillRepository.class.getName());

    private final DatabaseManager databaseManager;

    public SkillRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void saveSkill(long playerId, PlayerSkill skill) {
        String sql = """
                INSERT INTO player_skills (player_id, skill_type, level, exp)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    level = VALUES(level),
                    exp = VALUES(exp)
                """;
        databaseManager.executeAsync(sql, null,
                playerId, skill.getSkillType().name(), skill.getLevel(), skill.getExp());
    }

    public void loadSkill(long playerId, SkillType skillType, Consumer<PlayerSkill> callback) {
        String sql = "SELECT * FROM player_skills WHERE player_id = ? AND skill_type = ?";
        databaseManager.queryAsync(sql, this::mapRow, results -> {
            PlayerSkill skill = results.isEmpty() ? null : results.get(0);
            if (callback != null) {
                callback.accept(skill);
            }
        }, playerId, skillType.name());
    }

    public void loadSkills(long playerId, Consumer<List<PlayerSkill>> callback) {
        String sql = "SELECT * FROM player_skills WHERE player_id = ?";
        databaseManager.queryAsync(sql, this::mapRow, callback, playerId);
    }

    public void updateExp(long playerId, SkillType skillType, long expDelta) {
        String sql = "UPDATE player_skills SET exp = exp + ? WHERE player_id = ? AND skill_type = ?";
        databaseManager.executeAsync(sql, null, expDelta, playerId, skillType.name());
    }

    private PlayerSkill mapRow(ResultSet rs) {
        try {
            return PlayerSkill.builder()
                    .playerId(rs.getLong("player_id"))
                    .skillType(SkillType.valueOf(rs.getString("skill_type")))
                    .level(rs.getInt("level"))
                    .exp(rs.getLong("exp"))
                    .build();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error mapeando fila de player_skills", e);
            throw new IllegalStateException(e);
        }
    }
}
