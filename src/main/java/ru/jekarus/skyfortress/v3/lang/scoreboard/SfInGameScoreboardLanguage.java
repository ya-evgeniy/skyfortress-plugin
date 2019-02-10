package ru.jekarus.skyfortress.v3.lang.scoreboard;

import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfInGameScoreboardLanguage {

    public TextTemplate alive = SfTextParser.parse("{team.name.0}", TextColors.GRAY);
    public TextTemplate death = SfTextParser.parse("{team.name.0}", TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH);

}
