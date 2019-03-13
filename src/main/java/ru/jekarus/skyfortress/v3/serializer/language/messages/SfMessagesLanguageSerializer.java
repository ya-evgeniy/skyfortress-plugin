package ru.jekarus.skyfortress.v3.serializer.language.messages;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ru.jekarus.skyfortress.v3.lang.messages.SfDistributionLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfGameMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfLobbyMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfMessagesLanguage;

public class SfMessagesLanguageSerializer implements TypeSerializer<SfMessagesLanguage> {

    @Override
    public SfMessagesLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfMessagesLanguage language = new SfMessagesLanguage();

        language.lobby = node.getNode("lobby").getValue(TypeToken.of(SfLobbyMessagesLanguage.class));
        language.game = node.getNode("game").getValue(TypeToken.of(SfGameMessagesLanguage.class));

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfMessagesLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
