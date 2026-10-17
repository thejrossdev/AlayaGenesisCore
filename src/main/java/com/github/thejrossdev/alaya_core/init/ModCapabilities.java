package com.github.thejrossdev.alaya_core.init;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.capability.IPlayerLevel;
import com.github.thejrossdev.alaya_core.common.capability.PlayerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Alaya_core.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {
    public static final ResourceLocation PLAYER_LEVELS_LOCATION = Alaya_core.id("leveling");

    public static final Capability<IPlayerLevel> PLAYER_LEVEL = CapabilityManager.get(new CapabilityToken<>() {
    });

    private static class PlayerLevelProvider implements ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<IPlayerLevel> holder = LazyOptional.of(PlayerLevel::new);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
            return capability == PLAYER_LEVEL ? holder.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return holder.orElseThrow(() -> new IllegalStateException("Player level capability is not attached"))
                    .saveNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            holder.orElseThrow(() -> new IllegalStateException("Player level capability is not attached"))
                    .loadNBT(tag);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(PLAYER_LEVELS_LOCATION, new PlayerLevelProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IPlayerLevel.class);
    }

    /** Propagates leveling state through player death (clone) events. */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PLAYER_LEVEL).ifPresent(oldCap ->
                event.getEntity().getCapability(PLAYER_LEVEL).ifPresent(newCap ->
                        newCap.loadNBT(oldCap.saveNBT())));
        event.getOriginal().invalidateCaps();
    }
}
