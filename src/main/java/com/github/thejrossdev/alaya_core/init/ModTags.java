package com.github.thejrossdev.alaya_core.init;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
	public static class Items {
		public static final TagKey<Item> SOUL_BOUNDING = createTag("soul_bounding");
		public static final TagKey<Item> TIER_S = createTag("tier_s");
		public static final TagKey<Item> TIER_A = createTag("tier_a");
		public static final TagKey<Item> TIER_B = createTag("tier_b");
		public static final TagKey<Item> TIER_C = createTag("tier_c");
		public static final TagKey<Item> TIER_D = createTag("tier_d");

		private static TagKey<Item> createTag(String name) {
			return TagKey.create(Registries.ITEM,
				Alaya_core.id(name));
		}
	}
}
