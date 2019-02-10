package ru.jekarus.jgui.gui;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;
import ru.jekarus.jgui.gui.content.GuiContent;

import java.util.Optional;

public abstract class Gui {

    protected String uniqueId;
    protected Text title;

    protected GuiContent content;

    public Gui(final String uniqueId)
    {
        this(uniqueId, Text.of(uniqueId));
    }

    public Gui(final String uniqueId, Text title)
    {
        this.uniqueId = uniqueId;
        this.title = title;
    }

    public String getUniqueId()
    {
        return this.uniqueId;
    }

    public Text getTitle()
    {
        return this.title;
    }

    public void setContent(GuiContent content)
    {
        if (this.content != null)
        {
            this.content.setGui(null);
        }
        this.content = content;
        if (this.content != null)
        {
            this.content.setGui(this);
        }
    }

    public Optional<GuiContent> getContent()
    {
        return Optional.ofNullable(this.content);
    }

    public abstract Inventory getInventory();

}
