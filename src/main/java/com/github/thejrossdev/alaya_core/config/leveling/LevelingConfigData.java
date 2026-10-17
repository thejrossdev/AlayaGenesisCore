package com.github.thejrossdev.alaya_core.config.leveling;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

public class LevelingConfigData {
    @SerializedName("leveling_system")
    public LevelingSystem levelingSystem = new LevelingSystem();

    @SerializedName("mob_scaling")
    public MobScaling mobScaling = new MobScaling();

    @SerializedName("mob_experience")
    public Map<String, MobExperience> mobExperience = new HashMap<>();

    @SerializedName("structure_bonuses")
    public Map<String, Integer> structureBonuses = new HashMap<>();

    public static class LevelingSystem {
        @SerializedName("start_level")
        public int startLevel = 1;

        @SerializedName("max_level")
        public int maxLevel = 100;

        @SerializedName("exponent_base")
        public double exponentBase = 1.6;

        @SerializedName("exponent_multiplier")
        public double exponentMultiplier = 88.0;

        @SerializedName("base_xp_reward")
        public int baseXpReward = 10;

        @SerializedName("xp_reward_multiplier")
        public double xpRewardMultiplier = 1.2;
    }

    public static class MobScaling {
        @SerializedName("black_list")
        public java.util.List<String> blackList = new java.util.ArrayList<>();

        @SerializedName("attributes")
        public Map<String, Double> attributes = new HashMap<>();

        @SerializedName("player_radius")
        public double playerRadius = 256.0;
    }

    public static class MobExperience {
        @SerializedName("base_xp")
        public int baseXp = 10;

        @SerializedName("xp_multiplier")
        public double xpMultiplier = 1.0;

        @SerializedName("level_range")
        public int[] levelRange = new int[]{1, 100};
    }
}
