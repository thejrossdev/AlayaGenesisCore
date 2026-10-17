package com.github.thejrossdev.alaya_core.network;

import com.github.thejrossdev.alaya_core.client.event.SoundClientPlayerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RespawnSoundPacket {
    public RespawnSoundPacket() {
	}

    public RespawnSoundPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static RespawnSoundPacket decode(FriendlyByteBuf buf) {
        return new RespawnSoundPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> SoundClientPlayerEvents::handleRespawnSound);
        });
        ctx.get().setPacketHandled(true);
    }
}
