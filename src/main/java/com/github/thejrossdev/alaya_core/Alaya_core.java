package com.github.thejrossdev.alaya_core;

import com.github.thejrossdev.alaya_core.common.CommonProxy;
import com.github.thejrossdev.alaya_core.init.ModPayloadHandlers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(Alaya_core.MODID)
public class Alaya_core {
    public static final String MODID = "alaya_core";
    public static MinecraftServer MCS;
    public static ServerLevel OVERWORLD;

    public Alaya_core(FMLJavaModLoadingContext context) {
        new CommonProxy();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModPayloadHandlers.register();
    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(Alaya_core.MODID, path);
    }
}
