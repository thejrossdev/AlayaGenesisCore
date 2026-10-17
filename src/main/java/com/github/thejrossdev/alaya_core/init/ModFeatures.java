package com.github.thejrossdev.alaya_core.init;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, Alaya_core.MODID);
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, Alaya_core.MODID);

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
    }

    public static void bootstrapConfiguredFeature(BootstapContext<ConfiguredFeature<?, ?>> context) {
    }

    /**
     * Bootstrap placed features with configuration-based spacing.
     * Uses config values: STRUCTURE_MIN_DISTANCE, STRUCTURE_MAX_DISTANCE, STRUCTURE_COUNT_PER_CHUNK
     *
     * Example usage:
     * <pre>{@code
     * HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(CONFIGURED_FEATURES.getRegistryKey());
     * Holder<ConfiguredFeature<?, ?>> holderMyStructure = holdergetter.getOrThrow(YOUR_STRUCTURE_FEATURE);
     *
     * // Use configuration values structure spawning with one structure per chunk
     * List<PlacementModifier> list = StructureUtils.createStructurePlacement(
     *     Config.STRUCTURE_COUNT_PER_CHUNK.get(),
     *     Config.STRUCTURE_MIN_DISTANCE.get(),
     *     Config.STRUCTURE_MAX_DISTANCE.get()
     * );
     *
     * PlacementUtils.register(context, YOUR_STRUCTURE_PLACEMENT, holderMyStructure, list);
     * }</pre>
     */
    public static void bootstrapPlacedFeature(BootstapContext<PlacedFeature> context) {
    }

    public static void bootstrapBiomeModifier(final BootstapContext<BiomeModifier> context) {
        final var biomes = context.lookup(ForgeRegistries.BIOMES.getRegistryKey());
        final var features = context.lookup(PLACED_FEATURES.getRegistryKey());
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey(final String name) {
        return ResourceKey.create(CONFIGURED_FEATURES.getRegistryKey(), Alaya_core.id(name));
    }

    private static ResourceKey<PlacedFeature> placedFeatureResourceKey(final String name) {
        return ResourceKey.create(PLACED_FEATURES.getRegistryKey(), Alaya_core.id(name));
    }

    private static ResourceKey<BiomeModifier> biomeModifierResourceKey(final String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Alaya_core.id(name));
    }

    private static HolderSet<Biome> tag(final HolderGetter<Biome> holderGetter, final TagKey<Biome> key) {
        return holderGetter.getOrThrow(key);
    }

    private static HolderSet<PlacedFeature> feature(final HolderGetter<PlacedFeature> holderGetter, final ResourceKey<PlacedFeature> feature) {
        return HolderSet.direct(holderGetter.getOrThrow(feature));
    }
}
