package ru.jekarus.skyfortress.v3.distribution.captain;

import com.flowpowered.math.vector.Vector3d;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.List;

public class PlayerState {

    public SfPlayer player;

    public List<Vector3d> changed_blocks;
    public SfLocation location;

}
