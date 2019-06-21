package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3i;
import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.OptionalValue;

public class VectorConverters {

    public static Vector3i vector3i(@OptionalValue @ConfigPath("x") int x,
                                    @OptionalValue @ConfigPath("y") int y,
                                    @OptionalValue @ConfigPath("z") int z) {
        return new Vector3i(x, y, z);
    }

}
