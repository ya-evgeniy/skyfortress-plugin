package ru.jekarus.skyfortress.v3.lang.scoreboard;

import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfInGameScoreboardLanguage {

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate alive = SfTextParser.parse("{team.name.0}", TextColors.GRAY);

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateDarkGrayStrike")
    public TextTemplate death = SfTextParser.parse("{team.name.0}", TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH);

    private TextTemplate parse(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string);
    }
}
