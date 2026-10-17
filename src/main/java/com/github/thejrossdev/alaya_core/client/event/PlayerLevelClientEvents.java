package com.github.thejrossdev.alaya_core.client.event;

import com.github.thejrossdev.alaya_core.init.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side application of leveling state sent by the server.
 * The client never computes levels itself; the server is authoritative
 * (computed values on the client's own cache can diverge from the server's config).
 */
public final class PlayerLevelClientEvents {
	private static final Logger log = LoggerFactory.getLogger(PlayerLevelClientEvents.class);

	private PlayerLevelClientEvents() {
	}

	public static void applySync(long xp, int level) {
		if (level < 0) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft == null ? null : minecraft.player;
		if (player == null) {
			log.debug("Received level sync while no local player is present");
			return;
		}

		player.getCapability(ModCapabilities.PLAYER_LEVEL).ifPresent(cap ->
				cap.applyServerState(xp, level));
	}
}
