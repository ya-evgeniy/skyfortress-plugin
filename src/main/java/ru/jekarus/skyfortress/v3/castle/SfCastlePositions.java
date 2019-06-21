package ru.jekarus.skyfortress.v3.castle;

import jekarus.hocon.config.serializer.annotation.Generics;
import lombok.Getter;
import lombok.Setter;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.List;

public class SfCastlePositions {

    @Generics(SfLocation.class)
    @Getter @Setter private List<SfLocation> respawn;

    @Getter @Setter private SfLocation capture;

    @Generics(SfLocation.class)
    @Getter @Setter private List<SfLocation> shops;

    public SfCastlePositions() { }

    public SfCastlePositions(List<SfLocation> respawn, SfLocation capture, List<SfLocation> shops) {
        this.respawn = respawn;
        this.capture = capture;
        this.shops = shops;
    }

}
