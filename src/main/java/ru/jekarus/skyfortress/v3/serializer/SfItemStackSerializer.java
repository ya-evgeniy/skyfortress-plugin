package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class SfItemStackSerializer implements TypeSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        String typeStr = node.getNode("type").getString();
        int count = node.getNode("count").getInt();

        if (typeStr == null || count < 1)
        {
            throw new UnsupportedOperationException(String.format("typeStr[%s] == null || count[%s] < 1", typeStr, count));
        }

        ItemType itemType = Sponge.getRegistry().getType(ItemType.class, typeStr).orElse(ItemTypes.NONE);
        return ItemStack.of(itemType, count);
    }

    @Override
    public void serialize(TypeToken<?> type, ItemStack obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
