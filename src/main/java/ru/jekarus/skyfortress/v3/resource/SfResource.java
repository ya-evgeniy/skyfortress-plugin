package ru.jekarus.skyfortress.v3.resource;

import com.flowpowered.math.vector.Vector3d;
import lombok.Getter;
import lombok.Setter;
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

    @Getter @Setter private String uniqueId;
    @Getter @Setter private ItemStack item;

    @Getter @Setter private int ticks;
    private int leftTicks = 0;

    @Getter @Setter private Collection<Location<World>> positions;

    public SfResource() {

    }

    public SfResource(String uniqueId, ItemStack item, int ticks, Collection<Location<World>> positions) {
        this.uniqueId = uniqueId;
        this.item = item;
        this.ticks = ticks;
        this.positions = positions;
    }

    public void tick() {
        this.leftTicks --;
        if (this.leftTicks < 1)
        {
            this.summonItems();
            this.leftTicks = this.ticks;
        }
    }

    private void summonItems() {
        for (Location<World> position : this.positions) {
            Item item = (Item) position.createEntity(EntityTypes.ITEM);
            item.offer(Keys.REPRESENTED_ITEM, this.item.createSnapshot());
            item.offer(Keys.VELOCITY, Vector3d.from(0, -1, 0));
            position.spawnEntity(item);
        }
    }

}
