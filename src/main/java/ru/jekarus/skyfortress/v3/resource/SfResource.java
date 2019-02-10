package ru.jekarus.skyfortress.v3.resource;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class SfResource {

    private String uniqueId;
    private ItemStack item;

    private int ticks;
    private int leftTicks = 0;

    private Collection<Location<World>> positions;
    private Collection<UUID> prevEntities = new ArrayList<>();

    public SfResource()
    {

    }

    public SfResource(String uniqueId, ItemStack item, int ticks, Collection<Location<World>> positions)
    {
        this.uniqueId = uniqueId;
        this.item = item;
        this.ticks = ticks;
        this.positions = positions;
    }

    public String getUniqueId()
    {
        return this.uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public int getTicks()
    {
        return this.ticks;
    }

    public void setTicks(int ticks)
    {
        this.ticks = ticks;
    }

    public Collection<Location<World>> getPositions()
    {
        return this.positions;
    }

    public void setPositions(Collection<Location<World>> positions)
    {
        this.positions = positions;
    }

    public void tick()
    {
        this.leftTicks --;
        if (this.leftTicks < 1)
        {
            this.summonItems();
            this.leftTicks = this.ticks;
        }
    }

    private void summonItems()
    {
        this.removePrevItems();

        for (Location<World> position : this.positions)
        {
            Item item = (Item) position.createEntity(EntityTypes.ITEM);
            item.offer(Keys.REPRESENTED_ITEM, this.item.createSnapshot());
            item.offer(Keys.VELOCITY, Vector3d.from(0));
            position.spawnEntity(item);
            this.prevEntities.add(item.getUniqueId());
        }
    }

    @Deprecated
    private void removePrevItems()
    {
        Iterator<UUID> iterator = this.prevEntities.iterator();
        while (iterator.hasNext())
        {
            UUID uniqueId = iterator.next();
            SkyFortressPlugin.getInstance().getWorld().getEntity(uniqueId).ifPresent(entity -> {
                if (entity.getType().equals(EntityTypes.ITEM))
                {
                    entity.remove();
                }
            });
            iterator.remove();
        }
    }

}
