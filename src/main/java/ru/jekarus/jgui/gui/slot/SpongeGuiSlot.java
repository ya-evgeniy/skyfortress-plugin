package ru.jekarus.jgui.gui.slot;

import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import ru.jekarus.jgui.argument.GuiArguments;
import ru.jekarus.jgui.event.slot.GuiSlotEventHandler;

import java.util.Optional;

public class SpongeGuiSlot {

    private final int index;
    private Slot spongeSlot;

    private GuiSlot slot = null;
    private GuiSlotEventHandler eventHandler = new GuiSlotEventHandler();

    public SpongeGuiSlot(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return this.index;
    }

    public void setSpongeSlot(Slot slot)
    {
        this.spongeSlot = slot;
        this.render();
    }

    public Slot getSpongeSlot()
    {
        return this.spongeSlot;
    }

    public void setSlot(GuiSlot slot)
    {
        if (this.slot != null)
        {
            this.slot.updateOwner(this);
        }
        this.slot = slot;
        if (this.slot == null)
        {
            this.eventHandler.clear();
        }
        else
        {
            this.slot.updateOwner(this);
            this.eventHandler.updateClass(this.slot.getClass());
        }
        this.render();
    }

    public Optional<? extends GuiSlot> getSlot()
    {
        return Optional.ofNullable(this.slot);
    }

    public void render()
    {
        if (this.spongeSlot == null)
        {
            return;
        }

        if (this.slot == null)
        {
            this.spongeSlot.set(ItemStack.empty());
            return;
        }

        this.slot.getItem().ifPresent(item -> {
            Optional<ItemStack> itemStack = item.getItemStack();
            if (itemStack.isPresent())
            {
                this.spongeSlot.set(itemStack.get());
            }
            else
            {
                this.spongeSlot.set(ItemStack.empty());
            }
        });
    }

    public <E extends ClickInventoryEvent> void handleEvent(GuiArguments arguments)
    {
        if (this.slot == null)
        {
            return;
        }
        this.eventHandler.handleEvent(this.slot, arguments);
    }
}
