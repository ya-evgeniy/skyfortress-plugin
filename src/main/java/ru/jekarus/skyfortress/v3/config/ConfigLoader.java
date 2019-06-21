package ru.jekarus.skyfortress.v3.config;

import jekarus.hocon.config.serializer.ConfigSerializer;
import lombok.Getter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class ConfigLoader {

    protected final SkyFortressPlugin plugin;
    protected final Path path;

    @Getter private final Path pluginDirectory;
    @Getter private final Path configPath;

    @Getter private final Path assetsPath;

    public ConfigLoader(SkyFortressPlugin plugin, Path path) {
        this.plugin = plugin;
        this.path = path;

        this.pluginDirectory = Sponge.getConfigManager().getPluginConfig(this.plugin).getDirectory();
        this.configPath = this.pluginDirectory.resolve(this.path);

        this.assetsPath = Paths.get("/assets").resolve(this.path);
    }

    public void load() {
        if (!Files.exists(this.configPath)) {
            this.createDefault();
        }
        if (!Files.exists(this.configPath)) {
            return;
        }
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(this.configPath)
                .build();

        final ConfigSerializer serializer = new ConfigSerializer(loader);
        try {
            this.preDeserialize(serializer);

            final CommentedConfigurationNode node = loader.load();
            this.deserialize(serializer, node);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void deserialize(ConfigSerializer serializer, CommentedConfigurationNode node);

    protected abstract void preDeserialize(ConfigSerializer serializer);

    private void createDefault() {
        if (!Files.exists(this.pluginDirectory)) {
            try {
                Files.createDirectories(this.pluginDirectory);
            }
            catch (IOException e) {
                return;
            }
        }

        final String assetsPathString = assetsPath.toString();
        final String fixedAssetsPath = assetsPathString.replace("\\", "/");
        InputStream source = SkyFortressPlugin.class.getResourceAsStream(fixedAssetsPath);
        try {
            Files.copy(source, this.configPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            throw new RuntimeException("Cannot find path: " + fixedAssetsPath, e);
        }
    }

}
