package ru.jekarus.skyfortress.v3.team;

import lombok.NonNull;
import org.spongepowered.api.text.format.TextColor;

public class SfSpectatorTeam extends SfTeam {

    public SfSpectatorTeam(@NonNull String uniqueId, @NonNull TextColor color) {
        super(Type.SPECTATOR, uniqueId, color);
    }

}
