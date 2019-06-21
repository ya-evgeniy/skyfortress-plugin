package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigRegistry;
import jekarus.hocon.config.serializer.ConfigSerializer;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.serializer.base.BlockStateConfigSerializer;
import ru.jekarus.skyfortress.v3.serializer.base.ItemStackConfigSerializer;
import ru.jekarus.skyfortress.v3.serializer.resources.ResourceContainerView;

import java.nio.file.Paths;

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
