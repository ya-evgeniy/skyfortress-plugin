package ru.jekarus.jgui.gui.slot.item;

import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.jgui.gui.slot.GuiSlot;

import java.util.Optional;

public class GuiItem {

    private GuiSlot owner = null;
    private ItemStack itemStack = null;

    public GuiItem()
    {

    }

    public GuiItem(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public void updateOwner(GuiSlot owner)
    {
        this.owner = owner;
        this.render();
    }

    public Optional<ItemStack> getItemStack()
    {
        return Optional.ofNullable(this.itemStack);
    }

    public void setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
        this.render();
    }

    public void render()
    {
        if (this.owner != null)
        {
            this.owner.render();
        }
    }

    public static GuiItem of(ItemStack itemStack)
    {
        return new GuiItem(itemStack);
    }
}
