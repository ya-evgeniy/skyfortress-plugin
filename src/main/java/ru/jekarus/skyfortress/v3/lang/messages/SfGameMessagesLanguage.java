package ru.jekarus.skyfortress.v3.lang.messages;

import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfGameMessagesLanguage {

    public List<TextTemplate> castleCapture = singletonList(parse("{player.name} захватывает {team.name_1}"));
    public List<TextTemplate> castleCaptured = singletonList(parse("Команда {team.name_1} захвачена! Будьте осторожнее!"));

    public TextTemplate castleForTeamCapturing = parse("Вас захватывают");
    public List<SfTitleMessagesLanguage> castleForTeamCaptured = singletonList(
            new SfTitleMessagesLanguage("Вас захватили!", "У вас теперь сила!")
    );

    public List<TextTemplate> teamLost = singletonList(parse("Команда {team.name_1} проиграла!"));
    public List<TextTemplate> teamWin = singletonList(parse("Команда {team.name_1} победила!"));

}
