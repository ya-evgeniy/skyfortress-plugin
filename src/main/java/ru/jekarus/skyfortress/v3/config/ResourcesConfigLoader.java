package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigRegistry;
import jekarus.hocon.config.serializer.ConfigSerializer;
import lombok.val;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.config.ConfigLoader;
import ru.jekarus.skyfortress.v3.serializer.base.BlockStateConfigSerializer;
import ru.jekarus.skyfortress.v3.serializer.base.ItemStackConfigSerializer;
import ru.jekarus.skyfortress.v3.serializer.resources.ResourceContainerView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourcesConfigLoader extends ConfigLoader {

    public ResourcesConfigLoader(SkyFortressPlugin plugin) {
        super(plugin, Paths.get("resources.conf"));
    }

    @Override
    protected void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node) {
        System.out.println("DESERIALIZE");
        serializer.deserialize(node, ResourceContainerView.class).copyTo(this.plugin.getResourceContainer());
    }

    @Override
    protected void preDeserialize(ConfigSerializer serializer) {
        final ConfigRegistry registry = serializer.getRegistry();
        registry.registerSerializer(ItemStack.class, new ItemStackConfigSerializer());
        registry.registerSerializer(BlockState.class, new BlockStateConfigSerializer());
    }

}
