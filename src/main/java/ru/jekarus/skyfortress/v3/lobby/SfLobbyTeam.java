package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.lobby.interactive.*;
import ru.jekarus.skyfortress.v3.player.PlayerZone;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfUtils;

import java.util.ArrayList;
import java.util.Collection;

public class SfLobbyTeam {

    private SkyFortressPlugin plugin;
    private SfLobbyTeamSettings settings;

    private SfLobbyPlateJoin joinPlate;
    private SfLobbyPlateLeave leavePlate;

    private SfLobbyButtonLeave leaveButton;

    private SfLobbyButtonAccept acceptButton;
    private SfLobbyButtonDeny denyButton;

    private SfLobbyButtonReady buttonReady;

    public SfLobbyTeam(SfLobbyTeamSettings settings)
    {
        this.settings = settings;
    }

    public void init(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
        this.settings.init(plugin);

        this.joinPlate = new SfLobbyPlateJoin(plugin, this, this.settings);
        this.leavePlate = new SfLobbyPlateLeave(plugin, this, this.settings);

        this.leaveButton = new SfLobbyButtonLeave(plugin, this, this.settings);

        this.acceptButton = new SfLobbyButtonAccept(plugin, this, this.settings);
        this.denyButton = new SfLobbyButtonDeny(plugin, this, this.settings);

        this.buttonReady = new SfLobbyButtonReady(plugin, this, this.settings);
    }

    public SfLobbyTeamSettings getSettings()
    {
        return this.settings;
    }

    public void setSettings(SfLobbyTeamSettings settings)
    {
        this.settings = settings;
    }

    public boolean standOnPlate(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        if (this.joinPlate.activate(player, sfPlayer, snapshot)) {
            return true;
        }
        if (this.leavePlate.activate(player, sfPlayer, snapshot)) {
            this.checkReady();
            return true;
        }
        return false;
    }

    public boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot)
    {
        return
                this.leaveButton.pressButton(player, sfPlayer, snapshot)
                || this.acceptButton.pressButton(player, sfPlayer, snapshot)
                || this.denyButton.pressButton(player, sfPlayer, snapshot)
                || this.buttonReady.pressButton(player, sfPlayer, snapshot);
    }

    public void playerDisconnect(SfPlayer sfPlayer, Player player)
    {
        if (this.settings.waitingPlayer == sfPlayer) {
            this.settings.waitingPlayer = null;
            this.setWaitingPlayer(
                    getJoinedPlayer()
            );
            return;
        }

        SfTeam gameTeam = sfPlayer.getTeam();
        if (gameTeam != this.settings.team)
        {
            return;
        }

        Collection<SfPlayer> players = this.settings.team.getPlayers();
        int countOnlinePlayers = (int) players.stream().filter(pla -> pla.getPlayer().isPresent()).count();

        SfTeam noneTeam = this.plugin.getTeamContainer().getNoneTeam();
        System.out.println(countOnlinePlayers);
        if (countOnlinePlayers - 1 < 1) {
            new ArrayList<>(players).forEach(pla -> noneTeam.addPlayer(plugin, pla));
            if (this.settings.waitingPlayer != null) {
                this.settings.team.addPlayer(plugin, this.settings.waitingPlayer);

                setCaptainPlayer(this.settings.waitingPlayer);

                this.settings.waitingPlayer = null;
                this.setWaitingPlayer(
                        getJoinedPlayer()
                );
            }
            else {
                this.setCaptainPlayer(
                        getJoinedPlayer()
                );
                if (this.settings.captain == null) {
                    this.checkReady();
                }
            }
        }
    }

    public SfPlayer getJoinedPlayer() {
        for (Player anotherPlayer : Sponge.getServer().getOnlinePlayers()) {
            if (SfUtils.compareByInt(anotherPlayer.getLocation(), settings.joinPlate)) {
                return SfPlayers.getInstance().getOrCreatePlayer(anotherPlayer);
            }
        }
        return null;
    }

    public void setWaitingPlayer(SfPlayer sfPlayer) {
        if (sfPlayer == null) return;
        if (this.settings.waitingPlayer != null) {
            SfPlayer waitingPlayer = this.settings.waitingPlayer;
            if (waitingPlayer == sfPlayer) return;
            plugin.getLobby().moveToLobby(sfPlayer);
        }
        this.settings.waitingPlayer = sfPlayer;
        sfPlayer.setZone(PlayerZone.TEAM_ROOM);
        sfPlayer.getPlayer().ifPresent(player -> {
            player.setLocationAndRotation(
                    settings.waitingLocation.getLocation(),
                    settings.waitingLocation.getRotation()
            );
        });
    }

    public void setCaptainPlayer(SfPlayer sfPlayer) {
        if (sfPlayer == null) return;
        this.settings.captain = sfPlayer;
        addToTeam(sfPlayer);
    }

    public void addToTeam(SfPlayer sfPlayer) {
        if (sfPlayer == null) return;
        sfPlayer.setZone(PlayerZone.TEAM_ROOM);
        this.settings.team.addPlayer(plugin, sfPlayer);
        sfPlayer.getPlayer().ifPresent(player -> {
            player.setLocationAndRotation(
                    settings.accepted.getLocation(),
                    settings.accepted.getRotation()
            );
        });
    }

    private void checkReady() {
        int size = this.settings.team.getPlayers().size();
        if (size < 1) {
            this.buttonReady.setReady(false);
        }
    }

}
