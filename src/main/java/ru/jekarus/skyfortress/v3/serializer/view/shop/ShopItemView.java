package ru.jekarus.skyfortress.v3.serializer.view.shop;

import jekarus.hocon.config.serializer.annotation.Generics;
import lombok.val;
import ru.jekarus.jgui.gui.content.GuiContent;
import ru.jekarus.skyfortress.v3.gui.ShopItem;
import ru.jekarus.skyfortress.v3.gui.ShopSlot;
import ru.jekarus.skyfortress.v3.gui.TranslatedShopItem;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.lang.ShopMessages;

import java.util.List;

public class ShopItemView {

    private int index;

    private TranslatedShopItemView buy;

    @Generics(TranslatedShopItemView.class)
    private List<TranslatedShopItemView> price;

    public void setTo(GuiContent content, ShopMessages messages, SfLanguage language) {

        final val price = new TranslatedShopItem[this.price.size()];
        int index = 0;
        for (TranslatedShopItemView view : this.price) {
            price[index++] = view.create();
        }

        final val shopItem = new ShopItem(
                this.buy.create(),
                price
        );
        shopItem.init(messages, language);
        content.setSlot(this.index, new ShopSlot(index, shopItem));
    }

}
