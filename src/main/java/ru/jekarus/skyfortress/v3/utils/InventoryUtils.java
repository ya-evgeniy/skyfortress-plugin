package ru.jekarus.skyfortress.v3.utils;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.CarriedInventory;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class InventoryUtils {

    public static void drop(Location<World> location, List<ItemStack> items) {
        for (ItemStack itemStack : items) {
            Item item = (Item) location.createEntity(EntityTypes.ITEM);
            item.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
            item.offer(Keys.VELOCITY, Vector3d.from(0));
            location.spawnEntity(item);
        }
    }

    public static List<ItemStack> put(Player player, List<ItemStack> items) {
        final ArrayList<ItemStack> result = new ArrayList<>();
        for (ItemStack item : items) {
            int leftCount = put(player, item);
            if (leftCount > 0) {
                final ItemStack copy = item.copy();
                copy.setQuantity(leftCount);
                result.add(copy);
            }
        }
        return result;
    }

    public static int put(Player player, ItemStack item) {
        final CarriedInventory<? extends Carrier> inventory = player.getInventory();
        final ItemStack copy = item.copy();

        Inventory emptySlot = null;

        final Inventory hotbar = inventory.query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
        final Iterator<Inventory> hotBarIterator = hotbar.iterator();

        while (copy.getQuantity() > 0 && hotBarIterator.hasNext()) {
            final Inventory slot = hotBarIterator.next();
            if (!putToSlot(slot, copy) && emptySlot == null) {
                emptySlot = slot;
            }
        }

        if (copy.getQuantity() < 1) return 0;

        final Inventory rows = inventory.query(QueryOperationTypes.INVENTORY_TYPE.of(GridInventory.class)).first();
        final Iterator<Inventory> rowsIterator = rows.iterator();

        while (copy.getQuantity() > 0 && rowsIterator.hasNext()) {
            final Inventory row = rowsIterator.next();
            final Iterator<Inventory> slotsIterator = row.iterator();
            while (copy.getQuantity() > 0 && slotsIterator.hasNext()) {
                final Inventory slot = slotsIterator.next();
                if (!putToSlot(slot, copy) && emptySlot == null) {
                    emptySlot = slot;
                }
            }
        }

        if (copy.getQuantity() < 1) return 0;
        if (emptySlot == null) return copy.getQuantity();

        emptySlot.set(copy);
        return 0;
    }

    public static boolean putToSlot(Inventory slot, ItemStack item) {
        final Optional<ItemStack> optionalItemStack = slot.peek();
        if (optionalItemStack.isPresent()) {
            final ItemStack slotItem = optionalItemStack.get();
            if (!compareItems(item, slotItem)) return true;

            final int slotQuantity = slotItem.getQuantity();
            final int maxSlotQuantity = slotItem.getMaxStackQuantity();

            final int freeCount = maxSlotQuantity - slotQuantity;

            if (freeCount < 1) return true;

            if (freeCount >= item.getQuantity()) {
                slotItem.setQuantity(slotQuantity + item.getQuantity());
                item.setQuantity(0);
                slot.set(slotItem);
            }
            else {
                item.setQuantity(item.getQuantity() - freeCount);;
                slotItem.setQuantity(maxSlotQuantity);
                slot.set(slotItem);
            }
            return true;
        };
        return false;
    }

    public static boolean compareItems(ItemStack f, ItemStack s) {
        if (!f.getType().equals(s.getType())) return false;
        if (!compareItemsName(f, s)) return false;

        return true;
    }

    public static boolean compareItemsName(ItemStack f, ItemStack s) {
        final Optional<Text> optFName = f.get(Keys.DISPLAY_NAME);
        final Optional<Text> optSName = s.get(Keys.DISPLAY_NAME);

        if (!optFName.isPresent() && !optSName.isPresent()) return true;
        if (optFName.isPresent() && !optSName.isPresent()) return false;
        if (!optFName.isPresent() && optSName.isPresent()) return false;

        final Text fName = optFName.get();
        final Text sName = optSName.get();
        return fName.equals(sName);
    }

}
