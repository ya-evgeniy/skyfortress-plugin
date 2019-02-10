package ru.jekarus.jgui.gui.content;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import ru.jekarus.jgui.gui.Gui;
import ru.jekarus.jgui.gui.slot.GuiSlot;
import ru.jekarus.jgui.gui.slot.SpongeGuiSlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class GuiContent {

    private SpongeGuiSlot[] content;

    public GuiContent(int slotsCount)
    {
        this.content = new SpongeGuiSlot[slotsCount];
        for (int index = 0; index < content.length; index++)
        {
            this.content[index] = new SpongeGuiSlot(index);
        }
    }

    public void setGui(Gui gui)
    {
        if (gui == null)
        {
            for (SpongeGuiSlot slot: this.content)
            {
                slot.setSpongeSlot(null);
            }
            return;
        }
        Inventory inventory = gui.getInventory();
        Iterable<Slot> slots = inventory.slots();

        for (Slot slot : slots)
        {
            Optional<SlotIndex> optionalSpongeSlot = slot.getInventoryProperty(SlotIndex.class);
            if (optionalSpongeSlot.isPresent())
            {
                SlotIndex slotIndex = optionalSpongeSlot.get();
                int index = slotIndex.getValue() == null ? -1 : slotIndex.getValue();

                if (this.checkIndex(index))
                {
                    SpongeGuiSlot spongeGuiSlot = this.content[index];
                    spongeGuiSlot.setSpongeSlot(slot);
                }
                else
                {
                    System.out.println("CHECK INDEX FAILED");
                }
            }
            else
            {
                System.out.println("invalid SlotIndex.class");
            }
        }
    }

    public int getLength()
    {
        return this.content.length;
    }

    public void setSpongeSlot(int index, SpongeGuiSlot slot)
    {
        if (slot == null)
        {
            throw new NullPointerException("Slot cannot be null");
        }
        if (this.checkIndex(index))
        {
            this.content[index] = slot;
        }
    }

    public Optional<SpongeGuiSlot> getSpongeSlot(int index)
    {
        if (this.checkIndex(index))
        {
            return Optional.of(this.content[index]);
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setSlot(int index, GuiSlot slot)
    {
        if (this.checkIndex(index))
        {
            this.content[index].setSlot(slot);
        }
        else
        {
            System.out.println("setSlot invalid index");
        }
    }

    public Optional<? extends GuiSlot> getSlot(int index)
    {
        if (this.checkIndex(index))
        {
            return this.content[index].getSlot();
        }
        else
        {
            return Optional.empty();
        }
    }

    private boolean checkIndex(int index)
    {
        return index > -1 && index < this.content.length;
    }

    public Collection<? extends GuiSlot> asCollection()
    {
        Collection<GuiSlot> slots = new ArrayList<>();
        for (SpongeGuiSlot spongeGuiSlot : this.content)
        {
            Optional<? extends GuiSlot> slot = spongeGuiSlot.getSlot();
            if (slot.isPresent())
            {
                GuiSlot guiSlot = slot.get();
                slots.add(guiSlot);
            }
        }
        return slots;
    }
}
