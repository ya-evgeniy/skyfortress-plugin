package ru.jekarus.jserializer.itemstack;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.*;

public class ItemStackSerializer implements TypeSerializer<ItemStack> {

    public static final TypeSerializerCollection SERIALIZERS = TypeSerializers.getDefaultSerializers().newChild();

    static {
        SERIALIZERS.registerType(TypeToken.of(ItemStack.class), new ItemStackSerializer());
        SERIALIZERS.registerType(TypeToken.of(Enchantment.class), new EnchantmentSerializer());
    }

    @Override
    public ItemStack deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        String typeStr = node.getNode("type").getString("minecraft:air");
        int quantity = node.getNode("quantity").getInt(1);
        int durability = node.getNode("durability").getInt(-1);

        ItemType itemType = Sponge.getRegistry().getType(ItemType.class, typeStr).orElse(ItemTypes.NONE);
        ItemStack stack = ItemStack.of(itemType, quantity);
        if (durability != -1)
        {
            stack.tryOffer(Keys.ITEM_DURABILITY, durability);
        }

        ConfigurationNode meta = node.getNode("meta");
        if (!meta.isVirtual())
        {
            this.deserializeMeta(typeToken, stack, meta);
        }

        return stack;
    }

    private void deserializeMeta(TypeToken<?> typeToken, ItemStack stack, ConfigurationNode node) throws ObjectMappingException
    {
        ConfigurationNode displayNode = node.getNode("display");
        if (!displayNode.isVirtual())
        {
            stack.tryOffer(Keys.DISPLAY_NAME, displayNode.getValue(TypeToken.of(Text.class)));
        }

        ConfigurationNode loreNode = node.getNode("lore");
        if (!loreNode.isVirtual())
        {
            stack.tryOffer(Keys.ITEM_LORE, loreNode.getList(TypeToken.of(Text.class)));
        }

        ConfigurationNode enchantmentsNode = node.getNode("enchantments");
        if (!enchantmentsNode.isVirtual())
        {
            Deque<Enchantment> enchantments = new LinkedList<>();
            if (enchantmentsNode.hasMapChildren())
            {
                for (Map.Entry<Object, ? extends ConfigurationNode> entry : enchantmentsNode.getChildrenMap().entrySet())
                {
                    String key = (String) entry.getKey();
                    Sponge.getRegistry().getType(EnchantmentType.class, key).ifPresent(type -> enchantments.addLast(Enchantment.of(type, entry.getValue().getInt(1))));
                }
            }
            if (!enchantments.isEmpty())
            {
                stack.tryOffer(Keys.ITEM_ENCHANTMENTS, new ArrayList<>(enchantments));
            }
        }

        ConfigurationNode hidedFlagsNode = node.getNode("hided_flags");
        if (!hidedFlagsNode.isVirtual() && hidedFlagsNode.hasMapChildren())
        {
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : hidedFlagsNode.getChildrenMap().entrySet())
            {
                String flag = (String) entry.getKey();
                this.deserializeHidedFlag(typeToken, stack, flag, entry.getValue());
            }
        }

        ConfigurationNode dye_color = node.getNode("dye_color");
        if (!dye_color.isVirtual())
        {
            stack.tryOffer(
                    Keys.DYE_COLOR,
                    Sponge.getRegistry().getType(DyeColor.class, dye_color.getString()).orElse(DyeColors.WHITE)
            );
        }

    }

    private void deserializeHidedFlag(TypeToken<?> typeToken, ItemStack stack, String flag, ConfigurationNode node)
    {
        String lower = flag.toLowerCase();
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

    @Override
    public void serialize(TypeToken<?> typeToken, ItemStack stack, ConfigurationNode node) throws ObjectMappingException
    {
        node.getNode("type").setValue(stack.getType().getName());
        node.getNode("quantity").setValue(stack.getQuantity());

        Optional<Integer> durability = stack.get(Keys.ITEM_DURABILITY);
        durability.ifPresent(integer -> node.getNode("durability").setValue(integer));

        ConfigurationNode meta = node.getNode("meta");
        this.serializeMeta(typeToken, stack, meta);
    }

    private void serializeMeta(TypeToken<?> typeToken, ItemStack stack, ConfigurationNode node) throws ObjectMappingException
    {

        Optional<Text> displayName = stack.get(Keys.DISPLAY_NAME);
        if (displayName.isPresent())
        {
            node.getNode("display").setValue(TypeToken.of(Text.class), displayName.get());
        }

        Optional<List<Text>> optionalLore = stack.get(Keys.ITEM_LORE);
        if (optionalLore.isPresent())
        {
            List<Text> lore = optionalLore.get();
            ConfigurationNode loreNode = node.getNode("lore");
            loreNode.setValue(ImmutableList.of());
            for (Text text : lore)
            {
                loreNode.getAppendedNode().setValue(TypeToken.of(Text.class), text);
            }
        }

        Optional<List<Enchantment>> optionalEnchantments = stack.get(Keys.ITEM_ENCHANTMENTS);
        if (optionalEnchantments.isPresent())
        {
            List<Enchantment> enchantments = optionalEnchantments.get();
            ConfigurationNode enchantmentsNode = node.getNode("enchantments");
            enchantmentsNode.setValue(ImmutableList.of());
            for (Enchantment enchantment : enchantments)
            {
                enchantmentsNode.getNode(enchantment.getType().getId()).setValue(enchantment.getLevel());
            }
        }

        this.serializeHidedFlags(typeToken, stack, node.getNode("hided_flags"));

        Optional<DyeColor> dyeColor = stack.get(Keys.DYE_COLOR);
        if (dyeColor.isPresent())
        {
            node.getNode("dye_color").setValue(dyeColor.get().getId());
        }
    }

    private void serializeHidedFlags(TypeToken<?> typeToken, ItemStack stack, ConfigurationNode node) throws ObjectMappingException
    {

        Optional<Boolean> unbreakable = stack.get(Keys.HIDE_UNBREAKABLE);
        if (unbreakable.isPresent() && unbreakable.get())
        {
            node.getNode("unbreakable").setValue(unbreakable.get());
        }

        Optional<Boolean> attributes = stack.get(Keys.HIDE_ATTRIBUTES);
        if (attributes.isPresent() && attributes.get())
        {
            node.getNode("attributes").setValue(attributes.get());
        }

        Optional<Boolean> canDestroy = stack.get(Keys.HIDE_CAN_DESTROY);
        if (canDestroy.isPresent() && canDestroy.get())
        {
            node.getNode("can_destroy").setValue(canDestroy.get());
        }

        Optional<Boolean> canPlace = stack.get(Keys.HIDE_CAN_PLACE);
        if (canPlace.isPresent() && canPlace.get())
        {
            node.getNode("can_place").setValue(canPlace.get());
        }

        Optional<Boolean> enchantments = stack.get(Keys.HIDE_ENCHANTMENTS);
        if (enchantments.isPresent() && enchantments.get())
        {
            node.getNode("enchantments").setValue(enchantments.get());
        }

        Optional<Boolean> miscellaneous = stack.get(Keys.HIDE_MISCELLANEOUS);
        if (miscellaneous.isPresent() && miscellaneous.get())
        {
            node.getNode("miscellaneous").setValue(miscellaneous.get());
        }

    }

}
