package com.github.thejrossdev.alaya_core.common.leveling;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.capability.IPlayerLevel;
import com.github.thejrossdev.alaya_core.config.leveling.LevelingConfig;
import com.github.thejrossdev.alaya_core.init.ModAttributes;
import com.github.thejrossdev.alaya_core.init.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Mob leveling hub: stores/reads the level of a mob in the entity's
 * persistent data (survives saves through Forge's NBT persistence),
 * computes the effective player level around a spawn point and applies
 * per-level attribute modifiers.
 */
public final class MobLevelManager {
	/** Key inside the entity's persistent data ("ForgeData") holding the mob level. */
	public static final String LEVEL_NBT_KEY = Alaya_core.id("level").toString();
	private static final String MODIFIER_NAME = Alaya_core.id("level_modifier").toString();
	private static final UUID MODIFIER_UUID = UUID.nameUUIDFromBytes(MODIFIER_NAME.getBytes());

	private MobLevelManager() {
	}

	public static boolean hasLevel(Entity entity) {
		return entity.getPersistentData().contains(LEVEL_NBT_KEY);
	}

	public static int getLevel(Entity entity) {
		CompoundTag data = entity.getPersistentData();
		return data.contains(LEVEL_NBT_KEY) ? data.getInt(LEVEL_NBT_KEY) : 0;
	}

	public static void setLevel(Entity entity, int level) {
		entity.getPersistentData().putInt(LEVEL_NBT_KEY, Math.max(0, level));
	}

	/**
	 * Effective player level around a mob:
	 * floor(average level of non-spectator players within the configured radius) + structure bonus.
	 * Without nearby players the start level is used.
	 */
	public static int computeLevelAround(@NotNull ServerLevel level, @NotNull Mob mob, @NotNull BlockPos pos) {
		double radius = LevelingConfig.getPlayerRadius();
		if (radius <= 0) {
			return LevelXpCache.getStartLevel();
		}

		AABB box = mob.getBoundingBox().inflate(radius);
		List<? extends Player> nearby = level.getEntitiesOfClass(Player.class, box,
				player -> !player.isSpectator());

		int effective;
		if (nearby.isEmpty()) {
			effective = LevelXpCache.getStartLevel();
		} else {
			long total = 0L;
			for (Player player : nearby) {
				total += getLevelOf(player);
			}
			effective = (int) (total / nearby.size());
		}

		// Level bonus by structure
		effective += structureBonusAt(level, pos);

		return Math.max(effective, 0);
	}

	public static int getLevelOf(@NotNull Player player) {
		return player.getCapability(ModCapabilities.PLAYER_LEVEL)
				.map(IPlayerLevel::getLevel)
				.orElse(1);
	}

	/** Highest configured bonus among the listed structures at {@code pos} (0 when none). */
	private static int structureBonusAt(@NotNull ServerLevel level, @NotNull BlockPos pos) {
		Map<Structure, ?> structures = level.structureManager().getAllStructuresAt(pos);
		if (structures.isEmpty()) {
			return 0;
		}

		int bonus = 0;
		var structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
		for (Structure structure : structures.keySet()) {
			ResourceKey<Structure> key = structureRegistry.getResourceKey(structure).orElse(null);
			if (key == null) {
				continue;
			}
			bonus = Math.max(bonus, LevelingConfig.getStructureBonus(key.location().toString()));
		}
		return bonus;
	}

	/** Applies per-level attribute modifiers; removes any previous one first, so reapplication is safe. */
	public static void applyAttributes(@NotNull Mob mob, int level) {
		if (level <= 0) {
			return;
		}

		for (Map.Entry<Attribute, Double> entry : LevelingConfig.getMobAttributes().entrySet()) {
			AttributeInstance instance = mob.getAttribute(entry.getKey());
			if (instance == null) {
				continue;
			}

			if (instance.getModifier(MODIFIER_UUID) != null) {
				instance.removePermanentModifier(MODIFIER_UUID);
			}

			if (instance.getBaseValue() <= 0) {
				instance.setBaseValue(1.0D);
			}

			double amount = entry.getValue() * level;
			instance.addPermanentModifier(new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, amount,
					AttributeModifier.Operation.MULTIPLY_TOTAL));
		}

		mob.heal(mob.getMaxHealth());
	}

	private static double getAttributeValue(LivingEntity entity, Attribute attributeBonus) {
		if (entity.getAttribute(attributeBonus) == null) return 0F;
		return Objects.requireNonNull(entity.getAttribute(attributeBonus)).getValue();
	}

	public static double getDamageSourceMultiplier(DamageSource source, LivingEntity attacker) {
		if (source.is(DamageTypeTags.IS_PROJECTILE)) {
			return getAttributeValue(attacker, ModAttributes.PROJECTILE_DAMAGE_MULTIPLIER.get());
		}

		if (source.is(DamageTypeTags.IS_EXPLOSION)) {
			return getAttributeValue(attacker, ModAttributes.EXPLOSION_DAMAGE_MULTIPLIER.get());
		}

		if (source.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
			return getAttributeValue(attacker, ModAttributes.ENDER_DRAGON_DAMAGE_MULTIPLIER.get());
		}

		if (source.is(DamageTypeTags.IS_LIGHTNING)) {
			return getAttributeValue(attacker, ModAttributes.LIGHTNING_DAMAGE_MULTIPLIER.get());
		}

		if (source.is(DamageTypeTags.IS_FIRE)) {
			return getAttributeValue(attacker, ModAttributes.FIRE_DAMAGE_MULTIPLIER.get());
		}
		return 1F;
	}
}

