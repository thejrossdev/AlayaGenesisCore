package com.github.thejrossdev.alaya_core.util;

import com.github.thejrossdev.alaya_core.common.constant.ETier;
import com.github.thejrossdev.alaya_core.init.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public class TagUtils {
	public static class Tier {
		private static final Map<ETier, TagKey<Item>> TIER_TAG_MAP = new EnumMap<>(ETier.class);

		static {
			TIER_TAG_MAP.put(ETier.S, ModTags.Items.TIER_S);
			TIER_TAG_MAP.put(ETier.A, ModTags.Items.TIER_A);
			TIER_TAG_MAP.put(ETier.B, ModTags.Items.TIER_B);
			TIER_TAG_MAP.put(ETier.C, ModTags.Items.TIER_C);
			TIER_TAG_MAP.put(ETier.D, ModTags.Items.TIER_D);
		}

		public static ETier getTierFromTag(ItemStack stack) {
			for (Map.Entry<ETier, TagKey<Item>> entry : TIER_TAG_MAP.entrySet()) {
				if (stack.is(entry.getValue())) {
					return entry.getKey();
				}
			}
			return ETier.None;
		}
	}
}
