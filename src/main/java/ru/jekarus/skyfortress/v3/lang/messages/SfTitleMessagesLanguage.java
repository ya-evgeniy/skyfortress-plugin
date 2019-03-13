package ru.jekarus.skyfortress.v3.lang.messages;

import org.spongepowered.api.text.TextTemplate;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfTitleMessagesLanguage {

    public TextTemplate top;

    public TextTemplate bottom;

    public SfTitleMessagesLanguage() {
        this("title.top.null", "title.bottom.null");
    }

    public SfTitleMessagesLanguage(String top, String bottom) {
        this(parse(top), parse(bottom));
    }

    public SfTitleMessagesLanguage(TextTemplate top, TextTemplate bottom) {
        this.top = top;
        this.bottom = bottom;
    }

}
