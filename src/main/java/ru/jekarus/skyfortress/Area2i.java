package ru.jekarus.skyfortress;

public record Area2i(Vec3i min, Vec3i max) {

    public Area2i {
        assert min.x() <= max.x();
        assert min.y() <= max.y();
        assert min.z() <= max.z();
    }

    public Area2i(Vec3i v) {
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

}
