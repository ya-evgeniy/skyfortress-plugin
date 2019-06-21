package ru.jekarus.skyfortress.v3.resource;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Ignore;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import jekarus.hocon.config.serializer.annotation.converter.MethodConverter;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EntityResource extends SfResource {

    @Getter @Setter private Options options = new Options();

    @ConfigPath("entity_type") @MethodConverter(inClass = EntityResource.class, method = "getType")
    @Getter @Setter EntityType entityType;

    @Ignore
    @Getter @Setter private boolean runned = true;

    @Ignore
    @Getter private List<WeakReference<Entity>> entities = new LinkedList<>();

    public static class Options extends SfResource.Options {

        @OptionalValue @ConfigPath("spawn_radius")
        @Getter private int range = 1;

        @OptionalValue @ConfigPath("remove_if_out_of_range")
        @Getter private boolean remove = false;

        @OptionalValue @ConfigPath("max_entities")
        @Getter private int count = 1;

    }

    private static EntityType getType(String type) {
        final Optional<EntityType> optionalType = Sponge.getRegistry().getType(EntityType.class, type);
        return optionalType.orElse(null);
    }

}
