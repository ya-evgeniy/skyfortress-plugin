package ru.jekarus.skyfortress.config_sf2;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import ru.jekarus.skyfortress.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SfShop {

    public static ArrayList<Entity> spawn(World world, Vec3i[] shops, BlockFace face) {
        final var list = new ArrayList<Entity>();
        var loc = shops[0].toLocation(world);
        loc.setDirection(face.getDirection());
        loc.setYaw(loc.getYaw() - 90);
        list.add(world.spawnEntity(loc, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.DEFAULT, en -> {
            Villager villager = (Villager) en;
            buildShop1(villager);
        }));
        loc = shops[1].toLocation(world);
        loc.setDirection(face.getDirection());
        loc.setYaw(loc.getYaw() - 90);
        list.add(world.spawnEntity(loc, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.DEFAULT, en -> {
            Villager villager = (Villager) en;
            buildShop2(villager);
        }));
        loc = shops[2].toLocation(world);
        loc.setDirection(face.getDirection());
        loc.setYaw(loc.getYaw() - 90);
        list.add(world.spawnEntity(loc, EntityType.VILLAGER, CreatureSpawnEvent.SpawnReason.DEFAULT, en -> {
            Villager villager = (Villager) en;
            buildShop3(villager);
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
                build(() -> new ItemStack(Material.DIAMOND_SWORD, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 6));
                    recipe.addIngredient(new ItemStack(Material.IRON_SWORD, 1));
                }),
                build(() -> new ItemStack(Material.BOW, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 3));
                }),
                build(() -> new ItemStack(Material.ARROW, 32), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.BOOKSHELF, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.EXPERIENCE_BOTTLE, 16), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.ENDER_PEARL, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 30));
                }),
                build(() -> new ItemStack(Material.EMERALD, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.IRON_INGOT, 10));
                }),
                build(() -> new ItemStack(Material.IRON_INGOT, 5), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.DIRT, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 64));
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 64));
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
                build(() -> new ItemStack(Material.APPLE, 8), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.GOLD_INGOT, 6), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.TNT, 3), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                }),
                build(() -> new ItemStack(Material.FLINT_AND_STEEL, 1), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 2));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.BOW, 1);
                    is.addEnchantment(Enchantment.ARROW_FIRE, 10);
                    is.addEnchantment(Enchantment.ARROW_INFINITE, 10);
                    is.addEnchantment(Enchantment.ARROW_DAMAGE, 500);
                    is.editMeta(Damageable.class, im -> {
                        im.setDamage(384);
                    });
                    is.editMeta(Repairable.class, im -> {
                        im.setRepairCost(1335);
                    });
                    is.editMeta(ItemMeta.class, im -> {
                        im.displayName(Component.text("Anti Golden Apple", NamedTextColor.YELLOW, TextDecoration.BOLD));
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 30));
                }),
                build(() -> new ItemStack(Material.LAPIS_LAZULI, 3), recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
                })
        ));
    }

    public static void buildShop3(Villager entity) {
        entity.customName(Component.text("§6§lМАГАЗИН"));
        entity.setVillagerType(Villager.Type.PLAINS);
        entity.setProfession(Villager.Profession.CLERIC);
        entity.setVillagerLevel(4);
        entity.setInvulnerable(true);
        entity.setAI(false);
        entity.setSilent(true);
        entity.setRecipes(Arrays.asList(
                build(() -> {
                    final var is = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    is.editMeta(EnchantmentStorageMeta.class, im -> {
                        im.addStoredEnchant(Enchantment.ARROW_INFINITE, 1, false);
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 15));
                    recipe.addIngredient(new ItemStack(Material.BOOK, 1));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    is.editMeta(EnchantmentStorageMeta.class, im -> {
                        im.addStoredEnchant(Enchantment.ARROW_FIRE, 1, false);
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 25));
                    recipe.addIngredient(new ItemStack(Material.BOOK, 1));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    is.editMeta(EnchantmentStorageMeta.class, im -> {
                        im.addStoredEnchant(Enchantment.ARROW_DAMAGE, 3, false);
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 10));
                    recipe.addIngredient(new ItemStack(Material.BOOK, 1));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    is.editMeta(EnchantmentStorageMeta.class, im -> {
                        im.addStoredEnchant(Enchantment.DAMAGE_ALL, 3, false);
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 10));
                    recipe.addIngredient(new ItemStack(Material.BOOK, 1));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    is.editMeta(EnchantmentStorageMeta.class, im -> {
                        im.addStoredEnchant(Enchantment.PROTECTION_FALL, 4, false);
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 10));
                    recipe.addIngredient(new ItemStack(Material.BOOK, 1));
                }),
                build(() -> {
                    final var is = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    is.editMeta(EnchantmentStorageMeta.class, im -> {
                        im.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false);
                    });
                    return is;
                }, recipe -> {
                    recipe.addIngredient(new ItemStack(Material.EMERALD, 10));
                    recipe.addIngredient(new ItemStack(Material.BOOK, 1));
                })
        ));
    }

}
