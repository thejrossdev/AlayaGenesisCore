package com.github.thejrossdev.alaya_core.compat;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean jadeLoaded = false;

    public static void init(final FMLCommonSetupEvent event) {
        jadeLoaded = ModList.get().isLoaded("jade");

        if (jadeLoaded) {
            LOGGER.info("Jade detected, enabling level display integration");
            setupJadeIntegration();
        } else {
            LOGGER.info("Jade not found, level display integration disabled");
        }
    }

    public static void enqueueIMC(final InterModEnqueueEvent event) {
        if (jadeLoaded) {
            // Можно отправить IMC сообщения для Jade если нужно
            LOGGER.debug("Sending IMC to Jade");
        }
    }

    private static void setupJadeIntegration() {
        try {
            Class.forName("com.github.thejrossdev.alaya_core.compat.jade.JadePlugin");
            LOGGER.info("Jade integration successfully initialized");
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Jade integration classes not found, integration disabled");
            jadeLoaded = false;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Jade integration: {}", e.getMessage());
            jadeLoaded = false;
        }
    }

    public static boolean isJadeLoaded() {
        return jadeLoaded;
    }
}
