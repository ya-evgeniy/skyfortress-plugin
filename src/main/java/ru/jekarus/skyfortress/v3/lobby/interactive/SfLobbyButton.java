package ru.jekarus.skyfortress.v3.lobby.interactive;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import ru.jekarus.skyfortress.v3.player.SfPlayer;

public abstract class SfLobbyButton {

    public abstract boolean pressButton(Player player, SfPlayer sfPlayer, BlockSnapshot snapshot);

}
