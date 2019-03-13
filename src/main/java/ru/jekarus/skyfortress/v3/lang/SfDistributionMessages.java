package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.messages.SfDistributionLanguage;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.*;

public class SfDistributionMessages {

    private final SkyFortressPlugin plugin;
    private final SfMessages messages;

    private final SfPlayers players;

    public SfDistributionMessages(SkyFortressPlugin plugin, SfMessages messages) {
        this.plugin = plugin;
        this.messages = messages;

        this.players = SfPlayers.getInstance();
    }

    public Map<Locale, Text> captainSelected(SfPlayer player, SfPlayer target, SfTeam team) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.distribution.captainSelected),
                vars -> vars
                        .playerKey().color(team).name(player)
                        .playerKey("target").color(team).name(target)
        );
    }

    public Map<Locale, Text> randomSelected(SfPlayer player, SfPlayer target, SfTeam team) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.distribution.randomSelected),
                vars -> vars
                        .playerKey().color(team).name(player)
                        .playerKey("target").color(team).name(target)
        );
    }

    public Map<Locale, Text> randomSelectedTime(int time) {
        return messages.construct(
                lang -> lang.distribution.randomSelectedTime,
                vars -> vars.key("time").number(time)
        );
    }

    public Text clickToSelect(SfPlayer player) {
        Text rightClick = rightClick(player);
        return messages.construct(
                player,
                lang -> lang.distribution.clickToSelect,
                vars -> vars.key("right.click").text(rightClick)
        );
    }

    public Text rightClick(SfPlayer player) {
        return messages.getLang(player).distribution.rightClick.toText();
    }

    public Text commandInfoHeader(SfPlayer player) {
        return messages.getLang(player).distribution.commandInfoHeader.toText();
    }

    public Text commandInfoRandom(SfPlayer player, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.distribution.commandInfoRandom,
                vars -> vars.teamKey().name(team)
        );
    }

    public Text commandInfoPlayer(SfPlayer player, SfPlayer target, SfTeam team) {
        return messages.construct(
                player,
                lang -> lang.distribution.commandInfoPlayer,
                vars -> vars
                        .teamKey().name(team)
                        .playerKey().name(target)
        );
    }

    public Text commandInfoDisabled(SfPlayer player, List<SfTeam> disabledTeams) {
        Text disabledTeamsText = commandInfoDisabledTeamsFormat(player, disabledTeams);
        return messages.construct(
                player,
                lang -> lang.distribution.commandInfoDisabled,
                vars -> vars.key("disabled.teams").text(disabledTeamsText)
        );
    }

    public Text commandInfoDisabledTeamsFormat(SfPlayer player, List<SfTeam> disabledTeams) {
        if (disabledTeams.size() < 1) {
            return Text.EMPTY;
        }

        SfLanguage lang = messages.getLang(player);
        SfDistributionLanguage distribution = lang.distribution;
        TextTemplate element = distribution.commandInfoDisabledFormatElement;
        TextTemplate separator = distribution.commandInfoDisabledFormatSeparator;
        TextTemplate last = distribution.commandInfoDisabledFormatLast;

        if (disabledTeams.size() == 1) {
            SfTeam team = disabledTeams.get(0);
            return new LanguageVariables(lang).teamKey().color(team).name(team).apply(element);
        }
        else {
            Text.Builder builder = Text.builder();
            SfTeam lastTeam = disabledTeams.remove(disabledTeams.size() - 1);
            Iterator<SfTeam> iterator = disabledTeams.iterator();
            if (iterator.hasNext()) {
                SfTeam next = iterator.next();
                builder.append(new LanguageVariables(lang).teamKey().name(next).apply(element));
                while (iterator.hasNext()) {
                    next = iterator.next();
                    builder.append(separator.toText());
                    builder.append(new LanguageVariables(lang).teamKey().name(next).apply(element));
                }
            }

            builder.append(new LanguageVariables(lang).teamKey().color(lastTeam).name(lastTeam).apply(last));
            return builder.build();
        }
    }

}
