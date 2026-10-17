package com.github.thejrossdev.alaya_core.util;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class EntityUtils {
	private static final Logger log = LoggerFactory.getLogger(EntityUtils.class);
	private static final Set<EntityType<?>> FORCED_ATTACKING_TYPES = Sets.newHashSet(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.SHULKER, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN);
	private static final LoadingCache<EntityType<?>, Boolean> ATTACK_CACHE = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<>() {
		@Override
		public @NotNull Boolean load(@NotNull EntityType<?> type) {
			if (Alaya_core.MCS != null) {
				try {
					Entity entity = type.create(Alaya_core.OVERWORLD);
					if (entity instanceof LivingEntity living) {
						return canAttack(living);
					}
				} catch (Exception ignored) {
				}
			}
			return FORCED_ATTACKING_TYPES.contains(type);
		}
	});

	public static boolean isValidEntity(LivingEntity livingEntity) {
		return livingEntity != null && livingEntity.isAlive();
	}

	public static boolean canAttack(LivingEntity livingEntity) {
		if (!isValidEntity(livingEntity)) return false;

		boolean canAttack = FORCED_ATTACKING_TYPES.contains(livingEntity.getType());

		if (canAttack) return true;

		try {
			if (livingEntity instanceof RangedAttackMob) {
				return true;
			}
		} catch (Exception ignored) {
		}

		if (livingEntity.getType().getCategory() == MobCategory.MONSTER) {
			return true;
		}

		if (livingEntity instanceof Mob mob) {
			if (mob.getTarget() != null) return true;
		}

//		try {
//			AttributeInstance attackDamage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
//			if (attackDamage != null) {
//				return (attackDamage.getValue()) > 0;
//			}
//		} catch (Exception ignored) {
//		}

		try {
			if (livingEntity instanceof Mob mob) {
				return mob.goalSelector.getAvailableGoals().stream().anyMatch(goal -> {
					Goal wrapped = goal.getGoal();
					return wrapped instanceof MeleeAttackGoal ||
						wrapped instanceof RangedAttackGoal ||
						wrapped instanceof RangedBowAttackGoal ||
						wrapped instanceof RangedCrossbowAttackGoal ||
						wrapped instanceof TargetGoal ||
						wrapped instanceof LeapAtTargetGoal;
				});
			}
		} catch (Exception ignored) {
		}

		return false;
	}

	public static boolean canAttackCached(LivingEntity entity) {
		if (!isValidEntity(entity)) return false;

		return ATTACK_CACHE.getUnchecked(entity.getType());
	}

	public static void registerAttackingMob(EntityType<?> type) {
		FORCED_ATTACKING_TYPES.add(type);
		ATTACK_CACHE.invalidate(type);
	}
}
