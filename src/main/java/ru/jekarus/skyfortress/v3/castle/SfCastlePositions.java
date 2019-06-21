package ru.jekarus.skyfortress.v3.castle;

import jekarus.hocon.config.serializer.annotation.Generics;
import lombok.Getter;
import lombok.Setter;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

import java.util.List;

public class SfCastlePositions {

    @Generics(LocationAndRotation.class)
    @Getter @Setter private List<LocationAndRotation> respawn;

    @Getter @Setter private LocationAndRotation capture;

    @Generics(LocationAndRotation.class)
    @Getter @Setter private List<LocationAndRotation> shops;

    public SfCastlePositions() { }

    public SfCastlePositions(List<LocationAndRotation> respawn, LocationAndRotation capture, List<LocationAndRotation> shops) {
        this.respawn = respawn;
        this.capture = capture;
        this.shops = shops;
    }

}
