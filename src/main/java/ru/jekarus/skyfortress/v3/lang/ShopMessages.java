package ru.jekarus.skyfortress.v3.lang;

import lombok.var;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;

public class ShopMessages {

    private final SkyFortressPlugin plugin;
    private final SfMessages messages;
    private final PlayersDataContainer players;

    public ShopMessages(SkyFortressPlugin plugin, SfMessages messages) {
        this.plugin = plugin;
        this.messages = messages;

        this.players = PlayersDataContainer.getInstance();
    }

    public Text shopTitle(SfLanguage language) {
        return language.shop.title.toText();
    }

    public Text cost(SfLanguage language) {
        return language.shop.cost.toText();
    }

    public Text additionFirst(SfLanguage language) {
        return language.shop.additionFirst.toText();
    }

    public Text additionSecond(SfLanguage language) {
        return language.shop.additionSecond.toText();
    }

    public Text translatedKey(SfLanguage language, String translatedKey) {
        final var translatedText = language.shop.items.get(translatedKey);
        if (translatedText == null) {
            return Text.of(TextColors.GOLD, translatedKey);
        }
        return translatedText.toText();
    }

}
