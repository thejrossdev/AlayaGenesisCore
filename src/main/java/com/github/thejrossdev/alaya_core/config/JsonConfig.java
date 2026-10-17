package com.github.thejrossdev.alaya_core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class JsonConfig<T> {
    protected final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected boolean loaded;
    protected T data;
    private Path configDirectory;

    protected abstract String getFileName();
    protected abstract Class<T> getDataClass();
    protected abstract T createDefaults();
    protected void onLoaded() {}

    /** Resolves the config directory lazily; falls back to ./config/AlayaCore when the loader is absent (tests). */
    private Path configDirectory() {
        if (configDirectory != null) {
            return configDirectory;
        }
        try {
            configDirectory = FMLPaths.CONFIGDIR.get().resolve("AlayaCore");
        } catch (Throwable ignored) {
            configDirectory = java.nio.file.Path.of("config", "AlayaCore");
        }
        return configDirectory;
    }

    public void load() {
        Path file = configDirectory().resolve(getFileName());
        try {
            if (!Files.exists(configDirectory())) {
                Files.createDirectories(configDirectory());
            }
            if (!Files.exists(file)) {
                writeDefaults(file);
            }
            String json = Files.readString(file);
            data = GSON.fromJson(json, getDataClass());
            loaded = true;
            onLoaded();
        } catch (JsonSyntaxException e) {
            try {
                writeDefaults(file);
                load();
            } catch (IOException ex) {
                data = createDefaults();
                loaded = false;
            }
        } catch (Exception e) {
            data = createDefaults();
            loaded = false;
        }
    }

    public void save() {
        if (data == null) {
            return;
        }
        Path file = configDirectory().resolve(getFileName());
        try {
            if (!Files.exists(configDirectory())) {
                Files.createDirectories(configDirectory());
            }
            Files.writeString(file, GSON.toJson(data), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
        }
    }

    public void reload() {
        load();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public T getData() {
        return data;
    }

    public T getOrDefault() {
        return data != null ? data : createDefaults();
    }

    private void writeDefaults(Path file) throws IOException {
        T defaults = createDefaults();
        Files.writeString(file, GSON.toJson(defaults), StandardOpenOption.CREATE);
    }
}
