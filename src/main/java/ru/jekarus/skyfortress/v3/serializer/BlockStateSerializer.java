package ru.jekarus.skyfortress.v3.serializer;

import lombok.val;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

public class BlockStateSerializer {

    public static BlockState deserializeBlockState(CommentedConfigurationNode node) {
        final val type = node.getNode("type").getString();
        if (type == null) {
            return null;
        }

        final val blockType = Sponge.getRegistry().getType(BlockType.class, type).orElse(null);
        if (blockType == null) {
            return null;
        }

        return blockType.getDefaultState();
    }

}
