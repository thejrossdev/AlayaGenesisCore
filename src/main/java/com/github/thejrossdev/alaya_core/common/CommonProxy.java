package com.github.thejrossdev.alaya_core.common;

import com.github.thejrossdev.alaya_core.compat.CompatManager;
import com.github.thejrossdev.alaya_core.config.ConfigRegistry;
import com.github.thejrossdev.alaya_core.config.leveling.LevelingConfig;
import com.github.thejrossdev.alaya_core.init.ModAttributes;
import com.github.thejrossdev.alaya_core.init.ModFeatures;
import com.github.thejrossdev.alaya_core.init.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

public class CommonProxy {
	private static final Logger LOGGER = LogUtils.getLogger();

	@SuppressWarnings("removal")
	public CommonProxy() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.register(this);

		bus.addListener(this::onCommonSetup);
		bus.addListener(this::onEnqueueIMC);
		bus.addListener(this::onProcessIMC);

		ModFeatures.register(bus);
		ModSounds.register(bus);
		ModAttributes.register(bus);

		ConfigRegistry.get().register("leveling", LevelingConfig.getInstance());
	}

	@SubscribeEvent
	public void onCommonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ConfigRegistry.get().loadAll();
			CompatManager.init(event);
		});
	}

	private void onEnqueueIMC(final InterModEnqueueEvent e) {
		CompatManager.enqueueIMC(e);
	}

	private void onProcessIMC(final InterModProcessEvent event) {
		LOGGER.info("Got IMC {}", event.getIMCStream().
			map(m -> m.messageSupplier().get()).
			collect(Collectors.toList()));
	}
}
