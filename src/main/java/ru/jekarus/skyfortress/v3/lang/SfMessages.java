package ru.jekarus.skyfortress.v3.lang;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SfMessages {

    private final SkyFortressPlugin plugin;
    private final Server server;
    private final SfPlayers players;

    private ThreadLocalRandom random;

    public SfMessages(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.server = Sponge.getServer();
        this.players = SfPlayers.getInstance();

        this.random = ThreadLocalRandom.current();
    }

    public void broadcast(Text text)
    {
        this.server.getBroadcastChannel().send(text);
    }

    public void broadcast(Map<Locale, Text> locatedTexts)
    {
        broadcast(locatedTexts, false);
    }

    public void broadcast(Map<Locale, Text> locatedTexts, boolean need_spaces)
    {
        for (Player player : this.server.getOnlinePlayers())
        {
            SfPlayer sfPlayer = this.players.getOrCreatePlayer(player);
            Text text = locatedTexts.get(sfPlayer.getLocale());
            if (text != null)
            {
                if (need_spaces)
                {
                    player.sendMessage(Text.of());
                    player.sendMessage(text);
                    player.sendMessage(Text.of());
                }
                else
                {
                    player.sendMessage(text);
                }
            }
        }
    }

    public void send(Collection<SfPlayer> targets, Map<Locale, Text> locatedTexts)
    {
        for (SfPlayer sfPlayer : targets)
        {
            sfPlayer.getPlayer().ifPresent(player -> {
                Text text = locatedTexts.get(sfPlayer.getLocale());
                player.sendMessage(text);
            });
        }
    }

    public Map<Locale, Text> capture(SfGameTeam team, SfPlayer player)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            List<TextTemplate> captureMessage = language.messages.game.castle_capture;
            TextTemplate capture = captureMessage.get(this.random.nextInt(captureMessage.size()));

            Map<String, Text> params = new HashMap<>();
            params.put("player.name", Text.builder(player.getName()).color(player.getTeam().getColor()).build());
            appendTeamNames(params, "", team, language.teams.get(team));

            locatedText.put(locale, capture.apply(params).build());
        }

        return locatedText;
    }

    public Map<Locale, Text> captured(SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            List<TextTemplate> capturedMessage = language.messages.game.castle_captured;
            TextTemplate captured = capturedMessage.get(this.random.nextInt(capturedMessage.size()));

            Map<String, Text> params = new HashMap<>();
            appendTeamNames(params, "", team, language.teams.get(team));

            locatedText.put(locale, captured.apply(params).build());
        }

        return locatedText;
    }

    public Map<Locale, Text> lost(SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            List<TextTemplate> lostMessages = language.messages.game.team_lost;
            TextTemplate lost = lostMessages.get(this.random.nextInt(lostMessages.size()));

            Map<String, Text> params = new HashMap<>();
            appendTeamNames(params, "", team, language.teams.get(team));

            locatedText.put(locale, lost.apply(params).build());
        }

        return locatedText;
    }

    public Map<Locale, Text> win(SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            List<TextTemplate> winMessages = language.messages.game.team_win;
            TextTemplate win = winMessages.get(this.random.nextInt(winMessages.size()));

            Map<String, Text> params = new HashMap<>();
            appendTeamNames(params, "", team, language.teams.get(team));

            locatedText.put(locale, win.apply(params).build());
        }

        return locatedText;
    }

    public Text player_join(SfPlayer sfPlayer, SfTeam team)
    {
        SfLanguage language = this.plugin.getLanguages().get(sfPlayer.getLocale());
        return language.messages.lobby.player_join.toText();
    }

    public Map<Locale, Text> teammate_join(SfPlayer sfPlayer, SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            TextTemplate message = language.messages.lobby.teammate_join;

            Map<String, Text> params = new HashMap<>();
            params.put("player.name", Text.of(sfPlayer.getName()));

            locatedText.put(locale, message.apply(params).build());
        }

        return locatedText;
    }

    public Text player_joined(SfPlayer sfPlayer, SfTeam team)
    {
        SfLanguage language = this.plugin.getLanguages().get(sfPlayer.getLocale());
        TextTemplate message = language.messages.lobby.player_joined;
        return message.apply(
                appendTeamNames(new HashMap<>(), "", team, language.teams.get(team))
        ).build();
    }

    public Text player_accept(SfPlayer player, SfPlayer target, SfTeam team)
    {
        SfLanguage language = this.plugin.getLanguages().get(target.getLocale());
        TextTemplate message = language.messages.lobby.player_accept;
        return message.apply(
                ImmutableMap.of("player.name", Text.builder(player.getName()).color(team.getColor()).build())
        ).build();
    }

    public Map<Locale, Text> teammate_accept(SfPlayer player, SfPlayer target, SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            TextTemplate message = language.messages.lobby.teammate_accept;

            Map<String, Text> params = new HashMap<>();
            params.put("player.name", Text.builder(player.getName()).color(team.getColor()).build());
            params.put("target.player.name", Text.builder(target.getName()).color(team.getColor()).build());

            locatedText.put(locale, message.apply(params).build());
        }

        return locatedText;
    }

    public Text player_deny(SfPlayer player, SfPlayer target, SfGameTeam team)
    {
        SfLanguage language = this.plugin.getLanguages().get(target.getLocale());
        TextTemplate message = language.messages.lobby.player_deny;
        return message.apply(
                ImmutableMap.of("player.name", Text.builder(player.getName()).color(team.getColor()).build())
        ).build();
    }

    public Map<Locale, Text> teammate_deny(SfPlayer player, SfPlayer target, SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            TextTemplate message = language.messages.lobby.teammate_deny;

            Map<String, Text> params = new HashMap<>();
            params.put("player.name", Text.builder(player.getName()).color(team.getColor()).build());
            params.put("target.player.name", Text.builder(target.getName()).color(team.getColor()).build());

            locatedText.put(locale, message.apply(params).build());
        }

        return locatedText;
    }

    public Text player_leave(SfPlayer player, SfGameTeam team)
    {
        SfLanguage language = this.plugin.getLanguages().get(player.getLocale());
        TextTemplate message = language.messages.lobby.player_leave;
        return message.apply(
                appendTeamNames(new HashMap<>(), "", team, language.teams.get(team))
        ).build();
    }

    public Map<Locale, Text> teammate_leave(SfPlayer player, SfGameTeam team)
    {
        Map<Locale, Text> locatedText = new HashMap<>();
        SfLanguages languages = this.plugin.getLanguages();

        for (Map.Entry<Locale, SfLanguage> entry : languages.getMap().entrySet())
        {
            Locale locale = entry.getKey();
            SfLanguage language = entry.getValue();

            TextTemplate message = language.messages.lobby.teammate_leave;

            Map<String, Text> params = new HashMap<>();
            params.put("player.name", Text.builder(player.getName()).color(team.getColor()).build());

            locatedText.put(locale, message.apply(params).build());
        }

        return locatedText;
    }

    public static Map<String, Text> appendTeamNames(Map<String, Text> params, String prefix, SfTeam team, SfTeamLanguage language)
    {
        return appendTeamNames(params, prefix, team, language, true);
    }

    public static Map<String, Text> appendTeamNames(Map<String, Text> params, String prefix, SfTeam team, SfTeamLanguage language, boolean color)
    {
        int index = -1;
        for (String name : language.names)
        {
            if (color)
            {
                params.put(prefix + "team.name." + ++index, Text.builder(name).color(team.getColor()).build());
            }
            else
            {
                params.put(prefix + "team.name." + ++index, Text.builder(name).build());
            }
        }
        return params;
    }

//    public static TextTemplate applyTeam(TextTemplate template, String prefix, SfTeam team, SfTeamLanguage language)
//    {
//        return applyTeam(template, prefix, team, language, true);
//    }
//
//    public static TextTemplate applyTeam(TextTemplate template, SfTeam team, SfTeamLanguage language, boolean color)
//    {
//        return applyTeam(template, "", team, language, color);
//    }
//
//    public static TextTemplate applyTeam(TextTemplate template, String prefix, SfTeam team, SfTeamLanguage language, boolean color)
//    {
//        Map<String, Text> params = new HashMap<>();
//        appendTeamNames(params, prefix, team, language, color);
//        template.apply(params);
//        return template;
//    }

}
