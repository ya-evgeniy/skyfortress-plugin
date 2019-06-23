package ru.jekarus.skyfortress.v3.lobby.interaction;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.player.PlayerData;

import java.util.Collections;
import java.util.List;

public abstract class BlockInteraction {

    private final List<Vector3i> blockPositions;

    public BlockInteraction(Vector3d blockPosition) {
        this(new Vector3i(
                blockPosition.getX(),
                blockPosition.getY(),
                blockPosition.getZ()
        ));
    }

    public BlockInteraction(Vector3i blockPosition) {
        this(Collections.singletonList(blockPosition));
    }

    public BlockInteraction(List<Vector3i> blockPositions) {
        this.blockPositions = blockPositions;
    }

    public final boolean activate(Player player, PlayerData playerData, BlockSnapshot block) {
        final Vector3i position = block.getPosition();

        for (Vector3i blockPosition : this.blockPositions) {
            if (blockPosition.getX() != position.getX()) continue;
            if (blockPosition.getY() != position.getY()) continue;
            if (blockPosition.getZ() != position.getZ()) continue;

            return onInteract(player, playerData, block);
        }

        return false;
    }

    protected abstract boolean onInteract(Player player, PlayerData playerData, BlockSnapshot block);

}
