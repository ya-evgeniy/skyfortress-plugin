package ru.jekarus.skyfortress;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;

import java.util.Collection;

public record Vec3i(int x, int y, int z) {
    public Vec3i(Location l) {
        this(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public Location toLocation(World world) {
        return new Location(world, x + 0.5, y + 0.2, z + 0.5);
    }

    public void teleport(HumanEntity player, BlockFace face) {
        final var location = toLocation(player.getWorld());
        location.setDirection(face.getDirection());
        player.teleport(location);
    }

    public Vec3i add(int x, int y, int z) {
        return new Vec3i(
                this.x + x,
                this.y + y,
                this.z + z
        );
    }

    public Vec3i add(Vec3i that) {
        return new Vec3i(
                this.x + that.x,
                this.y + that.y,
                this.z + that.z
        );
    }

    public Vec3i sub(Vec3i that) {
        return new Vec3i(
                this.x - that.x,
                this.y - that.y,
                this.z - that.z
        );
    }

}
