package ru.jekarus.skyfortress.module.object;

import fr.mrmicky.fastboard.FastReflection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.control.ControllerMoveFlying;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.pathfinder.PathPoint;
import net.minecraft.world.level.pathfinder.PathType;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMob;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftWither;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Wither;
import ru.jekarus.skyfortress.Vec3i;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WitherNms {

    private static Class<?> cl_ControllerMove;
    private static Class<?> cl_EntityInsentient;
    private static Field f_moveControl;
    private static Class<?> cl_EntityWither;
    private static Field f_noPhysics;
    private static Field f_noCulling;
    private static Field f_pathfindingMalus;
    private static Method m_getNavigation;
    private static Method m_isDone;

    static {

        try {
            cl_ControllerMove = FastReflection.nmsClass("world.entity.ai.control", "ControllerMove");
            cl_EntityInsentient = FastReflection.nmsClass("world.entity", "EntityInsentient");
            f_moveControl = Arrays.stream(cl_EntityInsentient.getDeclaredFields())
                    .filter(field -> field.getType().isAssignableFrom(cl_ControllerMove))
                    .findFirst().orElseThrow(NoSuchFieldException::new);
            f_moveControl.setAccessible(true);
            cl_EntityWither = FastReflection.nmsClass("world.entity.boss.wither", "EntityWither");
            f_noPhysics = Entity.class.getDeclaredField("Q");
            f_noPhysics.setAccessible(true);
            f_noCulling = Entity.class.getDeclaredField("ae");
            f_noCulling.setAccessible(true);
            f_pathfindingMalus = EntityInsentient.class.getDeclaredField("cc");
            f_pathfindingMalus.setAccessible(true);
            m_getNavigation = EntityInsentient.class.getDeclaredMethod("D");
            m_getNavigation.setAccessible(true);
            m_isDone = NavigationAbstract.class.getDeclaredMethod("l");
            m_isDone.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setWantedPosition(Wither entity, Vec3i vec, double speed) {
        final var nmsEntity = ((CraftWither) entity).getHandle();
        nmsEntity.A().a(vec.x(), vec.y(), vec.z(), speed);
    }
    public static boolean followPath(Wither entity, @Nonnull List<Vec3i> path, double speed) {
        final var nmsEntity = ((CraftWither) entity).getHandle();

        final var points = new ArrayList<PathPoint>();
        PathPoint pp = null;
        for (Vec3i vec : path) {
            if(pp == null) {
                pp = new PathPoint(vec.x(), vec.y(), vec.z());
            } else {
                pp = pp.a(vec.x(), vec.y(), vec.z());
            }
            points.add(pp);
        }
        return nmsEntity.D().a(new PathEntity(
                points,
                pp.a(),
                true
        ), speed);
    }
    public static boolean isDoneMoving(Wither entity) {
        final var nmsEntity = ((CraftWither) entity).getHandle();
//        var nav = m_getNavigation.invoke(nmsEntity)
//        return m_isDone.invoke(nav);
        return nmsEntity.D().l();
    }
    public static void setBlockMalus(Mob entity, float value) {
        final var nmsEntity = ((CraftMob) entity).getHandle();
        try {
            Map<PathType, Float> pathfindingMalus = (Map<PathType, Float>) f_pathfindingMalus.get(nmsEntity);
            pathfindingMalus.put(PathType.a, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPhysics(org.bukkit.entity.Entity entity, boolean physics) {
        final var nmsEntity = ((CraftEntity) entity).getHandle();
        try {
            f_noPhysics.set(nmsEntity, !physics);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCulling(org.bukkit.entity.Entity entity, boolean culling) {
        final var nmsEntity = ((CraftEntity) entity).getHandle();
        try {
            f_noCulling.set(nmsEntity, !culling);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setGravity(org.bukkit.entity.Entity entity, boolean gravity) {
        final var nmsEntity = ((CraftWither) entity).getHandle();
        try {
            f_moveControl.set(nmsEntity, new ControllerMoveFlying(nmsEntity, 10, !gravity));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        entity.setGravity(gravity);
    }

    public static void setBodyRotation(org.bukkit.entity.Entity entity, float yaw) {
        final var nmsEntity = ((CraftWither) entity).getHandle();
        nmsEntity.m(yaw);
    }

}
