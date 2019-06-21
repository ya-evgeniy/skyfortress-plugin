package ru.jekarus.skyfortress.v3.resource;

import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.Ignore;
import jekarus.hocon.config.serializer.annotation.OptionalValue;
import lombok.Getter;
import lombok.Setter;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStack;

import java.lang.ref.WeakReference;

public class ItemResource extends SfResource {

    @Getter @Setter private Options options = new Options();

    @Getter @Setter private ItemStack item;

    @Ignore
    @Getter @Setter WeakReference<Item> itemEntityReference = new WeakReference<>(null);

    public static class Options extends SfResource.Options {

        @OptionalValue @ConfigPath("can_stack")
        @Getter private boolean canStack;

    }

}
