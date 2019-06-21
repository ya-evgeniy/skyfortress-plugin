package ru.jekarus.skyfortress.v3.settings;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import lombok.Getter;
import lombok.Setter;
import ru.jekarus.skyfortress.v3.utils.LocationAndRotation;

@ConfigPath("lobby.settings")
public class LobbySettings {

    @ConfigPath("center")
    @Getter @Setter private LocationAndRotation center;

    @ConfigPath("min_y")
    @Getter @Setter private double minY;

}
