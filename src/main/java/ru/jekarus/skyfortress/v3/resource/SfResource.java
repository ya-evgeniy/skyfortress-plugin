package ru.jekarus.skyfortress.v3.resource;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Ignore;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

public class SfResource {

    @ConfigPath("unique_id")
    @Getter @Setter private String uniqueId;

    @OptionalValue @ConfigPath("team_id")
    @Getter @Setter private String teamId;

    @ConfigPath("spawn_delay")
    @Getter @Setter private int spawnDelay = 400;

    @MethodConverter(inClass = SfLocation.class, method = "createLocation") @ConfigPath("position")
    @Getter @Setter Location<World> location;

    @Ignore
    @Getter @Setter SfGameTeam team;

    @Ignore
    @Getter @Setter int leftTicks;

    public static class Options {

        @OptionalValue @ConfigPath("disable_if_team_lost")
        @Getter @Setter private boolean disableIfTeamLost = false;

    }

}
