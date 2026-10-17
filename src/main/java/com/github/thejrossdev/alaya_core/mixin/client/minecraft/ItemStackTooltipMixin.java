package com.github.thejrossdev.alaya_core.mixin.client.minecraft;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemStack.class)
public class ItemStackTooltipMixin {
//	private static final Logger log = LoggerFactory.getLogger(ItemStackTooltipMixin.class);
//
//	@Inject(
//		method = "getTooltipLines",
//		at = @At("RETURN"),
//		cancellable = true
//	)
//	private void onGetTooltipLines(
//		@Nullable Player player,
//		TooltipFlag flag,
//		CallbackInfoReturnable<List<Component>> cir
//	) {
//		ItemStack stack = (ItemStack) (Object) this;
//
//		if (stack.isEmpty()) {
//			return;
//		}

//		log.info("=== Debug: List of Components (size: {}) ===", cir.getReturnValue().size());
//		for (int i = 0; i < cir.getReturnValue().size(); i++) {
//			Component comp = cir.getReturnValue().get(i);
//			log.info("[{}] Type: {}, Value: {}", i, comp.getClass().getSimpleName(), comp.getString());
//			Field[] fields = comp.getClass().getDeclaredFields();
//			for (Field field : fields) {
//				field.setAccessible(true);
//				try {
//					Object value = field.get(comp);
//					System.out.println("  " + field.getName() + " = " + value);
//				} catch (IllegalAccessException e) {
//					System.out.println("  " + field.getName() + " = [ACCESS ERROR]");
//				}
//			}
//			log.info("=== Debug: End Comp ===");
//		}

//		TooltipBuilder builder = new TooltipBuilder(stack);
//		List<Component> customTooltip = builder.build();
//
//		cir.getReturnValue().addAll(customTooltip);
//	}
}
