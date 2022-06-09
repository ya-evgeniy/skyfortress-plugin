package ru.jekarus.skyfortress.config;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import ru.jekarus.skyfortress.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SfShop {

    public static ArrayList<Entity> spawn(World world, Vec3i[] shops, BlockFace face) {
        final var list = new ArrayList<Entity>();
        var loc = shops[0].toLocation(world);
        loc.setY(loc.getBlockY());
        loc.setDirection(face.getDirection());
        loc.setYaw(loc.getYaw() + 90);
        list.add(world.spawnEntity(loc, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.DEFAULT, en -> {
            Villager villager = (Villager) en;
            buildShop1(villager);
        }));
        loc = shops[1].toLocation(world);
        loc.setY(loc.getBlockY());
        loc.setDirection(face.getDirection());
        loc.setYaw(loc.getYaw() + 90);
        list.add(world.spawnEntity(loc, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.DEFAULT, en -> {
            Villager villager = (Villager) en;
            buildShop2(villager);
        }));
        return list;
    }

    private static MerchantRecipe build(Supplier<ItemStack> out, Consumer<MerchantRecipe> fmt) {
        final var recipe = new MerchantRecipe(out.get(), 2000000000);
        fmt.accept(recipe);
        return recipe;
    }

    public static void buildShop1(Villager entity) {
        entity.customName(Component.text("§6§lМАГАЗИН"));
        entity.setVillagerType(Villager.Type.PLAINS);
        entity.setProfession(Villager.Profession.LIBRARIAN);
        entity.setVillagerLevel(4);
        entity.setInvulnerable(true);
        entity.setAI(false);
        entity.setSilent(true);
        entity.setRecipes(Arrays.asList(
                build(() -> new ItemStack(Material.GOLD_INGOT, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 10));
                }),
                build(() -> new ItemStack(Material.IRON_INGOT, 8), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT));
                }),
                build(() -> new ItemStack(Material.IRON_INGOT, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.COBBLESTONE, 10));
                }),
                build(() -> new ItemStack(Material.IRON_INGOT, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.OAK_PLANKS, 10));
                }),
                build(() -> new ItemStack(Material.BOW, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 3));
                }),
                build(() -> new ItemStack(Material.DIAMOND_AXE, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 6));
                    recipe.addIngredient(new ItemStack(Material.IRON_AXE));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.SHEARS, 1);
                    is.addEnchantment(Enchantment.DIG_SPEED, 5);
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));
                }),
                build(() -> new ItemStack(Material.WHITE_WOOL, 16), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 1));
                }),
                build(() -> new ItemStack(Material.BLACK_CONCRETE, 64), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));
                }),
                build(() -> new ItemStack(Material.APPLE, 8), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));
                }),
                build(() -> new ItemStack(Material.ARROW, 8), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));
                }),
                build(() -> new ItemStack(Material.DIAMOND_SWORD, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 6));
                    recipe.addIngredient(new ItemStack(Material.IRON_SWORD, 1));
                }),
                build(() -> new ItemStack(Material.SPECTRAL_ARROW, 8), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 1));
                })
        ));
    }

    public static void buildShop2(Villager entity) {
        entity.customName(Component.text("§6§lМАГАЗИН"));
        entity.setVillagerType(Villager.Type.PLAINS);
        entity.setProfession(Villager.Profession.FARMER);
        entity.setVillagerLevel(4);
        entity.setInvulnerable(true);
        entity.setAI(false);
        entity.setSilent(true);
        entity.setRecipes(Arrays.asList(
                build(() -> new ItemStack(Material.ENCHANTING_TABLE, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 32));
                }),
                build(() -> new ItemStack(Material.BOOKSHELF, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT));
                }),
                build(() -> new ItemStack(Material.EXPERIENCE_BOTTLE, 16), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 1));
                }),
                build(() -> new ItemStack(Material.FLINT_AND_STEEL, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));
                }),
                build(() -> new ItemStack(Material.TNT, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 2));
                }),
                build(() -> new ItemStack(Material.ENDER_PEARL, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 8));
                }),
                build(() -> new ItemStack(Material.TOTEM_OF_UNDYING, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.GOLD_INGOT, 16));
                })
        ));
    }

    /*
    инф павер 4 панч 2
    флейм повер 4
    анбрекинг 3 флейм повер 4
    плейм повер 4 панч 2
     */

}
