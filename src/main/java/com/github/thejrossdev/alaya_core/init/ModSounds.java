package com.github.thejrossdev.alaya_core.init;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.core.registries.Registries;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Alaya_core.MODID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static RegistryObject<SoundEvent> REZERO_RESPAWN_SOUND = registerSoundEvent("rezero_respawn_sound");

    public static RegistryObject<SoundEvent> LEVEL_UP = registerSoundEvent("level_up");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Alaya_core.id(name)));
    }
}
