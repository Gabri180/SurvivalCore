package com.atlasMC.survivalcore.api;

import com.atlasMC.survivalcore.models.PlayerJob;
import com.atlasMC.survivalcore.models.PlayerSkill;

import java.util.UUID;

public interface IExperienceService {
    void addExperience(PlayerJob job, double amount);
    void addExperience(PlayerSkill skill, double amount);
    void removeExperience(PlayerJob job, double amount);
    void removeExperience(PlayerSkill skill, double amount);
    void setExperience(PlayerJob job, double amount);
    void setExperience(PlayerSkill skill, double amount);
    double getRequiredExperience(int level);
    double calculateMultiplier(UUID playerId);
}
