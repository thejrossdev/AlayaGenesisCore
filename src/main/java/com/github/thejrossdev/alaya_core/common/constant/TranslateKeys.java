package com.github.thejrossdev.alaya_core.common.constant;

import com.github.thejrossdev.alaya_core.Alaya_core;

public class TranslateKeys {

    private static String translateModKey(String key) {
        return Alaya_core.MODID + "." + key;
    }

    private static String translateModKey(String pre, String key) {
        return pre + "." + Alaya_core.MODID + "." + key;
    }

    // Jade
    public static final String JADE = ("config.jade.plugin_" + Alaya_core.MODID + ".leveling"),
            JADE_LEVEL_MAX = translateModKey("jade", "max_level"),
            JADE_LEVEL_MOB = translateModKey("jade", "mob_level"),
            JADE_LEVEL_PLAYER = translateModKey("jade", "player_level"),
            JADE_CONFIG_PLAYER_LEVEL = "config.jade.plugin_" + Alaya_core.MODID + ".player_level",
            JADE_CONFIG_MOB_LEVEL = "config.jade.plugin_" + Alaya_core.MODID + ".mob_level",

    // Respawn Sounds
    RESPAWN_REZERO = translateModKey("subtitles", "respawn"),
            LEVEL_UP_SOUND = translateModKey("subtitles", "level_up"),

    // Tooltips
    TOOLTIP_KEY_ALT = translateModKey("tooltip_key_alt"),
            TOOLTIP_KEY_CTRL = translateModKey("tooltip_key_ctrl"),
            TOOLTIP_KEY_SHIFT = translateModKey("tooltip_key_shift"),
            TOOLTIP_KEY_ALT_CTRL = translateModKey("tooltip_key_alt_ctrl"),

    // Command
    COMMAND_PLAYER_ONLY = translateModKey("command.player_only"),
            COMMAND_NO_PERM = translateModKey("command.no_permission"),

    // Leveling
    LEVELING_XP_GRAINED = translateModKey("xp_gained"),
            LEVELING_LEVELS_EMPTY = translateModKey("levels_empty"),
            LEVELING_LEVELS_HEADER = translateModKey("levels_header"),
            LEVELING_LEVELS_ENTRY = translateModKey("levels_entry"),
            LEVELING_INFO_LEVEL = translateModKey("info_level"),
            LEVELING_INFO_XP = translateModKey("info_xp"),
            LEVELING_SET_LEVEL = translateModKey("set_level"),
            LEVELING_SET_XP = translateModKey("set_xp"),
            LEVELING_LEVEL_UP = translateModKey("level_up");
}
