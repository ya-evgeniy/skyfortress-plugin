package ru.jekarus.jgui.gui.slot;

import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.jgui.gui.slot.item.GuiItem;

import java.util.Optional;

public class GuiSlot {

    protected SpongeGuiSlot owner = null;

    protected int index;
    protected GuiItem item = null;

    public GuiSlot(GuiItem item)
    {
        this(-1, item);
    }

    public GuiSlot(int index, GuiItem item)
    {
        this.index = index;
        this.item = item;
    }

    public int getIndex()
    {
        if (this.index != -1)
        {
            return this.index;
        }
        if (this.owner != null)
        {
            return this.owner.getIndex();
        }
        return -1;
    }

    public void updateOwner(SpongeGuiSlot owner)
    {
        this.owner = owner;
    }

    public Optional<GuiItem> getItem()
    {
        return Optional.ofNullable(this.item);
    }

    public void setItem(GuiItem item)
    {
        if (this.item != null)
        {
            this.item.updateOwner(this);
        }
        this.item = item;
    }

    public void render()
    {
        if (this.owner != null)
        {
            this.owner.render();
        }
    }

    public static GuiSlot of(GuiItem item)
    {
        return new GuiSlot(item);
    }

    public static GuiSlot of(ItemStack itemStack)
    {
        return new GuiSlot(GuiItem.of(itemStack));
    }

}
