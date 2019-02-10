package ru.jekarus.skyfortress.v3.serializer;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class SfVector3dSerialize implements TypeSerializer<Vector3d> {

    @Override
    public Vector3d deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        double x = node.getNode("x").getDouble();
        double y = node.getNode("y").getDouble();
        double z = node.getNode("z").getDouble();

        return new Vector3d(x, y, z);
    }

    @Override
    public void serialize(TypeToken<?> type, Vector3d obj, ConfigurationNode value) throws ObjectMappingException
    {

    }

}
