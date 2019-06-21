package ru.jekarus.skyfortress.v3.lang.messages;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.TextTemplate;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;

import java.util.List;

import static java.util.Collections.singletonList;
import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfGameMessagesLanguage {


    @ConfigPath("castle.capture") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateListGray")
    public List<TextTemplate> castleCapture = singletonList(parse("{player.name} захватывает {team.name_1}"));

    @ConfigPath("castle.captured") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateListGray")
    public List<TextTemplate> castleCaptured = singletonList(parse("Команда {team.name_1} захвачена! Будьте осторожнее!"));

    @ConfigPath("castle.for_team.you_capturing") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate castleForTeamCapturing = parse("Вас захватывают");

    @ConfigPath("castle.for_team.you_captured") @OptionalValue @Generics(SfTitleMessagesLanguage.class)
    public List<SfTitleMessagesLanguage> castleForTeamCaptured = singletonList(new SfTitleMessagesLanguage());

    @ConfigPath("team.lost") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateListGray")
    public List<TextTemplate> teamLost = singletonList(parse("Команда {team.name_1} проиграла!"));

    @ConfigPath("team.win") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateListGray")
    public List<TextTemplate> teamWin = singletonList(parse("Команда {team.name_1} победила!"));

}
