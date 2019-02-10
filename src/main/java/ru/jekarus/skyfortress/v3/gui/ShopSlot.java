package ru.jekarus.skyfortress.v3.gui;

import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import ru.jekarus.jgui.argument.GuiArguments;
import ru.jekarus.jgui.event.GuiListener;
import ru.jekarus.jgui.gui.slot.GuiSlot;
import ru.jekarus.jgui.gui.slot.item.GuiItem;

public class ShopSlot extends GuiSlot {

    public ShopSlot(ShopItem item)
    {
        super(item);
    }

    public ShopSlot(int index, GuiItem item)
    {
        super(index, item);
    }

    @GuiListener
    public void onEvent(ClickInventoryEvent.Primary event, GuiArguments arguments)
    {
        ShopItem item = (ShopItem) this.item;
        item.checkPrice(arguments.player);
    }

}
