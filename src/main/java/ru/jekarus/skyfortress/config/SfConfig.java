package ru.jekarus.skyfortress.config;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import ru.jekarus.skyfortress.Area3i;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.module.gui.enchantments.SfEnchantment;

import java.util.Arrays;

public class SfConfig {

    public static Area3i LEAVE = Area3i.of(
            new Vec3i(498, 101, -2),
            new Vec3i(502, 103, 2)
    );
    public static Vec3i emerald = new Vec3i(2500, 97, 0);
    public static Vec3i FORCE_START = new Vec3i(500, 101, 0);

    public record ObjectLoc(Vec3i loc, Vec3i dir) {

        public Location toLocation(World world) {
            return loc.toLocation(world).setDirection(dir.toVector().normalize());
        }
    }

    public static ObjectLoc[] DRAGONS = new ObjectLoc[] {
            new ObjectLoc(new Vec3i(2570, 96, -70), new Vec3i(-1, 0, 1)),
            new ObjectLoc(new Vec3i(2430, 96, 70), new Vec3i(1, 0, -1))
    };

    public static ObjectLoc[] WITHERS = new ObjectLoc[] {
            new ObjectLoc(new Vec3i(2430, 96, -70), new Vec3i(1, 0, 1)),
            new ObjectLoc(new Vec3i(2570, 96, 70), new Vec3i(-1, 0, -1))
    };

    public static final int OBJECT_HOME_RADIUS = 13;

    public static SfEnchantment[] ENCHANTMENT_TOOLS = new SfEnchantment[] {
            new SfEnchantment(
                    "Эффективность",
                    Enchantment.DIG_SPEED,
                    new int[]{
                            2, 2,
                            4, 4,
                            6, 6,
                    }
            ),
            new SfEnchantment(
                    "Удача",
                    Enchantment.LOOT_BONUS_BLOCKS,
                    new int[] {
                            0, 0,
                            6, 2,
                            6, 4,
                            6, 6,
                    }
            ),
            new SfEnchantment(
                    "Прочность",
                    Enchantment.DURABILITY,
                    new int[] {
                            2, 2,
                            4, 4,
                            6, 6,
                    }
            ),
    };

    public static SfEnchantment[] ENCHANTMENT_ARMOR = new SfEnchantment[] {
            new SfEnchantment(
                    "Защита",
                    Enchantment.PROTECTION_ENVIRONMENTAL,
                    new int[]{
                            3, 1,
                            4, 2,
                            6, 3,
                            7, 4,
                    },
                    new Enchantment[] {
                            Enchantment.PROTECTION_FIRE,
                            Enchantment.PROTECTION_PROJECTILE,
                            Enchantment.PROTECTION_EXPLOSIONS
                    }
            ),
            new SfEnchantment(
                    "Невесомость",
                    Enchantment.PROTECTION_FALL,
                    new int[] {
                            1, 2,
                            2, 3,
                            3, 4,
                            4, 5,
                    },
                    new Material[] {
                            Material.CHAINMAIL_BOOTS,
                            Material.IRON_BOOTS,
                            Material.GOLDEN_BOOTS,
                            Material.DIAMOND_BOOTS
                    }
            ),
            new SfEnchantment(
                    "Огнеупоротость",
                    Enchantment.PROTECTION_FIRE,
                    new int[] {
                            0, 0,
                            3, 2,
                            0, 0,
                            5, 5,
                    },
                    new Enchantment[] {
                            Enchantment.PROTECTION_ENVIRONMENTAL,
                            Enchantment.PROTECTION_PROJECTILE,
                            Enchantment.PROTECTION_EXPLOSIONS
                    }
            ),
            new SfEnchantment(
                    "Защита от снарядов",
                    Enchantment.PROTECTION_PROJECTILE,
                    new int[] {
                            3, 2,
                            4, 3,
                            5, 4,
                            6, 5,
                    },
                    new Enchantment[] {
                            Enchantment.PROTECTION_ENVIRONMENTAL,
                            Enchantment.PROTECTION_FIRE,
                            Enchantment.PROTECTION_EXPLOSIONS
                    }
            ),
            new SfEnchantment(
                    "Взрывоустойчивость",
                    Enchantment.PROTECTION_EXPLOSIONS,
                    new int[] {
                            0, 0,
                            0, 0,
                            0, 0,
                            3, 3,
                    },
                    new Enchantment[] {
                            Enchantment.PROTECTION_ENVIRONMENTAL,
                            Enchantment.PROTECTION_FIRE,
                            Enchantment.PROTECTION_PROJECTILE
                    }
            ),
            new SfEnchantment(
                    "Шипы",
                    Enchantment.THORNS,
                    new int[] {
                            4, 5,
                            5, 6,
                            6, 7,
                    }
            ),
            new SfEnchantment(
                    "Прочность",
                    Enchantment.DURABILITY,
                    new int[] {
                            2, 2,
                            3, 4,
                            4, 6,
                    }
            ),
    };

    public static SfEnchantment[] ENCHANTMENT_SWORDS = new SfEnchantment[] {
            new SfEnchantment(
                    "Острота",
                    Enchantment.DAMAGE_ALL,
                    new int[]{
                            2, 1,
                            3, 2,
                            3, 3,
                            3, 4,
                            4, 5,
                    }
            ),
            new SfEnchantment(
                    "Разящий клинок",
                    Enchantment.SWEEPING_EDGE,
                    new int[] {
                            2, 3,
                            2, 4,
                            2, 5,
                    }
            ),
            new SfEnchantment(
                    "Отдача",
                    Enchantment.KNOCKBACK,
                    new int[] {
                            4, 4,
                            4, 5,
                    }
            ),
            new SfEnchantment(
                    "Заговор огня",
                    Enchantment.FIRE_ASPECT,
                    new int[] {
                            3, 4,
                            4, 6,
                    }
            ),
            new SfEnchantment(
                    "Прочность",
                    Enchantment.DURABILITY,
                    new int[] {
                            2, 2,
                            4, 4,
                            6, 6,
                    }
            ),
    };

    public static SfEnchantment[] ENCHANTMENT_BOW = new SfEnchantment[] {
            new SfEnchantment(
                    "Сила",
                    Enchantment.ARROW_DAMAGE,
                    new int[]{
                            4, 1,
                            5, 2,
                            6, 3,
                            9, 5,
                            11, 7,
                    }
            ),
            new SfEnchantment(
                    "Отбрасывание",
                    Enchantment.ARROW_KNOCKBACK,
                    new int[] {
                            5, 4,
                            7, 6,
                    }
            ),
            new SfEnchantment(
                    "Горящая стрела",
                    Enchantment.ARROW_FIRE,
                    new int[] {
                            8, 4,
                    }
            ),
            new SfEnchantment(
                    "Прочность",
                    Enchantment.DURABILITY,
                    new int[] {
                            2, 2,
                            4, 4,
                            6, 6,
                    }
            ),
    };

    public static SfEnchantment[] ENCHANTMENT_CROSSBOW = new SfEnchantment[] {
            new SfEnchantment(
                    "Быстрая перезарядка",
                    Enchantment.QUICK_CHARGE,
                    new int[]{
                            3, 2,
                            3, 4,
                            3, 5,
                    }
            ),
            new SfEnchantment(
                    "Пронзающая стрела",
                    Enchantment.PIERCING,
                    new int[]{
                            0, 0,
                            0, 0,
                            0, 0,
                            3, 3,
                    }
            ),
            new SfEnchantment(
                    "Прочность",
                    Enchantment.DURABILITY,
                    new int[] {
                            2, 2,
                            4, 4,
                            6, 6,
                    }
            ),
    };

    public static SfEnchantment[] ENCHANTMENT_TRIDENT = new SfEnchantment[] {
            new SfEnchantment(
                    "Верность",
                    Enchantment.LOYALTY,
                    new int[]{
                            0, 0,
                            0, 0,
                            2, 1,
                    }
            ),
            new SfEnchantment(
                    "Прочность",
                    Enchantment.DURABILITY,
                    new int[] {
                            1, 2,
                            1, 4,
                            1, 6,
                    }
            ),
    };

}
