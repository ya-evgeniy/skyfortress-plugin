package ru.jekarus.skyfortress.v3.lang.messages;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.TextTemplate;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;

import java.util.List;

import static java.util.Collections.singletonList;
import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfDistributionLanguage {

    @ConfigPath("captain.captain_selected") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateListGray")
    public List<TextTemplate> captainSelected = singletonList(parse("{captain.name} выбрал ${target.name} в свою команду!"));

    @ConfigPath("captain.random_selected") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateListGray")
    public List<TextTemplate> randomSelected = singletonList(parse("{captain.name} слишком долго выбирал, поэтому случайность выбрала за него ;)"));

    @ConfigPath("captain.random_selected_time") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate randomSelectedTime = parse("Случайный выбор через {time}");

    @ConfigPath("captain.click_to_select") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate clickToSelect = parse("Нажми {right.click}, чтобы выбрать игрока");

    @ConfigPath("captain.right_click") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate rightClick = parse("ПКМ");

    @ConfigPath("captain.command.info.header") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoHeader = parse("Капитаны команд при распределении:");

    @ConfigPath("captain.command.info.random") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoRandom = parse("{team.name.4} команда — случайный игрок");

    @ConfigPath("captain.command.info.player") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoPlayer = parse("{team.name.4} команда — {player.name}");

    @ConfigPath("captain.command.info.disabled") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoDisabled = parse("Отключённые команды: {disabled.teams}");


    @ConfigPath("captain.command.info.disabled_teams_format.element") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoDisabledFormatElement = parse("{team.name.0}");

    @ConfigPath("captain.command.info.disabled_teams_format.separator") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoDisabledFormatSeparator = parse(", ");

    @ConfigPath("captain.command.info.disabled_teams_format.last") @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate commandInfoDisabledFormatLast = parse(" и {team.name.0}");

}
