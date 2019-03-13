package ru.jekarus.skyfortress.v3.serializer.text;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;

public class SfTextParser {

    public static List<TextTemplate> templates(List<String> strings, TextColor color)
    {
        List<TextTemplate> templates = new ArrayList<>();
        for (String string : strings)
        {
            templates.add(SfTextParser.parse(string, color));
        }
        return templates;
    }

    public static TextTemplate parse(String raw) {
        return parse(raw, TextColors.GRAY);
    }

    public static TextTemplate parse(String raw, TextColor color)
    {
        return parse(raw, color, TextStyles.NONE);
    }

    public static TextTemplate parse(String raw, TextColor color, TextStyle style)
    {
        if (raw == null)
        {
            return TextTemplate.of("null");
        }
        char[] chars = raw.toCharArray();

        int index = 0;
        int length = chars.length;
        char c;

        boolean skip = false;

        List<Object> params = new ArrayList<>();
        params.add(color);
        params.add(style);
        StringBuilder builder = new StringBuilder();

        while (index < length)
        {
            c = chars[index];

            if (skip)
            {
                builder.append(c);

                skip = false;
                continue;
            }

            switch (c)
            {
                case '\\':
                    skip = true;
                    break;
                case '{':
                    if (builder.length() > 0)
                    {
                        params.add(builder.toString());
                        builder = new StringBuilder();
                    }
                    index = parseVariable(index + 1, chars, params);
                    break;
                    default:
                        builder.append(c);
                        break;
            }

            index++;
        }

        if (builder.length() > 0)
        {
            params.add(Text.of(builder.toString()).toBuilder().color(color).build());
        }

        return TextTemplate.of(
                TextTemplate.DEFAULT_OPEN_ARG,
                TextTemplate.DEFAULT_CLOSE_ARG,
                params.toArray()
        );
    }

    private static int parseVariable(int index, char[] chars, List<Object> params)
    {
        int length = chars.length;
        char c;

        boolean skip = false;
        StringBuilder builder = new StringBuilder();

        while (index < length)
        {
            c = chars[index];

            if (skip)
            {
                builder.append(c);
                skip = false;
                continue;
            }

            switch (c)
            {
                case '\\':
                    skip = true;
                    break;
                case '}':
                    if (builder.length() > 0)
                    {
                        String var = builder.toString();
                        params.add(
                                TextTemplate.arg(var).defaultValue(Text.of(var)).optional(true)
                        );
                    }
                    return index;
                default:
                    builder.append(c);
                    break;
            }

            index++;
        }
        return index;
    }
}
