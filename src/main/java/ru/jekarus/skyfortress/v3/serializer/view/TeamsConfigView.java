package ru.jekarus.skyfortress.v3.serializer.view;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.GenericMethodConverters;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import jekarus.hocon.config.serializer.converter.SpongeConverters;
import org.spongepowered.api.text.format.TextColor;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfSpectatorTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;

import java.util.List;

public class TeamsConfigView {

    @Generics(SfTeam.class)
    @GenericMethodConverters(@MethodConverter(inClass = TeamsConfigView.class, method = "createTeam"))
    public List<SfTeam> teams;

    public void addTo(SfTeamContainer container) {
        for (SfTeam team : teams) {
            container.register(team);
        }
    }

    public static SfTeam createTeam(
            @OptionalValue @ConfigPath("type") String type,
            @ConfigPath("unique_id") String uniqueId,
            @ConfigPath("color") @MethodConverter(inClass = SpongeConverters.class, method = "textColor") TextColor color
    ) {
        switch (type) {
            case "game":
                return new SfGameTeam(
                        uniqueId,
                        color
                );
            case "spectator":
                return new SfSpectatorTeam(
                        uniqueId,
                        color
                );
            default:
                return new SfTeam(
                        uniqueId,
                        color
                );
        }
    }

}