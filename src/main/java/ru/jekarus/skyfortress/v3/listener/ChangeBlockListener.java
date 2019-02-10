package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.concurrent.TimeUnit;

public class ChangeBlockListener {

    private final SkyFortressPlugin plugin;

    public ChangeBlockListener(SkyFortressPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void register()
    {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister()
    {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @First Player player)
    {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions())
        {
            BlockSnapshot blockSnapshot = transaction.getOriginal();
            BlockState state = blockSnapshot.getState();

            if(state.getType().equals(BlockTypes.LAPIS_ORE))
            {
                blockSnapshot.getLocation().ifPresent(location -> {
                    if (location.getBlockX() == 2500 && location.getBlockY() == 96 && location.getBlockZ() == 0)
                    {
                        location.setBlockType(BlockTypes.BEDROCK);

                        Task.builder().delay(10, TimeUnit.SECONDS).execute(() -> {
                            this.plugin.getWorld().getLocation(2500, 96, 0).setBlockType(BlockTypes.LAPIS_ORE);
                        }).submit(this.plugin);
                    }
                });
            }
        }
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player)
    {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions())
        {
            BlockSnapshot snapshot = transaction.getFinal();
            BlockState state = snapshot.getState();
            if (state.getType().equals(BlockTypes.BED))
            {
                event.setCancelled(true);
            }
        }
//        for (Transaction<BlockSnapshot> transaction : event.getTransactions())
//        {
//            BlockSnapshot blockSnapshot = transaction.getFinal();
//            BlockState state = blockSnapshot.getState();
//
//            if(state.getType().equals(BlockTypes.LAPIS_BLOCK) || state.getType().equals(BlockTypes.GOLD_BLOCK))
//            {
//                Optional<GameMode> optionalGameMode = player.get(Keys.GAME_MODE);
//                if (optionalGameMode.isPresent())
//                {
//                    GameMode gameMode = optionalGameMode.get();
//                    if (!gameMode.equals(GameModes.CREATIVE))
//                    {
//                        transaction.setValid(false);
//                    }
//                }
//                else
//                {
//                    transaction.setValid(false);
//                }
//            }
//        }
    }

}
