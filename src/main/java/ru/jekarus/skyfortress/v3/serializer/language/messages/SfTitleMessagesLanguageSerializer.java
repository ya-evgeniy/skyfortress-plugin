package ru.jekarus.skyfortress.v3.serializer.language.messages;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import ru.jekarus.skyfortress.v3.lang.messages.SfTitleMessagesLanguage;

import static ru.jekarus.skyfortress.v3.serializer.text.SfTextParser.parse;

public class SfTitleMessagesLanguageSerializer implements TypeSerializer<SfTitleMessagesLanguage> {

    @Nullable
    @Override
    public SfTitleMessagesLanguage deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode node) throws ObjectMappingException {
        SfTitleMessagesLanguage lang = new SfTitleMessagesLanguage();

        lang.top = parse(node.getNode("top").getString());
        lang.bottom = parse(node.getNode("bottom").getString());

        return lang;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable SfTitleMessagesLanguage obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }

}
