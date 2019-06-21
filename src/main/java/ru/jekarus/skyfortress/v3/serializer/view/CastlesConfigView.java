package ru.jekarus.skyfortress.v3.serializer.view;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.converter.GenericMethodConverters;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;

import java.util.List;

public class CastlesConfigView {

    @Generics(SfCastle.class) @GenericMethodConverters(@MethodConverter(inClass = CastlesConfigView.class, method = "createCastle"))
    public List<SfCastle> castles;

    public void addTo(SfCastleContainer container) {
        for (SfCastle castle : this.castles) {
            container.register(castle);
        }
    }

    public static SfCastle createCastle(
            @ConfigPath("unique_id") String uniqueId,
            @ConfigPath("team_id") String teamId,
            @ConfigPath("positions") SfCastlePositions positions
    ) {
        return new SfCastle(uniqueId, teamId, positions);
    }

}
