package ru.jekarus.jgui.gui;

import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import ru.jekarus.jgui.event.GuiEventHandler;
import ru.jekarus.jgui.gui.content.GuiContent;
import ru.jekarus.jgui.gui.slot.GuiSlot;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class ChestGui extends RectangleGui {

    public static final int CHEST_MIN_ROWS = 1;
    public static final int CHEST_MAX_ROWS = 6;
    public static final int CHEST_COLS = 9;

    protected Inventory inventory;

    protected GuiEventHandler handler;

    public ChestGui(String uniqueId)
    {
        this(uniqueId, Text.of(uniqueId));
    }

    public ChestGui(String uniqueId, Text title)
    {
        this(uniqueId, title, CHEST_MAX_ROWS);
    }

    public ChestGui(String uniqueId, int rows)
    {
        this(uniqueId, Text.of(uniqueId), rows);
    }

    public ChestGui(String uniqueId, Text title, int rows)
    {
        super(uniqueId, title, CHEST_COLS, getCorrectRows(rows));

        this.handler = new GuiEventHandler(this);

        this.inventory = Inventory.builder()

                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(title))
                .property(InventoryDimension.PROPERTY_NAME, InventoryDimension.of(CHEST_COLS, rows))

                .listener(ClickInventoryEvent.Primary.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Middle.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Secondary.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Shift.Primary.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Shift.Secondary.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Double.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Drop.Single.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.Drop.Full.class, this.handler::onEvent)
                .listener(ClickInventoryEvent.NumberPress.class, this.handler::onEvent)

                .build(SkyFortressPlugin.getInstance());

        this.content = new GuiContent(this.rows * this.cols);
    }

    public void setSlot(int col, int row, GuiSlot slot)
    {
        this.content.setSlot(row * CHEST_COLS + col, slot);
    }

    @Override
    public Inventory getInventory()
    {
        return this.inventory;
    }

    @Override
    public Text getTitle()
    {
        return this.title;
    }

    public static int getCorrectRows(int rows)
    {
        return rows < CHEST_MIN_ROWS ? CHEST_MIN_ROWS : rows > CHEST_MAX_ROWS ? CHEST_MAX_ROWS : rows;
    }

}
