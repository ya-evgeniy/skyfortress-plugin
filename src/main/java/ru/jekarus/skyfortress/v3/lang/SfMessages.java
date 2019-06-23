package ru.jekarus.skyfortress.v3.lang;

import lombok.Getter;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SfMessages {

    private final SkyFortressPlugin plugin;
    private final Server server;
    private final PlayersDataContainer players;

    private ThreadLocalRandom random;

    @Getter SfLobbyMessages lobby;
    @Getter SfGameMessages game;
    @Getter SfDistributionMessages distribution;
    @Getter ShopMessages shop;

    public SfMessages(SkyFortressPlugin plugin) {
        this.plugin = plugin;
        this.server = Sponge.getServer();
        this.players = PlayersDataContainer.getInstance();

        this.random = ThreadLocalRandom.current();

        this.lobby = new SfLobbyMessages(plugin, this);
        this.game = new SfGameMessages(plugin, this);
        this.distribution = new SfDistributionMessages(plugin, this);
        this.shop = new ShopMessages(plugin, this);
    }

    public void broadcast(Text text) {
        this.server.getBroadcastChannel().send(text);
    }

    public void broadcast(Map<Locale, Text> locatedTexts) {
        broadcast(locatedTexts, false);
    }

    public void broadcast(Map<Locale, Text> locatedTexts, boolean need_spaces) {
        for (Player player : this.server.getOnlinePlayers()) {
            PlayerData playerData = this.players.getOrCreatePlayer(player);
            Text text = locatedTexts.get(playerData.getLocale());
            if (text != null) {
                if (need_spaces) {
                    player.sendMessage(Text.of());
                    player.sendMessage(text);
                    player.sendMessage(Text.of());
                } else {
                    player.sendMessage(text);
                }
            }
        }
    }

    public void send(Collection<PlayerData> targets, Map<Locale, Text> locatedTexts) {
        this.send(targets, locatedTexts, ChatTypes.CHAT);
    }

    public void send(Collection<PlayerData> targets, Map<Locale, Text> locatedTexts, ChatType chatType) {
        for (PlayerData playerData : targets) {
            playerData.getPlayer().ifPresent(player -> {
                Text text = locatedTexts.get(playerData.getLocale());
                player.sendMessage(chatType, text);
            });
        }
    }

    public void sendToPlayers(Collection<Player> targets, Map<Locale, Text> locatedTexts) {
        List<PlayerData> sfTargets = targets.stream().map(this.players::getOrCreatePlayer).collect(Collectors.toList());
        this.send(sfTargets, locatedTexts);
    }

    public SfLanguage getLang(PlayerData player) {
        return this.plugin.getLanguages().get(player.getLocale());
    }

    public Text construct(PlayerData player, Function<SfLanguage, TextTemplate> textFromLanguage, Consumer<LanguageVariables> appendVariables) {
        SfLanguage lang = getLang(player);
        TextTemplate template = textFromLanguage.apply(lang);
        LanguageVariables variables = new LanguageVariables(lang);
        appendVariables.accept(variables);
        return variables.apply(template);
    }

    public Map<Locale, Text> construct(Function<SfLanguage, TextTemplate> textFromLanguage) {
        Collection<SfLanguage> languages = this.plugin.getLanguages().getLanguageByLocale().values();
        Map<Locale, Text> result = new HashMap<>();

        for (SfLanguage language : languages) {
            TextTemplate template = textFromLanguage.apply(language);
            result.put(language.locale, template.toText());
        }
        return result;
    }

    public <T> Map<Locale, T> constructAbstract(Function<SfLanguage, T> textFromLanguage) {
        Collection<SfLanguage> languages = this.plugin.getLanguages().getLanguageByLocale().values();
        Map<Locale, T> result = new HashMap<>();

        for (SfLanguage language : languages) {
            T template = textFromLanguage.apply(language);
            result.put(language.locale, template);
        }
        return result;
    }

    public Map<Locale, Text> construct(Function<SfLanguage, TextTemplate> textFromLanguage, Consumer<LanguageVariables> appendVariables) {
        Collection<SfLanguage> languages = this.plugin.getLanguages().getLanguageByLocale().values();
        Map<Locale, Text> result = new HashMap<>();

        for (SfLanguage language : languages) {
            TextTemplate template = textFromLanguage.apply(language);
            LanguageVariables variables = new LanguageVariables(language);
            appendVariables.accept(variables);
            result.put(language.locale, variables.apply(template));
        }
        return result;
    }

    public <T> T chooseRandom(List<T> templates) {
        return templates.get(random.nextInt(templates.size()));
    }

}