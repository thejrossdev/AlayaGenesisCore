package com.github.thejrossdev.alaya_core.common.event;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alaya_core.MODID)
public class ServerEvents {
	private static Boolean immediateRespawnWasSet = false;

	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event) {
		// Cache the live MinecraftServer instance for use throughout the mod.
		Alaya_core.MCS = event.getServer();
		// Cache a reference to the Overworld (Level.OVERWORLD) for quick access.
		Alaya_core.OVERWORLD = Alaya_core.MCS.overworld();

		setImmediateRespawnGameRule();
	}

	@SubscribeEvent
	public static void onServerStoppedEvent(ServerStoppedEvent event) {
		// Null out references; the server is no longer valid beyond this point.
		Alaya_core.MCS = null;
		Alaya_core.OVERWORLD = null;
	}

	private static void setImmediateRespawnGameRule() {
		if (!immediateRespawnWasSet && Alaya_core.MCS != null) {
			try {
				GameRules.BooleanValue immediateRespawnRule =
					Alaya_core.MCS.getGameRules().getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN);

				if (!immediateRespawnRule.get()) {
					immediateRespawnRule.set(true, Alaya_core.MCS);
					immediateRespawnWasSet = true;
				}
			} catch (Exception ignored) {}
		}
	}
}
