package ru.jekarus.skyfortress.v3.lobby.interaction;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

import java.util.List;

public abstract class ButtonBlockInteraction extends BlockInteraction {

    public ButtonBlockInteraction(Vector3d blockPosition) {
        super(blockPosition);
    }

    public ButtonBlockInteraction(Vector3i blockPosition) {
        super(blockPosition);
    }

    public ButtonBlockInteraction(List<Vector3i> blockPositions) {
        super(blockPositions);
    }

}
