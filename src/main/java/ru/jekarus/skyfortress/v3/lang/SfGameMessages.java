package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.messages.SfTitleMessagesLanguage;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;

public class SfGameMessages {

    private final SkyFortressPlugin plugin;
    private final SfMessages messages;

    private final PlayersDataContainer playersData;

    public SfGameMessages(SkyFortressPlugin plugin, SfMessages messages) {
        this.plugin = plugin;
        this.messages = messages;

        this.playersData = plugin.getPlayersDataContainer();
    }

    public Map<Locale, Text> castleCapture(PlayerData player, SfTeam team) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.messages.game.castleCapture),
                vars -> vars.
                        playerKey().color(player.getTeam()).name(player)
                        .teamKey().name(team)
        );
    }

    public Map<Locale, Text> castleCaptured(SfTeam team) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.messages.game.castleCaptured),
                vars -> vars.teamKey().name(team)
        );
    }

    public Map<Locale, Text> castleForTeamYouCapturing() {
        return messages.construct(
                lang -> lang.messages.game.castleForTeamCapturing
        );
    }

    public Map<Locale, SfTitleMessagesLanguage> castleForTeamYouCaptured() {
        return messages.constructAbstract(
                lang -> messages.chooseRandom(lang.messages.game.castleForTeamCaptured)
        );
    }

    public Map<Locale, SfTitleMessagesLanguage> castleForTeamStrengthRemoved() {
        return messages.constructAbstract(
                lang -> messages.chooseRandom(lang.messages.game.castleForTeamStrengthRemoved)
        );
    }

    public Text castleHaveSeconds(PlayerData playerData, int ticks) {
        return messages.construct(
                playerData,
                lang -> lang.messages.game.castleHaveSeconds,
                vars -> vars.key("seconds").color(TextColors.GREEN).string(new DecimalFormat("0.00").format((double) ticks * 0.20 / 20))
        );
    }

    public Map<Locale, Text> castleGiveSeconds(PlayerData playerData, int ticks) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.messages.game.castleGiveSeconds),
                vars -> vars
                        .playerKey().color(playerData.getTeam()).name(playerData)
                        .key("seconds").color(TextColors.GREEN).string(new DecimalFormat("0.00").format((double) ticks * 0.20 / 20))
        );
    }

    public Map<Locale, Text> teamLost(SfTeam team) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.messages.game.teamLost),
                vars -> vars.teamKey().name(team)
        );
    }

    public Map<Locale, Text> teamWin(SfTeam team) {
        return messages.construct(
                lang -> messages.chooseRandom(lang.messages.game.teamWin),
                vars -> vars.teamKey().name(team)
        );
    }

}
