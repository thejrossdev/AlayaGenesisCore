package com.github.thejrossdev.alaya_core.common.capability;

import net.minecraft.nbt.CompoundTag;

/**
 * Player-independent leveling state: own XP and derived level.
 *
 * Only raw XP is stored; the level is always derived through
 * {@link com.github.thejrossdev.alaya_core.common.leveling.LevelXpCache}.
 */
public interface IPlayerLevel {
	long getXp();

	/** Sets raw XP; clamps into valid bounds. Returns {@code true} if the level changed. */
	boolean setXp(long xp);

	/** Adds XP; clamps at the max-level XP cap. Returns {@code true} if the level changed. */
	boolean addXp(long amount);

	int getLevel();

	boolean isMaxLevel();

	/** Applies authoritative server state (display-only on clients; used in the sync packet). */
	void applyServerState(long xp, int level);

	CompoundTag saveNBT();

	void loadNBT(CompoundTag tag);
}
