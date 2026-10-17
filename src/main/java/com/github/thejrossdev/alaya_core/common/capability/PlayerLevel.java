package com.github.thejrossdev.alaya_core.common.capability;

import com.github.thejrossdev.alaya_core.common.leveling.LevelXpCache;
import net.minecraft.nbt.CompoundTag;

public class PlayerLevel implements IPlayerLevel {
	private static final String NBT_XP = "Xp";

	private long xp;
	private int level = LevelXpCache.getStartLevel();

	@Override
	public long getXp() {
		return xp;
	}

	@Override
	public boolean setXp(long xp) {
		int oldLevel = this.level;
		this.xp = getXpForRaw(xp);
		this.level = LevelXpCache.getLevel(this.xp);
		return this.level != oldLevel;
	}

	@Override
	public boolean addXp(long amount) {
		if (amount <= 0L) {
			return false;
		}
		return setXp(this.xp + amount);
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public boolean isMaxLevel() {
		return LevelXpCache.isMaxLevel(level);
	}

	@Override
	public void applyServerState(long serverXp, int serverLevel) {
		this.xp = Math.max(0L, serverXp);
		this.level = serverLevel;
	}

	@Override
	public CompoundTag saveNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putLong(NBT_XP, xp);
		return tag;
	}

	@Override
	public void loadNBT(CompoundTag tag) {
		setXp(tag.getLong(NBT_XP));
	}

	/** Clamps raw XP into [0, max-level cap], guarding against overflow. */
	private static long getXpForRaw(long raw) {
		if (raw <= 0L) {
			return 0L;
		}
		if (raw > LevelXpCache.getMaxXp()) {
			return LevelXpCache.getMaxXp();
		}
		return raw;
	}
}
