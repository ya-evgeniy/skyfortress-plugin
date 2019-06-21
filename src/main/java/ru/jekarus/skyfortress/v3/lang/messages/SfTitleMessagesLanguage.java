package ru.jekarus.skyfortress.v3.lang.messages;

import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import org.spongepowered.api.text.TextTemplate;
import ru.jekarus.skyfortress.v3.serializer.TextParserConverter;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfTitleMessagesLanguage {

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate top = SfTextParser.parse("Вас захватили!");

    @OptionalValue @MethodConverter(inClass = TextParserConverter.class, method = "templateGray")
    public TextTemplate bottom = SfTextParser.parse("У вас теперь сила!");

    public SfTitleMessagesLanguage() {
    }

    public SfTitleMessagesLanguage(String top, String bottom) {
        this(parse(top), parse(bottom));
    }

    public SfTitleMessagesLanguage(TextTemplate top, TextTemplate bottom) {
        this.top = top;
        this.bottom = bottom;
    }

}
