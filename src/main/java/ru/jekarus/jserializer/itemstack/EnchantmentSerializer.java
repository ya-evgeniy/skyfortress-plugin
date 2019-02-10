package ru.jekarus.jserializer.itemstack;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.enchantment.Enchantment;

public class EnchantmentSerializer implements TypeSerializer<Enchantment> {

    @Override
    public Enchantment deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        return null;
    }

    @Override
    public void serialize(TypeToken<?> typeToken, Enchantment enchantment, ConfigurationNode node) throws ObjectMappingException
    {
        node.getNode(enchantment.getType().getId()).setValue(enchantment.getLevel());
    }

}
