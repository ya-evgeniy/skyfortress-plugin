package ru.jekarus.skyfortress.v3.gui;

import com.flowpowered.math.vector.Vector3d;
import lombok.Getter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
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
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.lang.ShopMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopItem extends GuiItem {

    @Getter private TranslatedShopItem buy;
    @Getter private TranslatedShopItem[] price;

    private boolean placeInMainHand = false;

    public ShopItem() {

    }

    public ShopItem(TranslatedShopItem buy, TranslatedShopItem price) {
        this(buy, new TranslatedShopItem[]{ price });
    }

    public ShopItem(TranslatedShopItem buy, TranslatedShopItem[] price) {
        this.buy = buy;
        this.price = price;

        this.buy.getStack().remove(Keys.DISPLAY_NAME);
    }

    public void init(ShopMessages messages, SfLanguage language) {
        ItemStack display = ItemStack.builder()
                .from(this.buy.getStack())

                .add(Keys.DISPLAY_NAME, this.constructTitle(messages, language, this.buy))
                .add(Keys.ITEM_LORE, this.constructLore(messages, language, this.price))

                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.HIDE_MISCELLANEOUS, true)
                .add(Keys.HIDE_ENCHANTMENTS, true)
                .add(Keys.HIDE_CAN_PLACE, true)
                .add(Keys.HIDE_CAN_DESTROY, true)
                .add(Keys.HIDE_ATTRIBUTES, true)

                .build();

        this.setItemStack(display);
    }

    private Text constructTitle(ShopMessages messages, SfLanguage language, TranslatedShopItem translated) {
        return Text.builder()
                .append(
                        Text.of(TextColors.GOLD, messages.translatedKey(language, translated.getKey()))
                )
                .append(
                        Text.of(TextColors.GRAY, " x " + translated.getStack().getQuantity())
                ).build();
    }

    private List<Text> constructLore(ShopMessages messages, SfLanguage language, TranslatedShopItem[] price) {
        List<Text> result = new ArrayList<>();

        result.add(
                messages.cost(language)
        );

        boolean needFootnote = false;
        for (TranslatedShopItem translated : price) {
            needFootnote = needFootnote || translated.getStack().getMaxStackQuantity() == 1;
            result.add(
                    TranslatableText.builder()
                            .append(
                                    Text.of("- ").toBuilder().color(TextColors.GRAY).build()
                            )
                            .append(
                                    this.constructLoreDisplay(messages, language, translated)
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
                                    messages.additionFirst(language)
                            )
                            .build()
            );
            result.add(messages.additionSecond(language));
        }

        return result;
    }

    private Text constructLoreDisplay(ShopMessages messages, SfLanguage language, TranslatedShopItem translated) {
        if (translated.getStack().getMaxStackQuantity() == 1) {
            return Text.builder()
                    .append(
                            Text.of(TextColors.GOLD, messages.translatedKey(language, translated.getKey()))
                    )
                    .append(
                            Text.of(TextColors.RED, " *")
                    ).build();
        }
        else {
            return constructTitle(messages, language, translated);
        }
    }

    public void checkPrice(Player player) {
        for (TranslatedShopItem translated : this.price) {
            if (!this.checkPrice(player, translated.getStack())) {
                return;
            }
        }

        this.buy(player);
    }

    private boolean checkPrice(Player player, ItemStack price) {
        CarriedInventory<? extends Carrier> inventory = player.getInventory();
        Iterable<Slot> slots = inventory.slots();

        boolean needMainHand = price.getMaxStackQuantity() == 1;
        int need = price.getQuantity();
        int have = 0;

        if (needMainHand) {
            Optional<ItemStack> optionalItem = player.getItemInHand(HandTypes.MAIN_HAND);
            return optionalItem.map(itemStack -> itemStack.getType().equals(price.getType())).orElse(false);
        }

        for (Slot slot : slots) {
            Optional<ItemStack> optionalItemStack = slot.peek();
            if (optionalItemStack.isPresent()) {
                ItemStack stack = optionalItemStack.get();
                if (!stack.getType().equals(price.getType())) {
                    continue;
                }
                have += stack.getQuantity();
            }
        }

        return need <= have;
    }

    public void buy(Player player) {
        for (TranslatedShopItem translated : this.price) {
            this.buy(player, translated.getStack());
        }

        if (this.placeInMainHand) {
            player.setItemInHand(HandTypes.MAIN_HAND, this.buy.getStack().copy());
        }
        else {
            Location<World> location = player.getLocation();
            Inventory hotbar = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
            InventoryTransactionResult result = hotbar.offer(this.buy.getStack().copy());
//            InventoryTransactionResult result = player.getInventory().offer(this.buy.copy());
            if (!result.getRejectedItems().isEmpty()) {
                Inventory playerInventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory.class));
                for (ItemStackSnapshot stackSnapshot : result.getRejectedItems()) {
                    InventoryTransactionResult resultOffer = playerInventory.offer(stackSnapshot.createStack());
                    for (ItemStackSnapshot itemStackSnapshot : resultOffer.getRejectedItems()) {
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

    private void buy(Player player, ItemStack price) {
        if (price.getMaxStackQuantity() == 1) {
            return;
        }
        int needRemove = price.getQuantity();
        int left = 0;

        CarriedInventory<? extends Carrier> inventory = player.getInventory();
        Iterable<Slot> slots = inventory.slots();

        for (Slot slot : slots) {
            Optional<ItemStack> optionalItemStack = slot.peek();
            if (optionalItemStack.isPresent()) {
                ItemStack stack = optionalItemStack.get();
                if (!stack.getType().equals(price.getType())) {
                    continue;
                }
                left = stack.getQuantity() - needRemove;
                needRemove -= stack.getQuantity();

                stack.setQuantity(left < 0 ? 0 : left);
                slot.set(stack);

                if (needRemove < 1) {
                    return;
                }
            }
        }
    }
}
