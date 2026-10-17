package com.github.thejrossdev.alaya_core.data;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.data.providers.LanguageProviderEn;
import com.github.thejrossdev.alaya_core.data.providers.LanguageProviderRu;
import com.github.thejrossdev.alaya_core.data.providers.ModBlockTagsProvider;
import com.github.thejrossdev.alaya_core.data.providers.ModItemTagsProvider;
import com.github.thejrossdev.alaya_core.init.ModFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModFeatures::bootstrapBiomeModifier)
            .add(Registries.CONFIGURED_FEATURE, ModFeatures::bootstrapConfiguredFeature)
            .add(Registries.PLACED_FEATURE, ModFeatures::bootstrapPlacedFeature);

    // Use addProviders() instead
    private RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", Alaya_core.MODID));
    }

    public static void addProviders(boolean isServer, DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        generator.addProvider(isServer, new RegistryDataGenerator(output, provider));
        generator.addProvider(isServer, new LanguageProviderRu(output));
        generator.addProvider(isServer, new LanguageProviderEn(output));
        // This is needed here because Minecraft Forge doesn't properly support tagging custom registries, without problems.
        // If you think this looks fixable, please ensure the fixes are tested in runData & runClient as these current issues exist entirely within Forge's internals.
//        generator.addProvider(isServer, new DamageTypeTagGenerator(output, provider.thenApply(r -> append(r, BUILDER)), helper));

		ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, provider, helper);
		generator.addProvider(isServer, blockTagsProvider);

		ModItemTagsProvider itemTagsProvider = new ModItemTagsProvider(output, provider, blockTagsProvider.contentsGetter(), helper);
		generator.addProvider(isServer, itemTagsProvider);
    }

    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }
}
