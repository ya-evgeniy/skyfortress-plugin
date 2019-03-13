package ru.jekarus.skyfortress.v3.lang.messages;

import org.spongepowered.api.text.TextTemplate;

import java.util.List;

import static java.util.Collections.singletonList;
import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfDistributionLanguage {

    public List<TextTemplate> captainSelected = singletonList(parse("{captain.name} выбрал ${target.name} в свою команду!"));

    public List<TextTemplate> randomSelected = singletonList(parse("{captain.name} слишком долго выбирал, поэтому случайность выбрала за него ;)"));

    public TextTemplate randomSelectedTime = parse("Случайный выбор через {time}");

    public TextTemplate clickToSelect = parse("Нажми {right.click}, чтобы выбрать игрока");

    public TextTemplate rightClick = parse("ПКМ");

    public TextTemplate commandInfoHeader = parse("Капитаны команд при распределении:");
    public TextTemplate commandInfoRandom = parse("{team.name.4} команда — случайный игрок");
    public TextTemplate commandInfoPlayer = parse("{team.name.4} команда — {player.name}");
    public TextTemplate commandInfoDisabled = parse("Отключённые команды: {disabled.teams}");

    public TextTemplate commandInfoDisabledFormatElement = parse("{team.name.0}");
    public TextTemplate commandInfoDisabledFormatSeparator = parse(", ");
    public TextTemplate commandInfoDisabledFormatLast = parse(" и {team.name.0}");

}
