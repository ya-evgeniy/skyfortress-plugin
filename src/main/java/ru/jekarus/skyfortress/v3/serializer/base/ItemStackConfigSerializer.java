package ru.jekarus.skyfortress.v3.serializer.base;

import jekarus.hocon.config.serializer.ConfigSerializer;
import jekarus.hocon.config.serializer.serializer.Serializer;
import jekarus.hocon.config.serializer.state.ConfigDeserializeState;
import jekarus.hocon.config.serializer.state.ConfigSerializeState;
import lombok.val;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.serializer.ItemStackSerializer;
import ru.jekarus.skyfortress.v3.serializer.text.SfTextParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemStackConfigSerializer implements Serializer<ItemStack> {

    @Override
    public void serialize(ConfigSerializer serializer, ConfigSerializeState state) {

    }

    @Override
    public void deserialize(ConfigSerializer serializer, ConfigDeserializeState state) {

        final val node = state.getNode();
        final val stack = ItemStackSerializer.deserializeItemStack(
                node,
                single -> SfTextParser.parse(single.getString()).toText(),
                list -> {
                    List<Text> result = new ArrayList<>();
                    if (!list.hasListChildren()) return result;
                    for (CommentedConfigurationNode child : list.getChildrenList()) {
                        result.add(SfTextParser.parse(child.getString()).toText());
                    }
                    return result;
                }
        );

        state.setResult(Optional.ofNullable(stack));

    }

}
