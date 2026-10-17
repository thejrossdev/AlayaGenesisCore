package com.github.thejrossdev.alaya_core.common.event;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.init.ModPayloadHandlers;
import com.github.thejrossdev.alaya_core.network.RespawnSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Alaya_core.MODID)
public class PlayerRespawnEvent {
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {

			// Send a packet only to this player to trigger the respawn sound on their client.
			ModPayloadHandlers.INSTANCE.send(
				PacketDistributor.PLAYER.with(() -> serverPlayer),
				new RespawnSoundPacket()
			);
		}
	}
}
