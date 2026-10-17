package com.github.thejrossdev.alaya_core.compat.jade;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.constant.TranslateKeys;
import com.github.thejrossdev.alaya_core.init.ModCapabilities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadePlayerLevelProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!(accessor.getEntity() instanceof Player player)) {
            return;
        }

        // The local player's state is synced by the server (SyncPlayerLevelPacket);
        // for other players nothing is rendered since their state is not available.
        player.getCapability(ModCapabilities.PLAYER_LEVEL).ifPresent(level ->
                tooltip.add(Component.translatable(TranslateKeys.JADE_LEVEL_PLAYER, level.getLevel())));
    }

    @Override
    public ResourceLocation getUid() {
        return Alaya_core.id("player_level");
    }
}
