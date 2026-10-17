package com.github.thejrossdev.alaya_core.util;

import com.github.thejrossdev.alaya_core.common.constant.ETier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ChatUtils {
	public static Component formatWithTier(ETier ETier, String text) {
		return Component.literal(text)
			.withStyle(style -> style
				.withColor(ETier.getTierTextColor(ETier))
				.withBold(ETier.equals(ETier.S) || ETier.equals(ETier.A))
			);
	}

	public static Component formatWithTier(ETier ETier, Component text) {
		return text.copy()
			.withStyle(style -> style
				.withColor(ETier.getTierTextColor(ETier))
				.withBold(ETier.equals(ETier.S) || ETier.equals(ETier.A))
			);
	}

	public static Component formatWithTier(ETier ETier, MutableComponent text) {
		return text.withStyle()
			.withStyle(style -> style
				.withColor(ETier.getTierTextColor(ETier))
				.withBold(ETier.equals(ETier.S) || ETier.equals(ETier.A))
			);
	}
}
