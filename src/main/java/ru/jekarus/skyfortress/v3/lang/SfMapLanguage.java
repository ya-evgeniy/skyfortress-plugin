package ru.jekarus.skyfortress.v3.lang;

import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.GenericMethodConverters;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.Arrays;
import java.util.List;

public class SfMapLanguage {

    @OptionalValue @MethodConverter(inClass = SfMapLanguage.class, method = "gold")
    public Text name = Text.of(TextColors.GOLD,"Sky Fortress");

    @OptionalValue @Generics(Text.class) @GenericMethodConverters(@MethodConverter(
            inClass = SfMapLanguage.class, method = "gray"
    ))
    public List<Text> creators = Arrays.asList(
            Text.of(TextColors.GRAY, "JekaRUS"),
            Text.of(TextColors.GRAY, "PikaviT")
    );

    private static Text gray(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.GRAY).toText();
    }

    private static Text gold(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.GOLD).toText();
    }

}
