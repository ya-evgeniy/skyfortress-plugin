package ru.jekarus.skyfortress.v3.serializer.language;

import com.google.common.reflect.TypeToken;
import jekarus.hocon.config.serializer.ConfigSerializer;
import jekarus.hocon.config.serializer.annotation.ConfigPath;
import jekarus.hocon.config.serializer.annotation.handler.AnnotationHandler;
import jekarus.hocon.config.serializer.state.ConfigDeserializeState;
import lombok.val;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.translation.locale.Locales;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lang.SfTeamLanguage;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SfLanguagesSerializer {

    private final SkyFortressPlugin plugin;
    private final Path pluginDirectory;
    private final Path langs;
    private final Path langsDirectory;

    public SfLanguagesSerializer(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.pluginDirectory = Sponge.getConfigManager().getPluginConfig(this.plugin).getDirectory();
        this.langs = Paths.get(this.pluginDirectory.toString(), "langs.conf");
        this.langsDirectory = Paths.get(this.pluginDirectory.toString(), "langs");
    }

    public void load()
    {
        CommentedConfigurationNode langs = this.load(this.langs, "/assets/langs.conf").getValue();
        if (langs != null)
        {
            try
            {
                this.construct(langs);
            }
            catch (ObjectMappingException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Pair<HoconConfigurationLoader, CommentedConfigurationNode> load(Path file, String def) {
        if (!Files.exists(file)) {
            if (!this.createDefault(file, def)) {
                return null;
            }
        }

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(file)
//                .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.DEF_SERIALIZERS))
                .build();
        try {
            return new Pair<>(loader, loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void construct(CommentedConfigurationNode node) throws ObjectMappingException
    {
        CommentedConfigurationNode langs = node.getNode("langs");

        SfLanguages languages = this.plugin.getLanguages();
        languages.setDef(Locales.of(langs.getNode("default").getString()));

        List<String> paths = langs.getNode("paths").getList(TypeToken.of(String.class));
        for (String path : paths)
        {
            Path langPath = Paths.get(this.langsDirectory.toString(), path);
            final val result = this.load(langPath, "/assets/langs/" + langPath.getFileName().toString());
            if (result != null)
            {
                final val serializer = new ConfigSerializer(result.getKey());
                this.constructLang(serializer, result.getValue(), languages);
            }
        }
    }

    private void constructLang(ConfigSerializer serializer, CommentedConfigurationNode node, SfLanguages languages) throws ObjectMappingException
    {
//        SfLanguage language = new SfLanguage();
//        language.locale = Locales.of(node.getNode("locale").getString());
//
//        language.map = node.getNode("map").getValue(TypeToken.of(SfMapLanguage.class));
//
//        CommentedConfigurationNode teamsNode = node.getNode("teams");
//        for (SfTeam sfTeam : this.plugin.getTeamContainer().getCollection())
//        {
//            SfTeamLanguage teamLanguage = new SfTeamLanguage();
//            CommentedConfigurationNode teamNode = teamsNode.getNode(sfTeam.getUniqueId());
//            teamLanguage.names = teamNode.getNode("names").getList(TypeToken.of(String.class));
//            language.teams.put(sfTeam, teamLanguage);
//        }
//
//        language.scoreboard = node.getNode("scoreboard").getValue(TypeToken.of(SfScoreboardLanguage.class));
//        language.messages = node.getNode("messages").getValue(TypeToken.of(SfMessagesLanguage.class));
//        language.distribution = node.getNode("distribution").getValue(TypeToken.of(SfDistributionLanguage.class));

        serializer.getRegistry().getAnnotationsRegistry().registerAnnotationHandlers(new TestHandlers());
        final val language = serializer.deserialize(node, SfLanguage.class);
        languages.add(language);
    }

    public static class TestHandlers {

        @AnnotationHandler(
                state = AnnotationHandler.State.PRE,
                call = AnnotationHandler.Call.ALWAYS,
                priority = AnnotationHandler.Priority.HIGH
        )
        private void on(ConfigPath path, ConfigDeserializeState state) {
            System.out.println(getFullPath(state.getNode()));
        }

        private String getFullPath(CommentedConfigurationNode node) {
            StringBuilder builder = new StringBuilder();
            while (node != null) {
                builder.append(node.getKey()).append(" ");
                node = node.getParent();
            }
            return builder.toString();
        }

    }

    private boolean createDefault(Path file, String def)
    {
        if (!Files.exists(file.getParent()))
        {
            try
            {
                Files.createDirectories(file.getParent());
            }
            catch (IOException e)
            {
                return false;
            }
        }

        InputStream source = SkyFortressPlugin.class.getResourceAsStream(def);
        try {
            Files.copy(source, file, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static Map<SfTeam, SfTeamLanguage> teams(ConfigDeserializeState state) throws ObjectMappingException {
        Map<SfTeam, SfTeamLanguage> result = new HashMap<>();
        for (SfTeam sfTeam : SkyFortressPlugin.getInstance().getTeamContainer().getCollection()) {
            SfTeamLanguage teamLanguage = new SfTeamLanguage();
            CommentedConfigurationNode teamNode = state.getNode().getNode(sfTeam.getUniqueId());
            teamLanguage.names = teamNode.getNode("names").getList(TypeToken.of(String.class));
            result.put(sfTeam, teamLanguage);
        }
        return result;
    }

}
