package com.github.thejrossdev.alaya_core.common.leveling;

import com.github.thejrossdev.alaya_core.config.leveling.LevelingConfig;

import java.util.Collections;

/**
 * Precomputed cache of XP thresholds for every level.
 *
 * Built once when the leveling config is loaded; every lookup afterwards
 * is O(log N) via binary search over the cumulative XP array. All values
 * saturate at {@link Long#MAX_VALUE} so extreme config curves can never
 * overflow.
 */
public final class LevelXpCache {
	private static final long SATURATION = Long.MAX_VALUE;


	/** Cumulative XP required to be at level L (index = level, from 0 to maxLevel). */
	private static long[] xpUpTo = new long[] { 0L };
	/** XP required for the transition from level L to level L + 1. */
	private static long[] xpToNext = new long[] { 0L };

	/** Cache-enabled start level */
	private static int startLevel = 1;
	/** Cache-enabled max level */
	private static int maxLevel = 100;

	private static int structureMaxBonus = 0;

	private static boolean saturated = false;

	private LevelXpCache() {
	}

	/** Rebuilds the threshold arrays from the current leveling config. Called on config load/reload. */
	public static synchronized void reload() {
		startLevel = Math.max(1, LevelingConfig.getStartLevel());

		if (!LevelingConfig.getStructureBonuses().isEmpty()) {
			structureMaxBonus = Collections.max(LevelingConfig.getStructureBonuses().values());
		}

		maxLevel = Math.max(startLevel, LevelingConfig.getMaxLevel());

		double exponentBase = LevelingConfig.getExponentBase();
		double exponentMultiplier = LevelingConfig.getExponentMultiplier();

		// Calculate for over max level with structures bonus
		long[] upTo = new long[maxLevel + structureMaxBonus + 1];
		long[] toNext = new long[maxLevel + structureMaxBonus + 1];
		upTo[0] = 0L;

		long total = 0L;
		boolean isSaturated = false;
		for (int level = startLevel; level < maxLevel; level++) {
			long needed = isSaturated ? SATURATION : computeXpToNext(level, exponentBase, exponentMultiplier);
			toNext[level] = needed;
			total = saturatingAdd(total, needed);
			if (needed >= SATURATION) {
				isSaturated = true;
			}
			upTo[level + 1] = total;
		}
		toNext[maxLevel] = SATURATION;
		upTo[maxLevel] = total;

		xpUpTo = upTo;
		xpToNext = toNext;
		saturated = isSaturated;
	}

	/** XP required to advance from {@code level} to {@code level + 1}. */
	private static long computeXpToNext(int level, double exponentBase, double exponentMultiplier) {
		// Factor ~2 reserved against rounding artefacts before saturation.
		double raw = exponentMultiplier * Math.pow(exponentBase, level);
		if (Double.isNaN(raw) || Double.isInfinite(raw) || raw >= SATURATION / 2d) {
			return SATURATION;
		}
		long needed = (long) Math.floor(raw);
		if (needed < 0L) {
			return SATURATION;
		}
		return needed;
	}

	private static long saturatingAdd(long a, long b) {
		long result = a + b;
		if (((a ^ result) & (b ^ result)) < 0L) {
			return SATURATION;
		}
		return result;
	}

	/** Levels the range whose cumulative threshold covers {@code xp} — O(log N) binary search. */
	private static int levelForXp(long xp) {
		if (xp <= 0L) {
			return startLevel;
		}
		int low = startLevel;
		int high = maxLevel;
		while (low < high) {
			int mid = (low + high + 1) >>> 1;
			if (xpUpTo[mid] <= xp) {
				low = mid;
			} else {
				high = mid - 1;
			}
		}
		return low;
	}

	public static int getLevel(long xp) {
		return levelForXp(xp);
	}

	public static long getXpUpTo(int level) {
		return xpUpTo[Math.min(Math.max(level, 0), maxLevel)];
	}

	public static long getXpToNext(int level) {
		return xpToNext[Math.min(Math.max(level, 0), maxLevel)];
	}

	/** Progress within the current level, 0 .. getXpToNext(level) - 1. */
	public static long getXpProgress(long xp, int level) {
		if (isMaxLevel(level)) {
			return getXpToNext(level);
		}
		return Math.max(0L, xp - getXpUpTo(level));
	}

	public static boolean isMaxLevel(int level) {
		return level >= maxLevel;
	}

	/** Absolute XP cap corresponding to maxLevel — used to clamp stored progress. */
	public static long getMaxXp() {
		return getXpUpTo(maxLevel);
	}

	public static int getStartLevel() {
		return startLevel;
	}

	public static int getMaxLevel() {
		return maxLevel;
	}

	public static boolean isSaturated() {
		return saturated;
	}
}
