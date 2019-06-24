package ru.jekarus.skyfortress.v3.engine;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.title.Title;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.castle.SfCastle;
import ru.jekarus.skyfortress.v3.castle.SfCastleContainer;
import ru.jekarus.skyfortress.v3.castle.SfCastlePositions;
import ru.jekarus.skyfortress.v3.lang.SfMessages;
import ru.jekarus.skyfortress.v3.lang.messages.SfTitleMessagesLanguage;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.PlayersDataContainer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class CheckCaptureEngine {

    private final SkyFortressPlugin plugin;

    private final CaptureEngine captureEngine;
    private final SfTeamContainer teamContainer;
    private final SfCastleContainer castleContainer;

    private final PlayersDataContainer playersData;

    private Task task;
    private boolean enabled = false;

    public CheckCaptureEngine(SkyFortressPlugin plugin, CaptureEngine captureEngine)
    {
        this.plugin = plugin;

        this.captureEngine = captureEngine;
        this.teamContainer = this.plugin.getTeamContainer();
        this.castleContainer = this.plugin.getCastleContainer();

        this.playersData = plugin.getPlayersDataContainer();
    }

    public void start()
    {
        if (this.enabled)
        {
            return;
        }
        this.enabled = true;
        this.task = Task.builder()
                .name(this.getClass().getName())
                .execute(this::run)
                .intervalTicks(10)
                .submit(this.plugin);
    }

    public void stop()
    {
        if (!this.enabled)
        {
            return;
        }
        this.enabled = false;
        this.task.cancel();
    }

    private void run()
    {
        for (SfGameTeam gameTeam : this.teamContainer.getGameCollection())
        {
            this.tryTeamCapture(gameTeam);
        }
    }

    private void tryTeamCapture(SfGameTeam gameTeam)
    {
        for (PlayerData playerData : gameTeam.getPlayers())
        {
            this.tryPlayerCapture(playerData);
        }
    }

    private void tryPlayerCapture(PlayerData playerData)
    {
        if (playerData.getLastPlayed() != -1)
        {
            return;
        }
        Optional<Player> optionalPlayer = playerData.getPlayer();
        if (optionalPlayer.isPresent())
        {
            Player player = optionalPlayer.get();
            if (player.isOnline())
            {
                if (playerData.getCapturePoints() > 0) {
                    final SfMessages messages = plugin.getMessages();
                    player.sendMessage(
                            ChatTypes.ACTION_BAR,
                            messages.getGame().castleHaveSeconds(playerData, playerData.getCapturePoints())
                    );
                }
                if (!CaptureEngine.checkGoldBlock(player))
                {
                    return;
                }
                SfTeam team = playerData.getTeam();
                for (SfCastle castle : this.castleContainer.getCollection())
                {
                    if (!castle.isAlive()) continue;
                    if (tryCastleCapture(castle, playerData, player, team))
                    {
                        return;
                    }
                }
            }
        }
    }

    private boolean tryCastleCapture(SfCastle castle, PlayerData playerData, Player player, SfTeam team) {
        SfCastlePositions positions = castle.getPositions();
        if (SfUtils.checkLocation(player, positions.getCapture().getLocation())) {
            if (castle.getTeam() == team) {
                if (playerData.getCapturePoints() > 0) {
                    final boolean captured = castle.isCaptured();
                    castle.setAdditionalHealth(castle.getAdditionalHealth() + (int) (playerData.getCapturePoints() * 0.20));
                    plugin.getScoreboards().updateLeftSeconds(castle.getTeam());

                    final SfMessages messages = plugin.getMessages();
                    messages.sendToPlayers(
                            Sponge.getServer().getOnlinePlayers(),
                            messages.getGame().castleGiveSeconds(playerData, playerData.getCapturePoints())
                    );

                    playerData.setCapturePoints(0);

                    if (!captured) return true;

                    final Map<Locale, SfTitleMessagesLanguage> localized = messages.getGame().castleForTeamStrengthRemoved();

                    for (PlayerData teammatePlayerData : castle.getTeam().getPlayers()) {
                        teammatePlayerData.getPlayer().ifPresent(onlineTeammate -> {
                            final SfTitleMessagesLanguage title = localized.get(teammatePlayerData.getLocale());
                            if (title != null) {
                                player.sendTitle(Title.of(title.top.toText(), title.bottom.toText()));
                            }
                            onlineTeammate.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                                onlineTeammate.offer(
                                        effects.removeAll(potionEffect -> potionEffect.getType().equals(PotionEffectTypes.STRENGTH))
                                );
                            });
                        });
                    }
                }
            }
            else {
                if (castle.isCaptured()) return true;
                this.captureEngine.addCapture(castle, playerData);
            }
            return true;
        }
        return false;
    }

}
