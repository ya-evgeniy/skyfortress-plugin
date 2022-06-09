package ru.jekarus.skyfortress;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.Vector;

public record Vec2i(int x, int y) {

    public Vec2i add(int x, int y) {
        return new Vec2i(
                this.x + x,
                this.y + y
        );
    }

    public Vec2i add(Vec2i that) {
        return new Vec2i(
                this.x + that.x,
                this.y + that.y
        );
    }

    public Vec2i sub(Vec2i that) {
        return new Vec2i(
                this.x - that.x,
                this.y - that.y
        );
    }

    public Vec2i mul(int value) {
        return new Vec2i(
                this.x * value,
                this.y * value
        );
    }

    public boolean eq(Vec2i that) {
        return this.x == that.x && this.y == that.y;
    }

}
