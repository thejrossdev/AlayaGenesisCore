package com.github.thejrossdev.alaya_core.network;

import com.github.thejrossdev.alaya_core.client.event.PlayerLevelClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server → client sync of the player's leveling state.
 * The server is authoritative; the client only stores these values for display.
 */
public class SyncPlayerLevelPacket {
	private final long xp;
	private final int level;

	public SyncPlayerLevelPacket(long xp, int level) {
		this.xp = xp;
		this.level = level;
	}

	public long getXp() {
		return xp;
	}

	public int getLevel() {
		return level;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeLong(xp);
		buf.writeInt(level);
	}

	public static SyncPlayerLevelPacket decode(FriendlyByteBuf buf) {
		return new SyncPlayerLevelPacket(buf.readLong(), buf.readInt());
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
					() -> () -> PlayerLevelClientEvents.applySync(xp, level));
		});
		ctx.get().setPacketHandled(true);
	}
}
