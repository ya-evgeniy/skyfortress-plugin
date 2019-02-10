package ru.jekarus.jgui.gui;

import org.spongepowered.api.text.Text;

public abstract class RectangleGui extends Gui {

    protected int cols;
    protected int rows;

    public RectangleGui(String uniqueId, int cols, int rows)
    {
        this(uniqueId, Text.of(uniqueId), cols, rows);
    }

    public RectangleGui(String uniqueId, Text title, int cols, int rows)
    {
        super(uniqueId, title);

        this.cols = cols;
        this.rows = rows;
    }

    public int getCols()
    {
        return this.cols;
    }

    public int getRows()
    {
        return this.rows;
    }

}
