package com.github.thejrossdev.alaya_core.common.constant;

import com.github.thejrossdev.alaya_core.util.TagUtils;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

public enum ETier {
	S,
	A,
	B,
	C,
	D,
	None;

	public static int getTierIntColor(ETier ETier) {
		return switch (ETier) {
			case S -> Colors.TIER_COLOR_S;
			case A -> Colors.TIER_COLOR_A;
			case B -> Colors.TIER_COLOR_B;
			case C -> Colors.TIER_COLOR_C;
			case D -> Colors.TIER_COLOR_D;
			default -> 0xFFFFFF;
		};
	}

	public static TextColor getTierTextColor(ETier ETier) {
		return switch (ETier) {
			case S -> Formatting.TC_COLOR_S;
			case A -> Formatting.TC_COLOR_A;
			case B -> Formatting.TC_COLOR_B;
			case C -> Formatting.TC_COLOR_C;
			case D -> Formatting.TC_COLOR_D;
			default -> TextColor.fromRgb(0xFFFFFF);
		};
	}

	public static ETier getTierFromTag(ItemStack stack) {
		return TagUtils.Tier.getTierFromTag(stack);
	}
}
