package ru.jekarus.skyfortress.v3.settings;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import lombok.Getter;
import lombok.Setter;

@ConfigPath("settings")
public class WorldMapSettings {

    @OptionalValue @ConfigPath("player_offline_death")
    @Getter @Setter private int playerOfflineDeath = 120;

    @OptionalValue @ConfigPath("castle_health")
    @Getter @Setter private int castleHealth = 1000;

    @OptionalValue @ConfigPath("castle_death_seconds")
    @Getter @Setter private int castleDeathSeconds = 30;

}
