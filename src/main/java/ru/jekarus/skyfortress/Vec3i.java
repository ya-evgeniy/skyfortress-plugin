package ru.jekarus.skyfortress;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.Vector;

import java.util.Collection;

public record Vec3i(int x, int y, int z) {
    public Vec3i(Location l) {
        this(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    public Vec3i(int i) {
        this(i, i, i);
    }

    public Location toLocation(World world) {
        return new Location(world, x + 0.5, y + 0.2, z + 0.5);
    }

    public Location toLocation(World world, BlockFace face) {
        return toLocation(world).setDirection(face.getDirection());
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public void teleport(HumanEntity human, BlockFace face) {
        final var location = toLocation(human.getWorld());
        location.setDirection(face.getDirection());
        human.teleport(location);
    }

    public void teleport(HumanEntity human, Vector dir) {
        final var location = toLocation(human.getWorld());
        location.setDirection(dir);
        human.teleport(location);
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

    public Vec3i mul(int value) {
        return new Vec3i(
                this.x * value,
                this.y * value,
                this.z * value
        );
    }

    public boolean eq(Vec3i that) {
        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

}
