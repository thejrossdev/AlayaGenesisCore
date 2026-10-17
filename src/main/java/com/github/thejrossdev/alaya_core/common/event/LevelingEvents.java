package com.github.thejrossdev.alaya_core.common.event;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.capability.IPlayerLevel;
import com.github.thejrossdev.alaya_core.common.constant.TranslateKeys;
import com.github.thejrossdev.alaya_core.common.leveling.LevelXpCache;
import com.github.thejrossdev.alaya_core.common.leveling.MobLevelManager;
import com.github.thejrossdev.alaya_core.config.leveling.LevelingConfig;
import com.github.thejrossdev.alaya_core.config.leveling.LevelingConfigData;
import com.github.thejrossdev.alaya_core.init.ModCapabilities;
import com.github.thejrossdev.alaya_core.init.ModPayloadHandlers;
import com.github.thejrossdev.alaya_core.init.ModSounds;
import com.github.thejrossdev.alaya_core.util.EntityUtils;
import com.github.thejrossdev.alaya_core.network.SyncPlayerLevelPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

/**
 * Gameplay wiring of the leveling system:
 * mob level assignment on spawn, XP rewards on kills,
 * state sync on login/respawn.
 */
@Mod.EventBusSubscriber(modid = Alaya_core.MODID)
public class LevelingEvents {

	// ===== Mob level assignment =====

	@SubscribeEvent
	public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
		Mob mob = event.getEntity();
		if (!(event.getLevel() instanceof ServerLevel level) || level.isClientSide()) {
			return;
		}
		if (MobLevelManager.hasLevel(mob) || !isLevelable(mob)) {
			return;
		}

		int levelValue = MobLevelManager.computeLevelAround(level, mob, mob.blockPosition());
		MobLevelManager.setLevel(mob, levelValue);
		MobLevelManager.applyAttributes(mob, levelValue);
	}

	private static boolean isLevelable(Mob mob) {
		ResourceLocation entityId = EntityType.getKey(mob.getType());
		if (LevelingConfig.getInstance().getOrDefault().mobScaling.blackList.contains(entityId.toString())) {
			return false;
		}
		return EntityUtils.canAttackCached(mob);
	}

	// ===== Player Damage =====

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		DamageSource source = event.getSource();
		if (!(source.getEntity() instanceof LivingEntity attacker)) {
			return;
		}
		double multiplier = MobLevelManager.getDamageSourceMultiplier(source, attacker);

		if (multiplier > 1F) {
			event.setAmount((float) (event.getAmount() * multiplier));
		}
	}

	@SubscribeEvent
	public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
		event.getTypes().forEach(type -> {
			if (type.getCategory() == MobCategory.MONSTER) {
				LevelingConfig.getMobAttributes().forEach((attribute, aDouble) -> {
					if (!event.has(type, attribute)) {
						event.add(type, attribute, attribute.getDefaultValue());
					}
				});
			}
		});
	}

	// ===== XP rewards =====

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity victim = event.getEntity();
		if (victim.level().isClientSide()) {
			return;
		}

		Entity killer = event.getSource().getEntity();
		if (!(killer instanceof ServerPlayer player) || killer == victim) {
			return;
		}
		if (!hasLevel(victim)) {
			return;
		}

		int mobLevel = MobLevelManager.getLevel(victim);
		LevelingConfigData.MobExperience mobExp = LevelingConfig.getMobExperience(victim.getType());
		int effectiveLevel = clampInto(mobLevel, mobExp.levelRange);

		long xpAmount = Math.round(
				mobExp.baseXp
						* mobExp.xpMultiplier
						* (1.0 + LevelingConfig.getXpRewardMultiplier() * effectiveLevel));

		if (xpAmount <= 0) {
			return;
		}

		player.getCapability(ModCapabilities.PLAYER_LEVEL).ifPresent(cap -> {
			boolean leveledUp = cap.addXp(xpAmount);
			syncLevel(player, cap);
			showRewardMessage(player, cap, xpAmount);
			if (leveledUp) {
				playLevelUpEffects(player, cap);
			}
		});
	}

	private static boolean hasLevel(LivingEntity victim) {
		return MobLevelManager.hasLevel(victim);
	}

	private static int clampInto(int level, int[] levelRange) {
		int lo = levelRange != null && levelRange.length > 0 ? levelRange[0] : 1;
		int hi = levelRange != null && levelRange.length > 1 ? levelRange[1] : LevelXpCache.getMaxLevel();
		return Math.min(Math.max(level, lo), hi);
	}

	private static void showRewardMessage(ServerPlayer player, IPlayerLevel cap, long xpAmount) {
		int currentLevel = cap.getLevel();
		String progress;
		String needed;
		if (cap.isMaxLevel()) {
			progress = "MAX";
			needed = "-";
		} else {
			progress = String.valueOf(LevelXpCache.getXpProgress(cap.getXp(), currentLevel));
			needed = String.valueOf(LevelXpCache.getXpToNext(currentLevel));
		}

		Component message = Component.translatable(
				TranslateKeys.LEVELING_XP_GRAINED,
				String.valueOf(xpAmount),
				String.valueOf(currentLevel),
				progress,
				needed);
		player.displayClientMessage(message, true);
	}

	// ===== Level-up feedback (server side, no extra packets) =====

	private static void playLevelUpEffects(ServerPlayer player, IPlayerLevel cap) {
		player.level().playSound(null, player.blockPosition(), ModSounds.LEVEL_UP.get(),
				SoundSource.PLAYERS, 0.9F, 1.0F);

		if (player.level() instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
					player.getX(), player.getY() + 1.0, player.getZ(), 32, 0.6, 0.7, 0.6, 0.15);
			serverLevel.sendParticles(ParticleTypes.END_ROD,
					player.getX(), player.getY() + 1.0, player.getZ(), 16, 0.5, 0.5, 0.5, 0.05);
		}

		player.sendSystemMessage(Component.translatable(
				TranslateKeys.LEVELING_LEVEL_UP,
				String.valueOf(cap.getLevel())));
	}

	// ===== Cookie/state sync =====

	public static void syncLevel(ServerPlayer player, IPlayerLevel cap) {
		ModPayloadHandlers.INSTANCE.send(
				PacketDistributor.PLAYER.with(() -> player),
				new SyncPlayerLevelPacket(cap.getXp(), cap.getLevel()));
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		player.getCapability(ModCapabilities.PLAYER_LEVEL).ifPresent(cap -> syncLevel(player, cap));
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		player.getCapability(ModCapabilities.PLAYER_LEVEL).ifPresent(cap -> syncLevel(player, cap));
	}
}
