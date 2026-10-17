package com.github.thejrossdev.alaya_core.compat.jade;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.constant.TranslateKeys;
import com.github.thejrossdev.alaya_core.common.leveling.MobLevelManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeMobLevelProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        CompoundTag serverData = accessor.getServerData();
        if (!serverData.contains(MobLevelManager.LEVEL_NBT_KEY)) {
            return;
        }

        int level = serverData.getInt(MobLevelManager.LEVEL_NBT_KEY);
        if (level > 0) {
            tooltip.add(Component.translatable(TranslateKeys.JADE_LEVEL_MOB, level));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor accessor) {
        if (!(accessor.getEntity() instanceof LivingEntity living)) {
            return;
        }

        compoundTag.putInt(MobLevelManager.LEVEL_NBT_KEY, MobLevelManager.getLevel(living));
    }

    @Override
    public ResourceLocation getUid() {
        return Alaya_core.id("mob_level");
    }
}
