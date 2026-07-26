package com.atlasMC.survivalcore.services;

import com.atlasMC.survivalcore.api.IExperienceService;
import com.atlasMC.survivalcore.models.PlayerJob;
import com.atlasMC.survivalcore.models.PlayerSkill;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class ExperienceService implements IExperienceService {

    public static final double BASE_REQUIRED_XP = 100.0;
    public static final double XP_GROWTH_FACTOR = 1.15;
    public static final double DEFAULT_MULTIPLIER = 1.0;

    private double baseRequiredXp = BASE_REQUIRED_XP;
    private double growthFactor = XP_GROWTH_FACTOR;
    private UnaryOperator<Double> multiplierProvider = amount -> amount * DEFAULT_MULTIPLIER;

    public void setBaseRequiredXp(double baseRequiredXp) {
        if (baseRequiredXp <= 0.0) {
            throw new IllegalArgumentException("baseRequiredXp must be positive");
        }
        this.baseRequiredXp = baseRequiredXp;
    }

    public void setGrowthFactor(double growthFactor) {
        if (growthFactor <= 1.0) {
            throw new IllegalArgumentException("growthFactor must be greater than 1");
        }
        this.growthFactor = growthFactor;
    }

    public void resetCurve() {
        this.baseRequiredXp = BASE_REQUIRED_XP;
        this.growthFactor = XP_GROWTH_FACTOR;
    }

    public void setMultiplierProvider(UnaryOperator<Double> provider) {
        this.multiplierProvider = (provider != null) ? provider : amount -> amount * DEFAULT_MULTIPLIER;
    }

    public double currentMultiplier(UUID playerId) {
        return DEFAULT_MULTIPLIER;
    }

    @Override
    public void addExperience(PlayerJob job, double amount) {
        validateAmount(amount);
        if (job == null) return;
        double multiplied = multiplierProvider.apply(amount);
        job.setExp(job.getExp() + (long) multiplied);
    }

    @Override
    public void addExperience(PlayerSkill skill, double amount) {
        validateAmount(amount);
        if (skill == null) return;
        double multiplied = multiplierProvider.apply(amount);
        skill.setExp(skill.getExp() + (long) multiplied);
    }

    @Override
    public void removeExperience(PlayerJob job, double amount) {
        validateAmount(amount);
        if (job == null) return;
        long next = job.getExp() - (long) amount;
        job.setExp(Math.max(0L, next));
    }

    @Override
    public void removeExperience(PlayerSkill skill, double amount) {
        validateAmount(amount);
        if (skill == null) return;
        long next = skill.getExp() - (long) amount;
        skill.setExp(Math.max(0L, next));
    }

    @Override
    public void setExperience(PlayerJob job, double amount) {
        validateAmount(amount);
        if (job == null) return;
        job.setExp(Math.max(0L, (long) amount));
    }

    @Override
    public void setExperience(PlayerSkill skill, double amount) {
        validateAmount(amount);
        if (skill == null) return;
        skill.setExp(Math.max(0L, (long) amount));
    }

    @Override
    public double getRequiredExperience(int level) {
        if (level <= 1) return baseRequiredXp;
        return baseRequiredXp * Math.pow(growthFactor, level - 1);
    }

    @Override
    public double calculateMultiplier(UUID playerId) {
        return currentMultiplier(playerId);
    }

    private static void validateAmount(double amount) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Experience amount must be non-negative");
        }
    }
}
