package com.github.thejrossdev.alaya_core.config.leveling;

import com.github.thejrossdev.alaya_core.config.JsonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.HashMap;
import java.util.Map;

public class LevelingConfig extends JsonConfig<LevelingConfigData> {
    private static final LevelingConfig INSTANCE = new LevelingConfig();

    private Map<Attribute, Double> ATTRIBUTE_BONUSES = new HashMap<>();

    public static LevelingConfig getInstance() {
        return INSTANCE;
    }

    private LevelingConfig() {
    }

    @Override
    protected String getFileName() {
        return "leveling.json";
    }

    @Override
    protected Class<LevelingConfigData> getDataClass() {
        return LevelingConfigData.class;
    }

    @Override
    protected LevelingConfigData createDefaults() {
        LevelingConfigData defaultConfig = new LevelingConfigData();

        defaultConfig.mobExperience.put("minecraft:zombie", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:skeleton", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:spider", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:creeper", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:enderman", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:witch", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:blaze", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:ghast", new LevelingConfigData.MobExperience());
        defaultConfig.mobExperience.put("minecraft:wither_skeleton", new LevelingConfigData.MobExperience());

        defaultConfig.structureBonuses.put("minecraft:stronghold", 5);
        defaultConfig.structureBonuses.put("minecraft:fortress", 10);
        defaultConfig.structureBonuses.put("minecraft:end_city", 15);
        defaultConfig.structureBonuses.put("minecraft:nether_fortress", 8);
        defaultConfig.structureBonuses.put("minecraft:bastion_remnant", 12);
        defaultConfig.structureBonuses.put("minecraft:ancient_city", 20);

        defaultConfig.mobScaling.attributes.put("minecraft:generic.max_health", 0.125);
		defaultConfig.mobScaling.attributes.put("minecraft:generic.attack_damage", 0.08);
        defaultConfig.mobScaling.attributes.put("minecraft:generic.armor", 0.085);
		defaultConfig.mobScaling.attributes.put("minecraft:generic.armor_toughness", 0.055);
		defaultConfig.mobScaling.attributes.put("minecraft:generic.knockback_resistance", 0.012);
		defaultConfig.mobScaling.attributes.put("minecraft:generic.attack_knockback", 0.006);
		defaultConfig.mobScaling.attributes.put("minecraft:generic.movement_speed", 0.003415);
		defaultConfig.mobScaling.attributes.put("minecraft:generic.flying_speed", 0.0025);

		defaultConfig.mobScaling.attributes.put("alaya_core:monster.projectile_damage_bonus", 0.035);
        defaultConfig.mobScaling.attributes.put("alaya_core:monster.explosion_damage_bonus", 0.045);
        defaultConfig.mobScaling.attributes.put("alaya_core:monster.lightning_damage_bonus", 0.04);
		defaultConfig.mobScaling.attributes.put("alaya_core:monster.fire_damage_bonus", 0.012);
        defaultConfig.mobScaling.attributes.put("alaya_core:monster.ender_dragon_damage_bonus", 0.04);

        return defaultConfig;
    }

    @Override
    protected void onLoaded() {
        com.github.thejrossdev.alaya_core.common.leveling.LevelXpCache.reload();
        getMobAttributes();
    }

    public static int getStartLevel() {
        return INSTANCE.getOrDefault().levelingSystem.startLevel;
    }

    public static int getMaxLevel() {
        return INSTANCE.getOrDefault().levelingSystem.maxLevel;
    }

    public static double getExponentBase() {
        return INSTANCE.getOrDefault().levelingSystem.exponentBase;
    }

    public static double getExponentMultiplier() {
        return INSTANCE.getOrDefault().levelingSystem.exponentMultiplier;
    }

    public static int getBaseXpReward() {
        return INSTANCE.getOrDefault().levelingSystem.baseXpReward;
    }

    public static double getXpRewardMultiplier() {
        return INSTANCE.getOrDefault().levelingSystem.xpRewardMultiplier;
    }

    public static Map<Attribute, Double> getMobAttributes() {
        if (INSTANCE.loaded && INSTANCE.ATTRIBUTE_BONUSES.isEmpty()) {
            INSTANCE.data.mobScaling.attributes.forEach((attributeName, value) -> {
                ResourceLocation attributeId = ResourceLocation.tryParse(attributeName);
                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);
                if (attribute != null) {
                    INSTANCE.ATTRIBUTE_BONUSES.put(attribute, value);
                }
            });
        }
        return INSTANCE.ATTRIBUTE_BONUSES.isEmpty() ? new HashMap<>() : INSTANCE.ATTRIBUTE_BONUSES;
    }

    public static double getPlayerRadius() {
        return INSTANCE.getOrDefault().mobScaling.playerRadius;
    }

    public static LevelingConfigData.MobExperience getMobExperience(String mobId) {
        if (!INSTANCE.loaded || INSTANCE.data == null) {
            return getDefaultMobExperience();
        }
        if (INSTANCE.data.mobExperience == null || INSTANCE.data.mobExperience.isEmpty()) {
            return getDefaultMobExperience();
        }
        LevelingConfigData.MobExperience mobExp = INSTANCE.data.mobExperience.get(mobId);
        return mobExp != null ? mobExp : getDefaultMobExperience();
    }

    public static LevelingConfigData.MobExperience getMobExperience(net.minecraft.world.entity.EntityType<?> entityType) {
        ResourceLocation mobId = net.minecraft.world.entity.EntityType.getKey(entityType);
        return getMobExperience(mobId.toString());
    }

    public static int getStructureBonus(String structureId) {
        if (!INSTANCE.loaded || INSTANCE.data == null || structureId == null) {
            return 0;
        }
        if (INSTANCE.data.structureBonuses == null || INSTANCE.data.structureBonuses.isEmpty()) {
            return 0;
        }
        Integer bonus = INSTANCE.data.structureBonuses.get(structureId);
        return bonus != null ? bonus : 0;
    }

    public static Map<String, Integer> getStructureBonuses() {
        if (!INSTANCE.loaded || INSTANCE.data == null || INSTANCE.data.structureBonuses == null) {
            return new HashMap<>();
        }
        return new HashMap<>(INSTANCE.data.structureBonuses);
    }

    private static LevelingConfigData.MobExperience getDefaultMobExperience() {
        LevelingConfigData.MobExperience defaultExp = new LevelingConfigData.MobExperience();
        defaultExp.baseXp = getBaseXpReward();
        defaultExp.xpMultiplier = 1.0;
        defaultExp.levelRange = new int[]{1, getMaxLevel()};
        return defaultExp;
    }
}
