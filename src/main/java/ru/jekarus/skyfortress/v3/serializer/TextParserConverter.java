package ru.jekarus.skyfortress.v3.serializer;

import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import lombok.val;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.ArrayList;
import java.util.List;

public class TextParserConverter {

    private static TextTemplate templateGold(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.GOLD);
    }

    private static TextTemplate templateGray(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.GRAY);
    }

    private static TextTemplate templateWhite(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.WHITE);
    }

    private static TextTemplate templateBlack(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.BLACK);
    }

    private static TextTemplate templateDarkGrayStrike(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH);
    }

    private static Text textGold(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.GOLD).toText();
    }

    private static Text textGray(@OptionalValue String string) {
        if (string == null) return null;
        return SfTextParser.parse(string, TextColors.GRAY).toText();
    }

    private static List<TextTemplate> templateListGray(@Generics(String.class) List<String> strings) {
        final val result = new ArrayList<TextTemplate>();

        for (String string : strings) {
            result.add(SfTextParser.parse(string, TextColors.GRAY));
        }

        return result;
    }

}
