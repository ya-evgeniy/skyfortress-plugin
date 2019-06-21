package ru.jekarus.skyfortress.v3.lobby;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.lobby.interaction.AcceptButtonBlockInteraction;
import ru.jekarus.skyfortress.v3.lobby.interaction.DenyButtonBlockInteraction;
import ru.jekarus.skyfortress.v3.lobby.interaction.JoinPlateBlockInteraction;
import ru.jekarus.skyfortress.v3.lobby.interaction.LeaveButtonBlockInteraction;
import ru.jekarus.skyfortress.v3.lobby.interaction.LeavePlateBlockInteraction;
import ru.jekarus.skyfortress.v3.lobby.interaction.ReadyButtonBlockInteraction;
import ru.jekarus.skyfortress.v3.player.SfPlayer;

public class LobbyRoomInteractions {

    private final LobbyRoom room;

    private JoinPlateBlockInteraction joinPlate;
    private LeavePlateBlockInteraction leavePlate;

    private AcceptButtonBlockInteraction acceptButton;
    private DenyButtonBlockInteraction denyButton;

    private LeaveButtonBlockInteraction leaveButton;
    private ReadyButtonBlockInteraction readyButton;

    public LobbyRoomInteractions(LobbyRoom room) {
        this.room = room;
    }

    public void init() {
        this.joinPlate = new JoinPlateBlockInteraction(this.room);
        this.leavePlate = new LeavePlateBlockInteraction(this.room);

        this.acceptButton = new AcceptButtonBlockInteraction(this.room);
        this.denyButton = new DenyButtonBlockInteraction(this.room);

        this.leaveButton = new LeaveButtonBlockInteraction(this.room);
        this.readyButton = new ReadyButtonBlockInteraction(this.room);
    }

    public boolean standOnPlate(Player player, SfPlayer playerData, BlockSnapshot block) {
        return joinPlate.activate(player, playerData, block)
                || leavePlate.activate(player, playerData, block);
    }

    public boolean pressButton(Player player, SfPlayer playerData, BlockSnapshot block) {
        return acceptButton.activate(player, playerData, block)
                || denyButton.activate(player, playerData, block)
                || leaveButton.activate(player, playerData, block)
                || readyButton.activate(player, playerData, block);
    }

}
