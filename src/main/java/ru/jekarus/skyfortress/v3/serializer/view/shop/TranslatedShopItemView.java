package ru.jekarus.skyfortress.v3.serializer.view.shop;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import lombok.val;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.skyfortress.v3.gui.TranslatedShopItem;

public class TranslatedShopItemView {

    @ConfigPath("translate_key")
    private String translateKey;

    @ConfigPath("item")
    private ItemStack itemStack;

    public TranslatedShopItem create() {
        final val result = new TranslatedShopItem();

        result.setKey(translateKey);
        result.setStack(itemStack);

        return result;
    }
}
