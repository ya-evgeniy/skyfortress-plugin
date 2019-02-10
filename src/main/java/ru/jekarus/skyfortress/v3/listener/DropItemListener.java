package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

public class DropItemListener {

    private final SkyFortressPlugin plugin;

    public DropItemListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void register()
    {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onDrop(DropItemEvent.Destruct event)
    {
        for (Entity entity : event.getEntities())
        {
            if (!entity.getType().equals(EntityTypes.ITEM))
            {
                continue;
            }
            Item item = (Item) entity;
//            if (item.getItemType().equals(ItemTypes.LAPIS_BLOCK))
//            {
//                item.offer(
//                        Keys.REPRESENTED_ITEM,
//                        ItemStack.builder()
//                                .itemType(ItemTypes.DYE)
//                                .add(Keys.DYE_COLOR, DyeColors.BLUE)
//                                .quantity(1)
//                                .build()
//                                .createSnapshot()
//                );
//            }
            if (item.getItemType().equals(ItemTypes.GOLD_ORE))
            {
                item.offer(Keys.REPRESENTED_ITEM, ItemStack.of(ItemTypes.GOLD_INGOT, 1).createSnapshot());
            }
            else if (item.getItemType().equals(ItemTypes.IRON_ORE))
            {
                item.offer(Keys.REPRESENTED_ITEM, ItemStack.of(ItemTypes.IRON_INGOT, 1).createSnapshot());
            }
//            else if (item.getItemType().equals(ItemTypes.GOLD_BLOCK))
//            {
//                item.offer(Keys.REPRESENTED_ITEM, ItemStack.of(ItemTypes.GOLD_INGOT, 1).createSnapshot());
//            }
        }
    }

}
