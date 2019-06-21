package ru.jekarus.skyfortress.v3.serializer.base;

import jekarus.hocon.config.serializer.ConfigSerializer;
import jekarus.hocon.config.serializer.serializer.Serializer;
import jekarus.hocon.config.serializer.state.ConfigDeserializeState;
import jekarus.hocon.config.serializer.state.ConfigSerializeState;
import lombok.val;
import org.spongepowered.api.block.BlockState;
import ru.jekarus.skyfortress.v3.serializer.BlockStateSerializer;

import java.util.Optional;

public class BlockStateConfigSerializer implements Serializer<BlockState> {

    @Override
    public void serialize(ConfigSerializer serializer, ConfigSerializeState state) {

    }

    @Override
    public void deserialize(ConfigSerializer serializer, ConfigDeserializeState state) {

        final val node = state.getNode();
        final val blockState = BlockStateSerializer.deserializeBlockState(node);
        state.setResult(Optional.ofNullable(blockState));

    }

}
