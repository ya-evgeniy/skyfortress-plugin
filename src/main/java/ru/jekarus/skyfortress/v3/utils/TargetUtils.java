package ru.jekarus.skyfortress.v3.utils;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TargetUtils {

    public static <T extends Supplier<Optional<Entity>>> Optional<T> getCustomTargetEntity(Entity entity, double d, List<T> entities) {
        if (entity == null) return Optional.empty();
        Vector3d viewVector = processVector(entity.getRotation());

        Location<World> startPosition = entity.getLocation().add(new Vector3d(0, 1, 0)).add(viewVector).add(viewVector);

        double projectionLength;
        Vector3d headPosition;
        T targetEntity = null;

        int count = 0;
        for (T entitySupplier : entities) {
            Optional<Entity> optionalSelectedEntity = entitySupplier.get();
            if (!optionalSelectedEntity.isPresent()) {
                count += 1;
                continue;
            }
            Entity selectedEntity = optionalSelectedEntity.get();
            headPosition = selectedEntity.getLocation().getPosition().add(0, 0.5, 0);
            projectionLength = findD(startPosition.getPosition(), viewVector, headPosition);
            if (projectionLength <= d) {
                d = projectionLength;
                targetEntity = entitySupplier;
            }
        }
        System.out.println("Targets skipped " + count + "/" + entities.size());
        return Optional.ofNullable(targetEntity);
    }

    public static Optional<Entity> getTargetEntity(Entity entity, double d, List<Entity> entities) {
        if (entity == null) return Optional.empty();
        Vector3d viewVector = processVector(entity.getRotation());

        Location<World> startPosition = entity.getLocation().add(new Vector3d(0, 1, 0)).add(viewVector).add(viewVector);

        double projectionLength;
        Vector3d headPosition;
        Entity targetEntity = null;

        for (Entity selectedEntity : entities) {
            headPosition = selectedEntity.getLocation().getPosition().add(0, 0.5, 0);
            projectionLength = findD(startPosition.getPosition(), viewVector, headPosition);
            if (projectionLength <= d) {
                d = projectionLength;
                targetEntity = selectedEntity;
            }
        }
        return Optional.ofNullable(targetEntity);
    }

    public static Optional<Entity> getTargetEntity(Entity entity, int radius, double d, EntityType... types) {
        if (entity == null) return Optional.empty();
        Vector3d viewVector = processVector(entity.getRotation());

        Location<World> startPosition = entity.getLocation().add(new Vector3d(0, 1, 0)).add(viewVector).add(viewVector);

        double halfRadius = (double) radius / 2;
        Location<World> center = startPosition.add(viewVector.mul(halfRadius));

        Collection<Entity> selectedEntities = center.getExtent().getNearbyEntities(center.getPosition(), radius);
        double projectionLength;
        Vector3d headPosition;
        Entity targetEntity = null;

        List<EntityType> typesList = Arrays.asList(types);

        for (Entity selectedEntity : selectedEntities) {
            if (!typesList.isEmpty() && !typesList.contains(selectedEntity.getType())) {
                continue;
            }
            headPosition = selectedEntity.getLocation().getPosition().add(0, 0.5, 0);
            if (headPosition.distance(center.getPosition()) <= halfRadius) {
                projectionLength = findD(startPosition.getPosition(), viewVector, headPosition);
                if (projectionLength <= d) {
                    d = projectionLength;
                    targetEntity = selectedEntity;
                }
            }
        }
        return Optional.ofNullable(targetEntity);
    }

    private static Vector3d processVector(Vector3d vector) {
        double x, y, z;

        double rotX = vector.getY();
        double rotY = vector.getX();

        y = -Math.sin(Math.toRadians(rotY));

        double xz = Math.cos(Math.toRadians(rotY));

        x = -xz * Math.sin(Math.toRadians(rotX));
        z = xz * Math.cos(Math.toRadians(rotX));

        return new Vector3d(x, y, z);
    }

    private static Double findD(Vector3d position, Vector3d viewVector, Vector3d headPosition) {
        double x = headPosition.getX();
        double y = headPosition.getY();
        double z = headPosition.getZ();

        double x0 = position.getX();
        double y0 = position.getY();
        double z0 = position.getZ();

        double vx = viewVector.getX();
        double vy = viewVector.getY();
        double vz = viewVector.getZ();

        final Double a = Math.pow((y0 - y) * vz - (z0 - z) * vy, 2) + Math.pow((z0 - z) * vx - (x0 - x) * vz, 2) + Math.pow((x0 - x) * vy - (y0 - y) * vx, 2);
        final Double b = Math.pow(vx, 2) + Math.pow(vy, 2) + Math.pow(vz, 2);

        return Math.pow(a / b, 0.5);
    }

}