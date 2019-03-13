package ru.jekarus.skyfortress.v3.serializer.language;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.translation.locale.Locales;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.lang.SfLanguages;
import ru.jekarus.skyfortress.v3.lang.SfMapLanguage;
import ru.jekarus.skyfortress.v3.lang.SfTeamLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfDistributionLanguage;
import ru.jekarus.skyfortress.v3.lang.messages.SfMessagesLanguage;
import ru.jekarus.skyfortress.v3.lang.scoreboard.SfScoreboardLanguage;
import ru.jekarus.skyfortress.v3.serializer.SfSerializers;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
        CommentedConfigurationNode langs = this.load(this.langs, "/assets/langs.conf");
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

    public CommentedConfigurationNode load(Path file, String def)
    {
        if (!Files.exists(file))
        {
            if (!this.createDefault(file, def))
            {
                return null;
            }
        }

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(file)
                .setDefaultOptions(ConfigurationOptions.defaults().setSerializers(SfSerializers.DEF_SERIALIZERS))
                .build();
        try
        {
            return loader.load();
        }
        catch (IOException e)
        {
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
            CommentedConfigurationNode langFileNode = this.load(langPath, "/assets/langs/" + langPath.getFileName().toString());
            if (langFileNode != null)
            {
                this.constructLang(langFileNode, languages);
            }
        }
    }

    private void constructLang(CommentedConfigurationNode node, SfLanguages languages) throws ObjectMappingException
    {
        SfLanguage language = new SfLanguage();
        language.locale = Locales.of(node.getNode("locale").getString());

        language.map = node.getNode("map").getValue(TypeToken.of(SfMapLanguage.class));

        CommentedConfigurationNode teamsNode = node.getNode("teams");
        for (SfTeam sfTeam : this.plugin.getTeamContainer().getCollection())
        {
            SfTeamLanguage teamLanguage = new SfTeamLanguage();
            CommentedConfigurationNode teamNode = teamsNode.getNode(sfTeam.getUniqueId());
            teamLanguage.names = teamNode.getNode("names").getList(TypeToken.of(String.class));
            language.teams.put(sfTeam, teamLanguage);
        }

        language.scoreboard = node.getNode("scoreboard").getValue(TypeToken.of(SfScoreboardLanguage.class));
        language.messages = node.getNode("messages").getValue(TypeToken.of(SfMessagesLanguage.class));
        language.distribution = node.getNode("distribution").getValue(TypeToken.of(SfDistributionLanguage.class));

        languages.add(language);
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

}
