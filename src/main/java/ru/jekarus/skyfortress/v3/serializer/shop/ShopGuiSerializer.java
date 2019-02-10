package ru.jekarus.skyfortress.v3.serializer.shop;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import ru.jekarus.jgui.gui.content.GuiContent;
import ru.jekarus.jgui.gui.slot.GuiSlot;
import ru.jekarus.jgui.gui.slot.item.GuiItem;
import ru.jekarus.skyfortress.v3.gui.ShopGui;
import ru.jekarus.skyfortress.v3.gui.ShopItem;
import ru.jekarus.skyfortress.v3.gui.ShopSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShopGuiSerializer implements TypeSerializer<ShopGui> {

    @Override
    public ShopGui deserialize(TypeToken<?> typeToken, ConfigurationNode node) throws ObjectMappingException
    {
        Text title = node.getNode("title").getValue(TypeToken.of(Text.class));
        int rows = node.getNode("rows").getInt(6);

        ShopGui shop = new ShopGui("shop", title, rows);
        this.deserializeSlots(typeToken, node.getNode("slots"), shop);

        return shop;
    }

    private void deserializeSlots(TypeToken<?> typeToken, ConfigurationNode node, ShopGui shop) throws ObjectMappingException
    {
        for (ConfigurationNode slotNode : node.getChildrenList())
        {
            int index = slotNode.getNode("index").getInt();
            ItemStack buy = slotNode.getNode("buy").getValue(TypeToken.of(ItemStack.class));
            List<ItemStack> items = new ArrayList<>();
            for (ConfigurationNode priceNode : slotNode.getNode("price").getChildrenList())
            {
                items.add(priceNode.getValue(TypeToken.of(ItemStack.class)));
            }
            ItemStack[] price = new ItemStack[items.size()];
            for (int ind = 0; ind < items.size(); ind++)
            {
                price[ind] = items.get(ind);
            }
            shop.getContent().ifPresent(content -> {
                content.setSlot(index, new ShopSlot(index, new ShopItem(buy, price)));
            });
        }
    }

    @Override
    public void serialize(TypeToken<?> typeToken, ShopGui shop, ConfigurationNode node) throws ObjectMappingException
    {
        node.getNode("title").setValue(TypeToken.of(Text.class), shop.getTitle());
        node.getNode("rows").setValue(shop.getRows());

        this.serializeSlots(typeToken, Objects.requireNonNull(shop.getContent().orElse(null)), node.getNode("slots"));
    }

    private void serializeSlots(TypeToken<?> typeToken, GuiContent content, ConfigurationNode node) throws ObjectMappingException
    {
        node.setValue(ImmutableList.of());
        for (int index = 0; index < content.getLength(); index++)
        {
            Optional<? extends GuiSlot> optionalSlot = content.getSlot(index);
            if (optionalSlot.isPresent())
            {
                ShopSlot slot = (ShopSlot) optionalSlot.get();
                Optional<GuiItem> optionalItem = slot.getItem();
                if (optionalItem.isPresent())
                {
                    ShopItem item = (ShopItem) optionalItem.get();
                    ConfigurationNode slotNode = node.getAppendedNode();
                    slotNode.getNode("index").setValue(slot.getIndex());
                    slotNode.getNode("buy").setValue(TypeToken.of(ItemStack.class), item.getBuy());
                    ConfigurationNode price = slotNode.getNode("price");
                    price.setValue(ImmutableList.of());
                    for (ItemStack stack : item.getPrice())
                    {
                        price.getAppendedNode().setValue(TypeToken.of(ItemStack.class), stack);
                    }
                }
            }
        }
    }

}
