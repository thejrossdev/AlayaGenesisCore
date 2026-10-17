package com.github.thejrossdev.alaya_core.util;

import com.github.thejrossdev.alaya_core.Alaya_core;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;

public class StructureUtils {
	@Nullable
	public static Map<Structure, LongSet> getAllStructuresAt(BlockPos atPos) {
		if (atPos == null) return null;

		StructureManager structureManager = Alaya_core.OVERWORLD.structureManager();
		return structureManager.getAllStructuresAt(atPos);
	}

	@Nullable
	public static Structure getAllStructureAt(BlockPos atPos) {
		if (atPos == null) return null;

		Map<Structure, LongSet> structures = getAllStructuresAt(atPos);
		if (structures.isEmpty()) return null;

		return structures.keySet().stream().findFirst().orElse(null);
	}

	public static String getStackTraceAsString() {
		var trace = Arrays.stream(Thread.currentThread().getStackTrace());
		StringBuffer sb = new StringBuffer();
		trace.forEach(item -> {
			sb.append(item.toString());
			sb.append("\n");
		});
		return sb.toString();
	}

}
