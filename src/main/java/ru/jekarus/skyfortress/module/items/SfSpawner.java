package ru.jekarus.skyfortress.module.items;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.function.Supplier;

public class SfSpawner {

    private final Location location;
    private final int radius;
    private final int seconds;
    private final Supplier<ItemStack> spawn;
    private int counter = 0;

    public SfSpawner(Location location, int radius, int seconds, Supplier<ItemStack> spawn) {
        this.location = location;
        this.radius = radius;
        this.seconds = seconds;
        this.spawn = spawn;
    }

    public void tick() {
        if (location.getNearbyPlayers(radius).size() > 0) {
            counter++;
            if (counter >= seconds) {
                location.getWorld().spawn(
                        location.clone().add(0, 4, 0),
                        Item.class, it -> {
                            it.setItemStack(spawn.get());
                            it.setVelocity(new Vector());
                        }
                );
                counter = 0;
            }
        } else {
            counter = 0;
        }
    }
}
