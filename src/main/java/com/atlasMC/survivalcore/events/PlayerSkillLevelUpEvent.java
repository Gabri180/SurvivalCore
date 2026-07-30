package com.atlasMC.survivalcore.events;

import com.atlasMC.survivalcore.enums.SkillType;

import java.util.UUID;

/**
 * Emitido via EventAPI cuando un jugador sube de nivel en un skill.
 */
public class PlayerSkillLevelUpEvent {

    private final UUID playerUuid;
    private final SkillType skillType;
    private final int newLevel;

    public PlayerSkillLevelUpEvent(UUID playerUuid, SkillType skillType, int newLevel) {
        this.playerUuid = playerUuid;
        this.skillType = skillType;
        this.newLevel = newLevel;
    }

    public UUID getPlayerUuid() { return playerUuid; }
    public SkillType getSkillType() { return skillType; }
    public int getNewLevel() { return newLevel; }
}
