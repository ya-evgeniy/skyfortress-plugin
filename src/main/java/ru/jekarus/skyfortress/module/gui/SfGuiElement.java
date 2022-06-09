package ru.jekarus.skyfortress.module.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SfGuiElement {
    private @Nullable ItemStack item;
    protected ChestGuiView view;
    protected int idx = -1;
    public @Nullable Consumer<InventoryClickEvent> clickCb;
    public @Nullable Consumer<InventoryDragEvent> dragCb;
    public @Nullable Consumer<ChestGuiBase.MoveEvent> moveCb;
    public @Nullable Consumer<ChestGuiBase.CollectEvent> collectCb;

    public SfGuiElement() {
        item = new ItemStack(Material.AIR, 1);
    }
    public SfGuiElement(@Nullable ItemStack is) {
        item = is;
    }
    public SfGuiElement(Material material, final String name, final List<String> lore) {
        item = new ItemStack(material, 1);
        item.editMeta(im -> {
            im.setDisplayName(name);
            im.setLore(lore);
        });
    }
    public SfGuiElement(Material material, final String name, final String... lore) {
        this(material, name, Arrays.asList(lore));
    }
    public SfGuiElement(Material material, Consumer<ItemStack> op) {
        item = new ItemStack(material, 1);
        op.accept(item);
    }

    public SfGuiElement setOnClick(@Nullable Consumer<InventoryClickEvent> callback) {
        this.clickCb = callback;
        return this;
    }

    public SfGuiElement setOnDrag(@Nullable Consumer<InventoryDragEvent> callback) {
        this.dragCb = callback;
        return this;
    }

    public SfGuiElement setOnMove(@Nullable Consumer<ChestGuiBase.MoveEvent> callback) {
        this.moveCb = callback;
        return this;
    }

    /**
     * user double-clicked on item similar to current
     */
    public SfGuiElement setOnCollect(@Nullable Consumer<ChestGuiBase.CollectEvent> callback) {
        this.collectCb = callback;
        return this;
    }

    public void updateName(String name) {
        if(item != null) item.editMeta(im -> {
            im.setDisplayName(name);
        });
    }

    public void updateLore(String... lore) {
        updateLore(Arrays.asList(lore));
    }

    public void updateLore(List<String> lore) {
        if(item != null) item.editMeta(im -> {
            im.setLore(lore);
        });
    }

    public void updateText(String name, List<String> lore) {
        if(item != null) item.editMeta(im -> {
            im.setDisplayName(name);
            im.setLore(lore);
        });
    }
    public int x() { return idx % 9; }
    public int y() { return idx / 9; }

    public SfGuiElement with(Consumer<ItemStack> op) {
        if(item != null) op.accept(item);
        update();
        return this;
    }

    protected void update() {
        if(view == null) return;
        if(item == null) return;
        if(item.getType() == Material.AIR) {
            if(view.inv != null) view.inv.setItem(idx, null);
            return;
        }
        if(view.inv != null) view.inv.setItem(idx, item);
    }

    public void setItem(@Nullable ItemStack item) {
        if(this.item == null) {
            view.inv.setItem(idx, item);
            return;
        }
        this.item = item;
        update();
    }

    public @Nullable ItemStack readItem() {
        if (view.inv == null) return item;
        final var invItem = view.inv.getItem(idx);
        if (invItem == null) return null;
        return invItem.clone();
    }

}
