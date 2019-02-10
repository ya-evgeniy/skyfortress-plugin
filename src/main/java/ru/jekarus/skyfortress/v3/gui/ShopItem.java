package ru.jekarus.skyfortress.v3.gui;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.item.inventory.type.CarriedInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TranslatableText;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.jgui.gui.slot.item.GuiItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopItem extends GuiItem {

    private ItemStack buy;
    private ItemStack[] price;

    private boolean placeInMainHand = false;

    public ShopItem()
    {

    }

    public ShopItem(ItemStack buy, ItemStack price)
    {
        this(buy, new ItemStack[] {price});
    }

    public ShopItem(ItemStack buy, ItemStack[] price)
    {
        this.buy = buy;
        this.price = price;

        this.initDisplay();

        this.buy.remove(Keys.DISPLAY_NAME);
    }

    public void initDisplay()
    {
        ItemStack display = ItemStack.builder()
                .from(this.buy)

                .add(Keys.DISPLAY_NAME, this.constructDisplay(this.buy))
                .add(Keys.ITEM_LORE, this.constructLore())

                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.HIDE_MISCELLANEOUS, true)
                .add(Keys.HIDE_ENCHANTMENTS, true)
                .add(Keys.HIDE_CAN_PLACE, true)
                .add(Keys.HIDE_CAN_DESTROY, true)
                .add(Keys.HIDE_ATTRIBUTES, true)

                .build();

        this.setItemStack(display);
    }

    private List<Text> constructLore()
    {
        List<Text> result = new ArrayList<>();

        result.add(
                TranslatableText.builder().append(Text.of("Стоимость:")).color(TextColors.WHITE).build()
        );

        boolean needFootnote = false;
        for (ItemStack stack : this.price)
        {
            needFootnote = needFootnote || stack.getMaxStackQuantity() == 1;
            result.add(
                    TranslatableText.builder()
                            .append(
                                    Text.of("- ").toBuilder().color(TextColors.GRAY).build()
                            )
                            .append(
                                    this.constructLoreDisplay(stack)
                            )
                            .build()
            );
        }

        if (needFootnote)
        {
            this.placeInMainHand = true;
            result.add(Text.of());
            result.add(
                    Text.builder()
                            .append(
                                    Text.of("*").toBuilder().color(TextColors.RED).build()
                            )
                            .append(
                                    Text.of(" - Данный предмет должен").toBuilder().color(TextColors.GRAY).build()
                            )
                            .build()
            );
            result.add(Text.of("находиться в руке").toBuilder().color(TextColors.GRAY).build());
        }

        return result;
    }

    private Text constructDisplay(ItemStack stack)
    {
        return TranslatableText.builder()
                .append(
                        stack.get(Keys.DISPLAY_NAME).orElse(Text.of(stack.getType().getName())).toBuilder().color(TextColors.GOLD).build()
                )
                .append(
                        Text.of(" x " + stack.getQuantity()).toBuilder().color(TextColors.GRAY).build()
                )
                .build();
    }

    private Text constructLoreDisplay(ItemStack stack)
    {
        if (stack.getMaxStackQuantity() == 1)
        {
            return TranslatableText.builder()
                    .append(
                            stack.get(Keys.DISPLAY_NAME).orElse(Text.of(stack.getType().getName())).toBuilder().color(TextColors.GOLD).build()
                    )
                    .append(
                            Text.of(" *").toBuilder().color(TextColors.RED).build()
                    )
                    .build();
        }
        else
        {
            return TranslatableText.builder()
                    .append(
                            stack.get(Keys.DISPLAY_NAME).orElse(Text.of(stack.getType().getName())).toBuilder().color(TextColors.GOLD).build()
                    )
                    .append(
                            Text.of(" x " + stack.getQuantity()).toBuilder().color(TextColors.GRAY).build()
                    )
                    .build();
        }
    }

    public void checkPrice(Player player)
    {
        for (ItemStack stack : this.price)
        {
            if (!this.checkPrice(player, stack))
            {
                return;
            }
        }

        this.buy(player);
    }

    private boolean checkPrice(Player player, ItemStack price)
    {
        CarriedInventory<? extends Carrier> inventory = player.getInventory();
        Iterable<Slot> slots = inventory.slots();

        boolean needMainHand = price.getMaxStackQuantity() == 1;
        int need = price.getQuantity();
        int have = 0;

        if (needMainHand)
        {
            Optional<ItemStack> optionalItem = player.getItemInHand(HandTypes.MAIN_HAND);
            return optionalItem.map(itemStack -> itemStack.getType().equals(price.getType())).orElse(false);
        }

        for (Slot slot : slots)
        {
            Optional<ItemStack> optionalItemStack = slot.peek();
            if (optionalItemStack.isPresent())
            {
                ItemStack stack = optionalItemStack.get();
                if (!stack.getType().equals(price.getType()))
                {
                    continue;
                }
                have += stack.getQuantity();
            }
        }

        return need <= have;
    }

    public void buy(Player player)
    {
        for (ItemStack stack : this.price)
        {
            this.buy(player, stack);
        }

        if (this.placeInMainHand)
        {
            player.setItemInHand(HandTypes.MAIN_HAND, this.buy.copy());
        }
        else
        {
            Location<World> location = player.getLocation();
            Inventory hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
            InventoryTransactionResult result = hotbar.offer(this.buy.copy());
//            InventoryTransactionResult result = player.getInventory().offer(this.buy.copy());
            if (!result.getRejectedItems().isEmpty())
            {
                Inventory playerInventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));
                for (ItemStackSnapshot stackSnapshot : result.getRejectedItems())
                {
                    InventoryTransactionResult resultOffer = playerInventory.offer(stackSnapshot.createStack());
                    for (ItemStackSnapshot itemStackSnapshot : resultOffer.getRejectedItems())
                    {
                        Item item = (Item) location.createEntity(EntityTypes.ITEM);
                        item.offer(Keys.REPRESENTED_ITEM, itemStackSnapshot);
                        item.offer(Keys.VELOCITY, Vector3d.from(0));
                        location.spawnEntity(item);
                    }
                }
            }
//            for (ItemStackSnapshot itemStackSnapshot : result.getRejectedItems())
//            {
//                Item item = (Item) location.createEntity(EntityTypes.ITEM);
//                item.offer(Keys.REPRESENTED_ITEM, itemStackSnapshot);
//                item.offer(Keys.VELOCITY, Vector3d.from(0));
//                location.spawnEntity(item);
//            }
        }
    }

    private void buy(Player player, ItemStack price)
    {
        if (price.getMaxStackQuantity() == 1)
        {
            return;
        }
        int needRemove = price.getQuantity();
        int left = 0;

        CarriedInventory<? extends Carrier> inventory = player.getInventory();
        Iterable<Slot> slots = inventory.slots();

        for (Slot slot : slots)
        {
            Optional<ItemStack> optionalItemStack = slot.peek();
            if (optionalItemStack.isPresent())
            {
                ItemStack stack = optionalItemStack.get();
                if (!stack.getType().equals(price.getType()))
                {
                    continue;
                }
                left = stack.getQuantity() - needRemove;
                needRemove -= stack.getQuantity();

                stack.setQuantity(left < 0 ? 0 : left);
                slot.set(stack);

                if (needRemove < 1)
                {
                    return;
                }
            }
        }
    }

    public ItemStack getBuy()
    {
        return this.buy;
    }

    public ItemStack[] getPrice()
    {
        return this.price;
    }
}
