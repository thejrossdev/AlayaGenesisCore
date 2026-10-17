package com.github.thejrossdev.alaya_core.data.providers;

import com.github.thejrossdev.alaya_core.Alaya_core;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(PackOutput output,
								CompletableFuture<HolderLookup.Provider> lookupProvider,
								@Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Alaya_core.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
	}
}
