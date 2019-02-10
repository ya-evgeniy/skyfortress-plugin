package ru.jekarus.skyfortress.v3.lang.scoreboard;

import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

public class SfPreGameScoreboardLanguage {

    public TextTemplate creators = SfTextParser.parse("Создатели", TextColors.GOLD);
    public TextTemplate creator = SfTextParser.parse("{creator}", TextColors.GRAY);
    public TextTemplate teams = SfTextParser.parse("Команды", TextColors.GOLD);
    public TextTemplate team = SfTextParser.parse("{team.name.0}", TextColors.GRAY);

}
