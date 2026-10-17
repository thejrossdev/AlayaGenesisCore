package com.github.thejrossdev.alaya_core.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigRegistry {
    private static final ConfigRegistry INSTANCE = new ConfigRegistry();
    private final Map<String, JsonConfig<?>> configs = new HashMap<>();

    public static ConfigRegistry get() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonConfig<?>> T register(String name, T config) {
        configs.put(name, config);
        return config;
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonConfig<?>> T get(String name) {
        return (T) configs.get(name);
    }

    public void loadAll() {
        configs.values().forEach(JsonConfig::load);
    }

    public void saveAll() {
        configs.values().forEach(JsonConfig::save);
    }
}
