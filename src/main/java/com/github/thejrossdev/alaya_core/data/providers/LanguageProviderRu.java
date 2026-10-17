package com.github.thejrossdev.alaya_core.data.providers;

import com.github.thejrossdev.alaya_core.Alaya_core;
import com.github.thejrossdev.alaya_core.common.constant.TranslateKeys;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;


public class LanguageProviderRu extends LanguageProvider {
    public LanguageProviderRu(PackOutput gen) {
        super(gen, Alaya_core.MODID, "ru_ru");
    }

    @Override
    protected void addTranslations() {
        // Jade
        add(TranslateKeys.JADE, "Система уровней");
        add(TranslateKeys.JADE_LEVEL_MAX, "§2Максимальный уровень достигнут");
        add(TranslateKeys.JADE_LEVEL_MOB, "§cУровень §c%d");
        add(TranslateKeys.JADE_LEVEL_PLAYER, "§6Уровень: §e%d");
        add(TranslateKeys.JADE_CONFIG_PLAYER_LEVEL, "Уровень игрока");
        add(TranslateKeys.JADE_CONFIG_MOB_LEVEL, "Уровень моба");

        // Respawn Sounds
        add(TranslateKeys.RESPAWN_REZERO, "Респавн Резеро");
        add(TranslateKeys.LEVEL_UP_SOUND, "Повышение уровня");

        // Leveling
        add(TranslateKeys.LEVELING_XP_GRAINED, "%s ОП §7| §eУровень %s: %s/%s ОП");
        add(TranslateKeys.LEVELING_LEVELS_EMPTY, "Данные по уровням недоступны.");
        add(TranslateKeys.LEVELING_LEVELS_HEADER, "Требуемый опыт по уровням:");
        add(TranslateKeys.LEVELING_LEVELS_ENTRY, "Уровень %d: %d ОП");
        add(TranslateKeys.LEVELING_INFO_LEVEL, "Ваш уровень: %d");
        add(TranslateKeys.LEVELING_INFO_XP, "Опыт: %d / %d");
        add(TranslateKeys.LEVELING_SET_LEVEL, "Ваш уровень установлен на %d.");
        add(TranslateKeys.LEVELING_SET_XP, "Ваш опыт установлен на %d.");
        add(TranslateKeys.LEVELING_LEVEL_UP, "§aПовышение уровня! Теперь вы §e%d§a уровня.");

        // Command
        add(TranslateKeys.COMMAND_PLAYER_ONLY, "Эту команду может использовать только игрок.");
        add(TranslateKeys.COMMAND_NO_PERM, "Необходимо быть в творческом режиме или иметь права оператора.");

        // Tooltips
        add(TranslateKeys.TOOLTIP_KEY_ALT, "§7Нажмите §eALT §7для подробностей");
        add(TranslateKeys.TOOLTIP_KEY_CTRL, "§7Нажмите §eCTRL §7для статистики");
        add(TranslateKeys.TOOLTIP_KEY_SHIFT, "§7Нажмите §eSHIFT §7для советов");
        add(TranslateKeys.TOOLTIP_KEY_ALT_CTRL, "§7Нажмите §eALT+CTRL §7для скрытых данных");
    }

    public void specialText(Item key, String type, String translation) {
        add(key.getDescriptionId() + "." + type, translation);
    }
}
