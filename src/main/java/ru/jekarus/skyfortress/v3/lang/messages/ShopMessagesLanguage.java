package ru.jekarus.skyfortress.v3.lang.messages;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import lombok.val;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.HashMap;
import java.util.Map;

public class ShopMessagesLanguage {

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateBlack")
    public TextTemplate title = TextTemplate.of("title");

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateWhite")
    public TextTemplate cost = TextTemplate.of(TextColors.WHITE, "cost");;

    @OptionalValue @ConfigPath("addition.first") @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate additionFirst = TextTemplate.of(TextColors.GRAY, "addition.first");;

    @OptionalValue @ConfigPath("addition.second") @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate additionSecond = TextTemplate.of(TextColors.GRAY, "addition.second");;

    @OptionalValue @MethodConverter(inClass = ShopMessagesLanguage.class, method = "deserializeItems")
    public Map<String, TextTemplate> items = new HashMap<>();

    private static Map<String, TextTemplate> deserializeItems(@Generics({String.class, String.class}) Map<String, String> items) {
        Map<String, TextTemplate> result = new HashMap<>();
        for (Map.Entry<String, String> entry : items.entrySet()) {
            final val key = entry.getKey();
            final val value = entry.getValue();

            final val text = SfTextParser.parse(value, TextColors.GOLD);
            result.put(key, text);
        }
        return result;
    }

}
