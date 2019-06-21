package ru.jekarus.skyfortress.v3.lang.scoreboard;

import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfPreGameScoreboardLanguage {

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGold")
    public TextTemplate creators = SfTextParser.parse("Создатели", TextColors.GOLD);

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate creator = SfTextParser.parse("{creator}", TextColors.GRAY);

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGold")
    public TextTemplate teams = SfTextParser.parse("Команды", TextColors.GOLD);

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate team = SfTextParser.parse("{team.name.0}", TextColors.GRAY);

}
