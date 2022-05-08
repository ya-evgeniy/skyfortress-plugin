package ru.jekarus.skyfortress;

public record Area3i(Vec3i min, Vec3i max) {

    public Area3i {
        assert min.x() <= max.x();
        assert min.y() <= max.y();
        assert min.z() <= max.z();
    }

    public Area3i(Vec3i v) {
        this(v, v);
    }

    public boolean contains(Vec3i v) {
        if (v.x() < min.x() || v.x() > max.x()) return false;
        if (v.y() < min.y() || v.y() > max.y()) return false;
        if (v.z() < min.z() || v.z() > max.z()) return false;
        return true;
    }

    public Vec3i middle() {
        return new Vec3i(
                (min.x() + max.x()) / 2,
                (min.y() + max.y()) / 2,
                (min.z() + max.z()) / 2
        );
    }

    public static Area3i of(Vec3i point1, Vec3i point2) {
        return new Area3i(
                new Vec3i(
                        Math.min(point1.x(), point2.x()),
                        Math.min(point1.y(), point2.y()),
                        Math.min(point1.z(), point2.z())
                ),
                new Vec3i(
                        Math.max(point1.x(), point2.x()),
                        Math.max(point1.y(), point2.y()),
                        Math.max(point1.z(), point2.z())
                )
        );
    }

}
