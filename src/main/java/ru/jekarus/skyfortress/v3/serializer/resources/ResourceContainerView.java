package ru.jekarus.skyfortress.v3.serializer.resources;

import jekarus.hocon.config.serializer.annotation.Generics;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import lombok.Getter;
import ru.jekarus.skyfortress.v3.resource.BlockResource;
import ru.jekarus.skyfortress.v3.resource.EntityResource;
import ru.jekarus.skyfortress.v3.resource.ItemResource;
import ru.jekarus.skyfortress.v3.resource.ResourceContainer;

import java.util.ArrayList;
import java.util.List;

public class ResourceContainerView {

    @OptionalValue @Generics(ItemResource.class)
    @Getter List<ItemResource> items = new ArrayList<>();

    @OptionalValue @Generics(BlockResource.class)
    @Getter List<BlockResource> blocks = new ArrayList<>();

    @OptionalValue @Generics(EntityResource.class)
    @Getter List<EntityResource> entities = new ArrayList<>();

    public void copyTo(ResourceContainer container) {
        System.out.println("COPY: " + items.size());

        container.getItems().addAll(this.items);
        container.getBlocks().addAll(this.blocks);
        container.getEntities().addAll(this.entities);
    }

}
