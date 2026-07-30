package com.atlasMC.survivalcore.api.impl;

import com.atlasMC.survivalcore.api.EventAPI;
import com.atlasMC.survivalcore.api.ISkillManager;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.db.SkillRepository;
import com.atlasMC.survivalcore.enums.SkillType;
import com.atlasMC.survivalcore.events.PlayerSkillLevelUpEvent;
import com.atlasMC.survivalcore.models.PlayerProfile;
import com.atlasMC.survivalcore.models.PlayerSkill;

import java.util.UUID;
import java.util.function.Consumer;

public class SkillManagerImpl implements ISkillManager {

    private final PlayerCache playerCache;
    private final SkillRepository skillRepository;
    private final EventAPI eventAPI;

    public SkillManagerImpl(PlayerCache playerCache, SkillRepository skillRepository, EventAPI eventAPI) {
        this.playerCache = playerCache;
        this.skillRepository = skillRepository;
        this.eventAPI = eventAPI;
    }

    @Override
    public void addExp(UUID uuid, SkillType skillType, long amount) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) return;

        int oldLevel = getLevel(uuid, skillType);
        skillRepository.updateExp(profile.getPlayerId(), skillType, amount);
        skillRepository.loadSkill(profile.getPlayerId(), skillType, skill -> {
            if (skill != null && skill.getLevel() > oldLevel) {
                eventAPI.emit(new PlayerSkillLevelUpEvent(uuid, skillType, skill.getLevel()));
            }
        });
    }

    @Override
    public int getLevel(UUID uuid, SkillType skillType) {
        Integer cached = playerCache.get(uuid) != null
                ? playerCache.get(uuid).getSkills().get(skillType.name())
                : null;
        return cached != null ? cached : 1;
    }

    @Override
    public void getSkill(UUID uuid, SkillType skillType, Consumer<PlayerSkill> callback) {
        PlayerProfile profile = playerCache.get(uuid);
        if (profile == null) {
            callback.accept(null);
            return;
        }
        skillRepository.loadSkill(profile.getPlayerId(), skillType, callback);
    }
}
