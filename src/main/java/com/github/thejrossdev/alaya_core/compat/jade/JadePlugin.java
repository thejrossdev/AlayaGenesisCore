package com.github.thejrossdev.alaya_core.compat.jade;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(JadeMobLevelProvider.INSTANCE, LivingEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(JadePlayerLevelProvider.INSTANCE, Player.class);
        registration.registerEntityComponent(JadeMobLevelProvider.INSTANCE, LivingEntity.class);
    }
}
