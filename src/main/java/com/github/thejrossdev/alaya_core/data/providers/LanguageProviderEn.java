package com.github.thejrossdev.alaya_core.data.providers;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.constant.TranslateKeys;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;


public class LanguageProviderEn extends LanguageProvider {
    public LanguageProviderEn(PackOutput gen) { super(gen, Alaya_core.MODID, "en_us"); }

    @Override
    protected void addTranslations() {
        // Jade
        add(TranslateKeys.JADE, "Leveling system");
        add(TranslateKeys.JADE_LEVEL_MAX, "§2Max Level Reached");
        add(TranslateKeys.JADE_LEVEL_MOB, "§cLevel: §c%d");
        add(TranslateKeys.JADE_LEVEL_PLAYER, "§6Level: §e%d");
        add(TranslateKeys.JADE_CONFIG_PLAYER_LEVEL, "Player Level");
        add(TranslateKeys.JADE_CONFIG_MOB_LEVEL, "Mob Level");

        // Respawn Sounds
        add(TranslateKeys.RESPAWN_REZERO, "Rezero Respawn");
        add(TranslateKeys.LEVEL_UP_SOUND, "Level Up");

        // Leveling
        add(TranslateKeys.LEVELING_XP_GRAINED, "%s XP §7| §eLevel %s: %s/%s XP");
        add(TranslateKeys.LEVELING_LEVELS_EMPTY, "No level data available.");
        add(TranslateKeys.LEVELING_LEVELS_HEADER, "Level requirements:");
        add(TranslateKeys.LEVELING_LEVELS_ENTRY, "Level %d: %d XP");
        add(TranslateKeys.LEVELING_INFO_LEVEL, "Your level: %d");
        add(TranslateKeys.LEVELING_INFO_XP, "XP: %d / %d");
        add(TranslateKeys.LEVELING_SET_LEVEL, "Player level has been set to %d.");
        add(TranslateKeys.LEVELING_SET_XP, "Player XP has been set to %d.");
        add(TranslateKeys.LEVELING_LEVEL_UP, "§aLevel up! You are now level §e%d§a.");

        // Command
        add(TranslateKeys.COMMAND_PLAYER_ONLY, "This command can only be used by a player.");
        add(TranslateKeys.COMMAND_NO_PERM, "You must be in creative mode or have operator permissions to use this command.");

        // Tooltips
        add(TranslateKeys.TOOLTIP_KEY_ALT, "§7Press §eALT §7for details");
        add(TranslateKeys.TOOLTIP_KEY_CTRL, "§7Press §eCTRL §7for statistics");
        add(TranslateKeys.TOOLTIP_KEY_SHIFT, "§7Press §eSHIFT §7for tips");
        add(TranslateKeys.TOOLTIP_KEY_ALT_CTRL, "§7Press §eALT+CTRL §7for hidden data");
    }

    public void specialText(Item key, String type, String translation) {
        add(key.getDescriptionId() + "." + type, translation);
    }
}
