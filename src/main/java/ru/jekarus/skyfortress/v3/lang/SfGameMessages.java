package ru.jekarus.skyfortress.v3.lang;

import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lang.messages.SfTitleMessagesLanguage;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.Locale;
import java.util.Map;

public class SfGameMessages {

    private final SkyFortressPlugin plugin;
    private final SfMessages messages;

    private final PlayersDataContainer players;

    public SfGameMessages(SkyFortressPlugin plugin, SfMessages messages) {
        this.plugin = plugin;
        this.messages = messages;

        this.players = PlayersDataContainer.getInstance();
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
