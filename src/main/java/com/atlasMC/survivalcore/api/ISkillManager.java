package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.enums.SkillType;
import com.atlasMC.survivalcore.models.PlayerSkill;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Placeholder de Hauch. Implementacion pendiente: conectar a
 * {@link com.atlasMC.survivalcore.db.SkillRepository}.
 */
public interface ISkillManager {

    void addExp(UUID uuid, SkillType skillType, long amount);

    int getLevel(UUID uuid, SkillType skillType);

    void getSkill(UUID uuid, SkillType skillType, Consumer<PlayerSkill> callback);
}
