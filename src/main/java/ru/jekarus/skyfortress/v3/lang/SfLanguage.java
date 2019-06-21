package ru.jekarus.skyfortress.v3.lang;

import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.translation.locale.Locales;
import ru.jekarus.skyfortress.v3.lang.messages.SfDistributionLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.ShopMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfScoreboardLanguage;
import ru.jekarus.skyfortress.v3.serializer.language.SfLanguagesSerializer;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SfLanguage {

    @MethodConverter(inClass = Locales.class, method = "of")
    public Locale locale;

    public SfMapLanguage map = new SfMapLanguage();

    @MethodConverter(inClass = SfLanguagesSerializer.class, method = "teams")
    public Map<SfTeam, SfTeamLanguage> teams = new HashMap<>();

    public SfScoreboardLanguage scoreboard = new SfScoreboardLanguage();

    public SfMessagesLanguage messages = new SfMessagesLanguage();

    public SfDistributionLanguage distribution = new SfDistributionLanguage();

    @OptionalValue
    public ShopMessagesLanguage shop = new ShopMessagesLanguage();

}
