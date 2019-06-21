package ru.jekarus.skyfortress.v3.gui;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.jgui.gui.slot.item.GuiItem;

public class TranslatedShopItem extends GuiItem {

    @ConfigPath("translate_key")
    @Getter @Setter private String key;

    @ConfigPath("item")
    @Getter @Setter private ItemStack stack;

}
