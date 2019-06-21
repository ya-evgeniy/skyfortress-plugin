package ru.jekarus.skyfortress.v3.serializer.view.shop;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import lombok.val;
import ru.jekarus.skyfortress.v3.gui.ShopGui;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.lang.ShopMessages;

import java.util.List;

@ConfigPath("shop")
public class ShopView {

    private int rows;

    @Generics(ShopItemView.class)
    private List<ShopItemView> slots;

    public ShopGui createShop(ShopMessages messages, SfLanguage language) {
        final val shopGui = new ShopGui(
                language.locale.toString() + "_shop",
                messages.shopTitle(language),
                rows
        );

        final val optionalContent = shopGui.getContent();
        if (optionalContent.isPresent()) {
            final val content = optionalContent.get();

            for (ShopItemView slot : this.slots) {
                slot.setTo(content, messages, language);
            }
        }
        return shopGui;
    }

}
