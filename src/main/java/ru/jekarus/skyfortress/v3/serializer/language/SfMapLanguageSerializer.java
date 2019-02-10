package ru.jekarus.skyfortress.v3.serializer.language;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.lang.SfMapLanguage;

import java.util.ArrayList;
import java.util.List;

public class SfMapLanguageSerializer implements TypeSerializer<SfMapLanguage> {

    @Override
    public SfMapLanguage deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        SfMapLanguage language = new SfMapLanguage();

        language.name = Text.builder(node.getNode("name").getString()).color(TextColors.GOLD).build();

        List<String> unStyledCreators = node.getNode("creators").getList(TypeToken.of(String.class));
        List<Text> creators = new ArrayList<>();
        for (String creator : unStyledCreators)
        {
            creators.add(Text.builder(creator).color(TextColors.GRAY).build());
        }
        language.creators = creators;

        return language;
    }

    @Override
    public void serialize(TypeToken<?> type, SfMapLanguage obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
