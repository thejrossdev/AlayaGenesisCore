package com.github.thejrossdev.alaya_core.init;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Alaya_core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Alaya_core.MODID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    public static final RegistryObject<Attribute> PROJECTILE_DAMAGE_MULTIPLIER = rangedAttribute("monster", "projectile_damage_bonus", 1, 1, 1000);
    public static final RegistryObject<Attribute> EXPLOSION_DAMAGE_MULTIPLIER = rangedAttribute("monster", "explosion_damage_bonus", 1, 1, 1000);
    public static final RegistryObject<Attribute> FIRE_DAMAGE_MULTIPLIER = rangedAttribute("monster", "fire_damage_bonus", 1, 1, 1000);
    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE_MULTIPLIER = rangedAttribute("monster", "lightning_damage_bonus", 1, 1, 1000);
    public static final RegistryObject<Attribute> ENDER_DRAGON_DAMAGE_MULTIPLIER = rangedAttribute("monster", "ender_dragon_damage_bonus", 1, 1, 1000);

    private static RegistryObject<Attribute> rangedAttribute(String category, String name, double defaultValue, double minValue, double maxValue) {
        return ATTRIBUTES.register(category + "." + name, () -> new RangedAttribute(category + "." + name, defaultValue, minValue, maxValue).setSyncable(true));
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute.get())));
    }
}
