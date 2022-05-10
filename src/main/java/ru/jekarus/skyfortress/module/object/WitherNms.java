package ru.jekarus.skyfortress.module.object;

import fr.mrmicky.fastboard.FastReflection;
import net.minecraft.world.entity.ai.control.ControllerMoveFlying;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftWither;
import org.bukkit.entity.Wither;

import java.lang.reflect.Field;
import java.util.Arrays;

public class WitherNms {

    private static Class<?> cl_ControllerMove;
    private static Class<?> cl_EntityInsentient;
    private static Field f_moveControl;
    private static Class<?> cl_EntityWither;

    static {

        try {
            cl_ControllerMove = FastReflection.nmsClass("world.entity.ai.control", "ControllerMove");
            cl_EntityInsentient = FastReflection.nmsClass("world.entity", "EntityInsentient");
            f_moveControl = Arrays.stream(cl_EntityInsentient.getDeclaredFields())
                    .filter(field -> field.getType().isAssignableFrom(cl_ControllerMove))
                    .findFirst().orElseThrow(NoSuchFieldException::new);
            f_moveControl.setAccessible(true);
            cl_EntityWither = FastReflection.nmsClass("world.entity.boss.wither", "EntityWither");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setGravity(Wither entity, boolean gravity) {
        final var nmsEntity = ((CraftWither) entity).getHandle();
        try {
            f_moveControl.set(nmsEntity, new ControllerMoveFlying(nmsEntity, 10, !gravity));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        entity.setGravity(gravity);
    }

}
