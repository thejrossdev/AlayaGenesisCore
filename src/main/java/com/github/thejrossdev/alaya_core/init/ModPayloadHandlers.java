package com.github.thejrossdev.alaya_core.init;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.network.RespawnSoundPacket;
import com.github.thejrossdev.alaya_core.network.SyncPlayerLevelPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPayloadHandlers {
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Alaya_core.id("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.registerMessage(id(), RespawnSoundPacket.class,
                RespawnSoundPacket::encode,
                RespawnSoundPacket::decode,
                RespawnSoundPacket::handle);

        INSTANCE.registerMessage(id(), SyncPlayerLevelPacket.class,
                SyncPlayerLevelPacket::encode,
                SyncPlayerLevelPacket::decode,
                SyncPlayerLevelPacket::handle);
    }
}
