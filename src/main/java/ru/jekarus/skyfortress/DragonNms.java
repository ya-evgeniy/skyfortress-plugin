package ru.jekarus.skyfortress;

import fr.mrmicky.fastboard.FastReflection;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonController;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.IDragonController;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DragonNms {
    private static Class<?> cl_Vec3D;
    private static Constructor<?> init_Vec3D;
    private static Class<?> cl_PathEntity;
    private static Class<?> cl_DragonControllerFly;
    private static Field f_currentPath;
    private static Field f_targetLocation;
    private static Method m_doServerTick;
    private static Class<?> cl_IDragonController;
    private static Class<?> cl_DragonControllerManager;
    private static Field f_phases;

    static {
        try {

            cl_Vec3D = FastReflection.nmsClass("world.phys", "Vec3D");
            init_Vec3D = cl_Vec3D.getDeclaredConstructor(double.class, double.class, double.class);

            cl_PathEntity = FastReflection.nmsClass("world.level.pathfinder", "PathEntity");

            cl_DragonControllerFly = FastReflection.nmsClass("world.entity.boss.enderdragon.phases", "DragonControllerFly");
            f_currentPath = Arrays.stream(cl_DragonControllerFly.getDeclaredFields())
                    .filter(field -> field.getType().isAssignableFrom(cl_PathEntity))
                    .findFirst().orElseThrow(NoSuchFieldException::new);
            f_currentPath.setAccessible(true);
            f_targetLocation = Arrays.stream(cl_DragonControllerFly.getDeclaredFields())
                    .filter(field -> field.getType().isAssignableFrom(cl_Vec3D))
                    .findFirst().orElseThrow(NoSuchFieldException::new);
            f_targetLocation.setAccessible(true);
            m_doServerTick = Arrays.stream(cl_DragonControllerFly.getDeclaredMethods())
                    .filter(meth -> meth.getParameterCount() == 0 && meth.getReturnType().isAssignableFrom(void.class))
                    .findFirst().orElseThrow(NoSuchFieldException::new);

            cl_IDragonController = FastReflection.nmsClass("world.entity.boss.enderdragon.phases", "IDragonController");
            cl_DragonControllerManager = FastReflection.nmsClass("world.entity.boss.enderdragon.phases", "DragonControllerManager");
            f_phases = Arrays.stream(cl_DragonControllerManager.getDeclaredFields())
                    .filter(field -> field.getType().isAssignableFrom(cl_IDragonController.arrayType()))
                    .findFirst().orElseThrow(NoSuchFieldException::new);
            f_phases.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class MyPhase extends AbstractDragonController {

        private final EnderDragon dragon;
        private final Location moveTo;
        private final Runnable onDone;
        private final double distanceSquared;

        public MyPhase(EnderDragon dragon, Location moveTo, double distance, Runnable onDone) {
            super(((CraftEnderDragon)dragon).getHandle());
            this.dragon = dragon;
            this.moveTo = moveTo;
            this.onDone = onDone;
            this.distanceSquared = distance * distance;
        }

        @Override
        public void c() {
            if(onDone == null) return;
            double d = dragon.getLocation().distanceSquared(moveTo);
            if (d < this.distanceSquared) {
                onDone.run();
            }
        }

        @Override
        public DragonControllerPhase<? extends IDragonController> i() {
            return CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL);
        }

        @Nullable
        @Override
        public Vec3D g() {
            return new Vec3D(moveTo.getX(), moveTo.getY(), moveTo.getZ());
        }

    }

//    static Location MoveTo;
//    private static DragonControllerPhase<MyPhase> myPhase;

    static {
//        myPhase = replaceDragonPhase(EnderDragon.Phase.LEAVE_PORTAL, MyPhase.class, "Fly");
    }

    public static void moveTo(EnderDragon entity, Location moveTo, double distance, @Nullable Runnable onDone) {
        EntityEnderDragon nmsEntity = ((CraftEnderDragon)entity).getHandle();
//        nmsEntity.fx().b(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL));
        try {
            final Object[] phases = (Object[]) f_phases.get(nmsEntity.fx());
            phases[EnderDragon.Phase.LEAVE_PORTAL.ordinal()] = new MyPhase(entity, moveTo, distance, onDone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // reload phase
        entity.setPhase(EnderDragon.Phase.HOVER);
        entity.setPhase(EnderDragon.Phase.LEAVE_PORTAL);
    }

}
