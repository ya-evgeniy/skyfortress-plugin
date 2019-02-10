package ru.jekarus.skyfortress.v3.resource;

import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SfResourceContainer {

    private Map<String, SfResource> resourceById = new HashMap<>();

    public SfResourceContainer()
    {

    }

    public void init(SkyFortressPlugin plugin)
    {

    }

    public void add(SfResource resource)
    {
        this.resourceById.put(resource.getUniqueId(), resource);
    }

    public void remove(SfResource resource)
    {
        this.resourceById.remove(resource.getUniqueId());
    }

    public Optional<SfResource> fromUniqueId(String uniqueId)
    {
        return Optional.ofNullable(this.resourceById.get(uniqueId));
    }

    public Collection<SfResource> getCollection()
    {
        return this.resourceById.values();
    }

}
