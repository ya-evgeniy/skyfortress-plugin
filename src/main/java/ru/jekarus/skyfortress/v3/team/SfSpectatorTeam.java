package ru.jekarus.skyfortress.v3.team;

import org.spongepowered.api.text.format.TextColor;

public class SfSpectatorTeam extends SfTeam {

    public SfSpectatorTeam()
    {

    }

    public SfSpectatorTeam(String uniqueId, TextColor color)
    {
        super(uniqueId, color);
    }

    @Override
    public Type getType()
    {
        return Type.SPECTATOR;
    }
}
