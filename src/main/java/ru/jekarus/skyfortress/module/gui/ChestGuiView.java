package ru.jekarus.skyfortress.module.gui;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

public class ChestGuiView {

    private final SfGuiElement[] elements;
    protected Inventory inv;

    public ChestGuiView(int height) {
        int count = height * 9;
        elements = new SfGuiElement[count];
    }

    public @Nullable SfGuiElement get(int rawSlot) {
        return elements[rawSlot];
    }

    public @Nullable SfGuiElement get(int x, int y) {
        int idx = x + y * 9;
        return elements[idx];
    }

    public void set(int x, int y, final SfGuiElement element) {
        int idx = x + y * 9;
        remove(x, y);
        elements[idx] = element;
        element.view = this;
        element.idx = idx;
        element.update();
    }

    public void remove(int x, int y) {
        int idx = x + y * 9;
        final var element = elements[idx];
        if(element == null) return;
        if(inv != null) inv.clear(idx);
        elements[idx] = null;
        element.view = null;
        element.idx = -1;
    }

    public void updateAll() {
        if(inv == null) return;
        assert elements.length == inv.getSize();
        for (int i = 0; i < elements.length; i++) {
            final var element = elements[i];
            if (element == null) {
                inv.setItem(i, null);
            } else {
                element.view = this;
                element.idx = i;
                element.update();
            }
        }
    }
}
