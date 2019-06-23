package ru.jekarus.skyfortress.v3.game;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.PlayerData;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboards;
import ru.jekarus.skyfortress.v3.settings.LobbySettings;

public class EndGameStage extends SfGameStage {

    private final SkyFortressPlugin plugin;

    public EndGameStage(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        this.plugin.getScoreboards().setSideBar(SfScoreboards.Types.POST_GAME);
        this.plugin.getScoreboards().updatePlaces();
        final LobbySettings lobbySettings = plugin.getSettings().getLobby();
        SfPlayers players = SfPlayers.getInstance();
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            player.setLocationAndRotation(
                    lobbySettings.getCenter().getLocation(),
                    lobbySettings.getCenter().getRotation()
            );
            player.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
            PlayerData playerData = players.getOrCreatePlayer(player);
            this.plugin.getTeamContainer().getNoneTeam().addPlayer(this.plugin, playerData);
            playerData.setZone(PlayerZone.LOBBY);
            player.getInventory().clear();
            player.getOrCreate(PotionEffectData.class).ifPresent(effects -> {
                effects.addElement(
                        PotionEffect.builder().potionType(PotionEffectTypes.SATURATION).duration(1_000_000).amplifier(255).particles(false).build()
                );
                player.offer(effects);
            });
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public SfGameStageType getType() {
        return SfGameStageType.END_GAME;
    }

}