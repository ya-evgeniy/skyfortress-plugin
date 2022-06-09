package ru.jekarus.skyfortress.module.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ChestGuiBase implements InventoryHolder {

    private final Inventory inv;
    private ChestGuiView view;

    public ChestGuiBase(int height, String name) {
        int count = height * 9;
        inv = Bukkit.createInventory(this, count, name);
        view = new ChestGuiView(height);
        view.inv = inv;
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public ChestGuiView getView() {
        return view;
    }

    public void setView(ChestGuiView view) {
        Objects.requireNonNull(view);
        this.view.inv = null;
        this.view = view;
        view.inv = inv;
        view.updateAll();
    }

    public void open(InventoryOpenEvent e) {

    }

    public void click(InventoryClickEvent e) {
        final int rawSlot = e.getRawSlot();

        // verify current item is not null
        if (rawSlot < 0) return;
        if (rawSlot >= inv.getSize()) return;
        e.setCancelled(true);

        final var element = view.get(rawSlot);
        if(element == null) return;
        if(element.clickCb == null) return;
        element.clickCb.accept(e);
    }

    public void drag(InventoryDragEvent e) {
        final var touched = new ArrayList<Integer>();
        for (Integer rawSlot : e.getRawSlots()) {
            if (rawSlot < 0) continue;
            if (rawSlot >= inv.getSize()) continue;
            touched.add(rawSlot);
            break;
        }
        if(touched.isEmpty()) return;
        e.setCancelled(true);

        for (Integer rawSlot : touched) {
            final var element = view.get(rawSlot);
            if(element == null) continue;
            if(element.dragCb == null) continue;
            element.dragCb.accept(e);
        }
    }

    public static class MoveEvent {

        public final InventoryClickEvent event;
        public final boolean moveFrom;

        public MoveEvent(InventoryClickEvent event, boolean moveFrom) {
            this.event = event;
            this.moveFrom = moveFrom;
        }
    }

    public void move(InventoryClickEvent e) {
        e.setCancelled(true);
        var rawSlot = e.getRawSlot();

        // verify current item is not null
        if (rawSlot < 0) return;
        boolean moveFrom = true;
        if (rawSlot >= inv.getSize()) {
            rawSlot = inv.firstEmpty();
            if (rawSlot < 0) return;
            moveFrom = false;
        }

        final var element = view.get(rawSlot);
        if(element == null) return;
        if(element.moveCb == null) return;
        element.moveCb.accept(new MoveEvent(e, moveFrom));
    }

    public static class ItemWithIndex {
        final int idx;
        final ItemStack item;

        public ItemWithIndex(int idx, ItemStack item) {
            this.idx = idx;
            this.item = item;
        }
    }
    public static class CollectEvent {

        public final InventoryClickEvent event;
        public final ArrayList<ItemWithIndex> touched;

        public CollectEvent(InventoryClickEvent event, ArrayList<ItemWithIndex> touched) {
            this.event = event;
            this.touched = touched;
        }
    }

    public void collect(InventoryClickEvent e) {
        final var item = e.getCursor();
        if(item == null) return;
        final var touched = new ArrayList<ItemWithIndex>();
        final var size = inv.getSize();
        for (int rawSlot = 0; rawSlot < size; rawSlot++) {
            final var is = inv.getItem(rawSlot);
            if(is == null) continue;
            if(is.isSimilar(item)) {
                touched.add(new ItemWithIndex(rawSlot, is));
            }
        }
        if(touched.isEmpty()) return;
        e.setCancelled(true);

        for (ItemWithIndex ii : touched) {
            final var element = view.get(ii.idx);
            if(element == null) continue;
            if(element.collectCb == null) continue;
            element.collectCb.accept(new CollectEvent(e, touched));
        }
    }

    public void close(InventoryCloseEvent e) {

    }

}
