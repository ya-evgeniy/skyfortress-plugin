package ru.jekarus.skyfortress.v3.lang.messages;

import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.Collections;
import java.util.List;

public class SfGameMessagesLanguage {

    public List<TextTemplate> castle_capture = Collections.singletonList(SfTextParser.parse("{player.name} захватывает {team.name_1}", TextColors.GRAY));
    public List<TextTemplate> castle_captured = Collections.singletonList(SfTextParser.parse("Команда {team.name_1} захвачена! Будьте осторожнее!", TextColors.GRAY));

    public List<TextTemplate> team_lost = Collections.singletonList(SfTextParser.parse("Команда {team.name_1} проиграла!", TextColors.GRAY));
    public List<TextTemplate> team_win = Collections.singletonList(SfTextParser.parse("Команда {team.name_1} победила!", TextColors.GRAY));

}
