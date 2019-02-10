package ru.jekarus.skyfortress.v3.castle;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SfCastleContainer {

    private Map<String, SfCastle> castleById = new HashMap<>();

    public SfCastleContainer()
    {

    }

    public void init(SkyFortressPlugin plugin)
    {
        for (SfCastle castle : this.castleById.values())
        {
            castle.init(plugin);
        }
    }

    public void add(SfCastle castle)
    {
        this.castleById.put(castle.getUniqueId(), castle);
    }

    public void remove(SfCastle castle)
    {
        this.castleById.remove(castle.getUniqueId());
    }

    public Optional<SfCastle> fromUniqueId(String uniqueId)
    {
        return Optional.ofNullable(this.castleById.get(uniqueId));
    }

    public Collection<SfCastle> getCollection()
    {
        return this.castleById.values();
    }

}
