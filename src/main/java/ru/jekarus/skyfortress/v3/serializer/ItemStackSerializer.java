package ru.jekarus.skyfortress.v3.serializer;

import com.google.common.reflect.TypeToken;
import lombok.val;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ItemStackSerializer {

    public static ItemStack deserializeItemStack(CommentedConfigurationNode node) {
        return deserializeItemStack(
                node,
                single -> getValue(single, Text.class, Text.of("null")),
                list -> getList(list, Text.class)
        );
    }

    public static ItemStack deserializeItemStack(CommentedConfigurationNode node,
                                                 Function<CommentedConfigurationNode, Text> textConverter,
                                                 Function<CommentedConfigurationNode, List<Text>> listConverter) {
        if (node.isVirtual()) {
            return null;
        }

        final val typeString = node.getNode("type").getString();
        if (typeString == null) {
            return null;
        }

        final val type = Sponge.getRegistry().getType(ItemType.class, typeString).orElse(null);
        if (type == null) {
            return null;
        }

        final val quantity = node.getNode("quantity").getInt(1);
        final val stack = ItemStack.of(type, quantity);

        final val durability = node.getNode("durability").getInt(Integer.MIN_VALUE);
        if (durability != Integer.MIN_VALUE) {
            stack.offer(Keys.ITEM_DURABILITY, durability);
        }

        final val meta = node.getNode("meta");
        if (!meta.isVirtual()) {
            deserializeItemStackMeta(meta, stack);
        }

        return stack;
    }

    private static void deserializeItemStackMeta(CommentedConfigurationNode node, ItemStack stack) {
        deserializeItemStackMeta(
                node, stack,
                single -> getValue(single, Text.class, Text.of("null")),
                list -> getList(list, Text.class)
        );
    }

    private static void deserializeItemStackMeta(CommentedConfigurationNode node, ItemStack stack,
                                                 Function<CommentedConfigurationNode, Text> singleConverter,
                                                 Function<CommentedConfigurationNode, List<Text>> listConverter) {
        final val display = node.getNode("display");
        if (!display.isVirtual()) {
            stack.tryOffer(Keys.DISPLAY_NAME, singleConverter.apply(display));
        }

        final val lore = node.getNode("lore");
        if (!display.isVirtual()) {
            stack.tryOffer(Keys.ITEM_LORE, listConverter.apply(display));
        }

        final val enchantments = node.getNode("enchantments");
        if (!enchantments.isVirtual()) {
            deserializeItemStackEnchantments(enchantments, stack);
        }

        final val hidedFlags = node.getNode("hided_flags");
        if (!hidedFlags.isVirtual()) {
            deserializeItemStackHidedFlags(hidedFlags, stack);
        }

        ConfigurationNode dye_color = node.getNode("dye_color");
        if (!dye_color.isVirtual()) {
            final val dyeColorString = dye_color.getString("white");
            stack.tryOffer(
                    Keys.DYE_COLOR,
                    Sponge.getRegistry().getType(DyeColor.class, dyeColorString).orElse(DyeColors.WHITE)
            );
        }
    }

    private static void deserializeItemStackEnchantments(CommentedConfigurationNode node, ItemStack stack) {
        if (!node.hasMapChildren()) {
            return;
        }
        Deque<Enchantment> enchantments = new LinkedList<>();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.getChildrenMap().entrySet()) {
            final val objectKey = entry.getKey();
            if (objectKey instanceof String) {
                String key = (String) entry.getKey();
                final val optionalEnchantmentType = Sponge.getRegistry().getType(EnchantmentType.class, key);
                if (optionalEnchantmentType.isPresent()) {
                    final val enchantmentType = optionalEnchantmentType.get();
                    final val enchantmentLevel = entry.getValue().getInt(1);
                    enchantments.add(
                            Enchantment.of(enchantmentType, enchantmentLevel)
                    );
                }
            }
        }
        if (!enchantments.isEmpty())
        {
            stack.tryOffer(Keys.ITEM_ENCHANTMENTS, new ArrayList<>(enchantments));
        }
    }

    private static void deserializeItemStackHidedFlags(CommentedConfigurationNode node, ItemStack stack) {
        if (!node.hasMapChildren()) {
            return;
        }
        for (Map.Entry<Object, ? extends CommentedConfigurationNode> entry : node.getChildrenMap().entrySet()) {
            final val objectKey = entry.getKey();
            if (objectKey instanceof String) {
                final val key = (String) objectKey;
                String lower = key.toLowerCase();
                switch (lower)
                {
                    case "unbreakable":
                        stack.tryOffer(Keys.HIDE_UNBREAKABLE, node.getBoolean());
                        break;
                    case "attributes":
                        stack.tryOffer(Keys.HIDE_ATTRIBUTES, node.getBoolean());
                        break;
                    case "can_destroy":
                        stack.tryOffer(Keys.HIDE_CAN_DESTROY, node.getBoolean());
                        break;
                    case "can_place":
                        stack.tryOffer(Keys.HIDE_CAN_PLACE, node.getBoolean());
                        break;
                    case "enchantments":
                        stack.tryOffer(Keys.HIDE_ENCHANTMENTS, node.getBoolean());
                        break;
                    case "miscellaneous":
                        stack.tryOffer(Keys.HIDE_MISCELLANEOUS, node.getBoolean());
                        break;
                }
            }
            String flag = (String) entry.getKey();
        }
    }

    private static <T> T getValue(CommentedConfigurationNode node, Class<T> tokenType) {
        return getValue(node, tokenType, null);
    }

    private static <T> T getValue(CommentedConfigurationNode node, Class<T> tokenType, T defaultValue) {
        try {
            return node.getValue(TypeToken.of(tokenType), defaultValue);
        }
        catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private static <T> List<T> getList(CommentedConfigurationNode node, Class<T> tokenType) {
        try {
            return node.getList(TypeToken.of(tokenType));
        }
        catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
