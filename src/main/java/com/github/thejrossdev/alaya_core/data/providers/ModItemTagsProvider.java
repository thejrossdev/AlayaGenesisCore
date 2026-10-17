package com.github.thejrossdev.alaya_core.data.providers;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
	public ModItemTagsProvider(PackOutput packOutput,
							   CompletableFuture<HolderLookup.Provider> lookupProvider,
							   CompletableFuture<TagLookup<Block>> blockTagProvider,
							   @Nullable ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, blockTagProvider, Alaya_core.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {

		//     .add(ModItems.MY_ITEM.get())
		//     .add(ModItems.MY_OTHER_ITEM.get());

		// tag(net.minecraft.tags.ItemTags.SWORDS)
		//     .add(ModItems.MY_SWORD.get());

		// tag(net.minecraftforge.common.Tags.Items.INGOTS)
		//     .add(ModItems.MY_INGOT.get());
	}
}
