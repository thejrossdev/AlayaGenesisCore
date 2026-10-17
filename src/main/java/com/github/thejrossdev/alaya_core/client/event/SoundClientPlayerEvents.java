package com.github.thejrossdev.alaya_core.client.event;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(modid = Alaya_core.MODID, value = Dist.CLIENT)
public class SoundClientPlayerEvents {
    private static final Logger log = LoggerFactory.getLogger(SoundClientPlayerEvents.class);

    public static void handleRespawnSound() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft == null || ModSounds.REZERO_RESPAWN_SOUND == null) {
            return;
        }

        try {
            minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(ModSounds.REZERO_RESPAWN_SOUND.get(), 1.0F, 1.0F)
            );
        } catch (Exception ignored) {}
    }
}
