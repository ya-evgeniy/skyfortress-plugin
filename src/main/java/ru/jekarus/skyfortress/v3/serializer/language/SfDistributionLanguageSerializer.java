package ru.jekarus.skyfortress.v3.serializer.language;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.TypeTokens;
import ru.jekarus.skyfortress.v3.lang.messages.SfDistributionLanguage;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;
import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.templates;

public class SfDistributionLanguageSerializer implements TypeSerializer<SfDistributionLanguage> {

    @Nullable
    @Override
    public SfDistributionLanguage deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode node) throws ObjectMappingException {
        SfDistributionLanguage lang = new SfDistributionLanguage();

        ConfigurationNode captainNode = node.getNode("captain");
        lang.captainSelected = templates(captainNode.getNode("captain_selected").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);
        lang.randomSelected = templates(captainNode.getNode("random_selected").getList(TypeTokens.STRING_TOKEN), TextColors.GRAY);

        lang.randomSelectedTime = parse(captainNode.getNode("random_selected_time").getString());
        lang.clickToSelect = parse(captainNode.getNode("click_to_select").getString());
        lang.rightClick = parse(captainNode.getNode("right_click").getString());

        ConfigurationNode captainCommandNode = captainNode.getNode("command");
        ConfigurationNode captainCommandInfoNode = captainCommandNode.getNode("info");

        lang.commandInfoHeader = parse(captainCommandInfoNode.getNode("header").getString());
        lang.commandInfoRandom = parse(captainCommandInfoNode.getNode("random").getString());
        lang.commandInfoPlayer = parse(captainCommandInfoNode.getNode("player").getString());
        lang.commandInfoDisabled = parse(captainCommandInfoNode.getNode("disabled").getString());

        ConfigurationNode disabledTeamsFormat = captainCommandInfoNode.getNode("disabled_teams_format");
        lang.commandInfoDisabledFormatElement = parse(disabledTeamsFormat.getNode("element").getString());
        lang.commandInfoDisabledFormatSeparator = parse(disabledTeamsFormat.getNode("separator").getString());
        lang.commandInfoDisabledFormatLast = parse(disabledTeamsFormat.getNode("last").getString());

        return lang;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable SfDistributionLanguage obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }

}
