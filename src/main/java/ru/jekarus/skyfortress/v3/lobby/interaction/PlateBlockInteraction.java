package ru.jekarus.skyfortress.v3.lobby.interaction;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

public abstract class PlateBlockInteraction extends BlockInteraction {

    public PlateBlockInteraction(Vector3d blockPosition) {
        super(blockPosition);
    }

    public PlateBlockInteraction(Vector3i blockPosition) {
        super(blockPosition);
    }

}
