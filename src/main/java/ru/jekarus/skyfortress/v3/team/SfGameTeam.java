package ru.jekarus.skyfortress.v3.team;

import org.spongepowered.api.text.format.TextColor;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;

public class SfGameTeam extends SfTeam {

    private String castleId;
    private SfCastle castle;

    public SfGameTeam()
    {

    }

    public SfGameTeam(String uniqueId, String castleId, TextColor color)
    {
        super(uniqueId, color);
        this.castleId = castleId;
    }

    @Override
    public void init(SkyFortressPlugin plugin)
    {
        super.init(plugin);
        this.castle = plugin.getCastleContainer().fromUniqueId(this.castleId).orElse(null);
    }

    public String getCastleId()
    {
        return this.castleId;
    }

    public void setCastleId(String castleId)
    {
        this.castleId = castleId;
    }

    public SfCastle getCastle()
    {
        return this.castle;
    }

    @Override
    public Type getType()
    {
        return Type.GAME;
    }
}
