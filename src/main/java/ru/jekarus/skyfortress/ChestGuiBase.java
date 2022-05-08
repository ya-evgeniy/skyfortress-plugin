package ru.jekarus.skyfortress;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ChestGuiBase implements InventoryHolder, Listener {

    public static class Element {
        public final ItemStack item;
        private ChestGuiBase holder;
        private int idx = -1;
        private Consumer<InventoryClickEvent> callback;

        public Element(Material material) {
            item = new ItemStack(material, 1);
        }

        public void setOnClick(Consumer<InventoryClickEvent> callback) {
            this.callback = callback;
        }

        public void updateName(String name) {
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        public void updateLore(String... lore) { updateLore(Arrays.asList(lore)); }
        public void updateLore(List<String> lore) {
            final ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        public void updateText(String name, List<String> lore) {
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        public void remove() {
            if(holder == null) return;
            holder.remove(idx % 9, idx / 9);
        }
    }

    private final Inventory inv;
    private final Element[] elements;

    public ChestGuiBase(int height, String name) {
        int count = height * 9;
        inv = Bukkit.createInventory(this, count, name);
        elements = new Element[count];
    }

    protected Element set(int x, int y, final Material material, final String name, final String... lore) {
        final var element = new Element(material);
        element.updateName(name);
        element.updateLore(lore);
        set(x, y, element);
        return element;
    }

    protected void set(int x, int y, final Element element) {
        int idx = x + y * 9;
        element.remove();
        inv.setItem(idx, element.item);
        elements[idx] = element;
        element.holder = this;
        element.idx = idx;
    }

    private void remove(int x, int y) {
        int idx = x + y * 9;
        inv.clear(idx);
        final var element = elements[idx];
        elements[idx] = null;
        element.holder = null;
        element.idx = -1;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final int rawSlot = e.getRawSlot();

        // verify current item is not null
        if (rawSlot < 0) return;
        if (rawSlot >= inv.getSize()) return;
        final var element = elements[rawSlot];
        if(element == null) return;
        if(element.callback == null) return;
        element.callback.accept(e);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
