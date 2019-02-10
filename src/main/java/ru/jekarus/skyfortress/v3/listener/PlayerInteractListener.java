package ru.jekarus.skyfortress.v3.listener;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.gui.ShopGui;

public class PlayerInteractListener {

    private final SkyFortressPlugin plugin;

    public PlayerInteractListener(SkyFortressPlugin plugin)
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
    public void onInteract(InteractBlockEvent.Secondary.MainHand event, @First Player player)
    {
//        Optional<ItemStack> optionalItemStack = player.getItemInHand(HandTypes.MAIN_HAND);
//        if (optionalItemStack.isPresent())
//        {
//            ItemStack itemStack = optionalItemStack.get();
//            if (itemStack.getType().equals(ItemTypes.GOLD_INGOT))
//            {
//                BlockSnapshot targetBlock = event.getTargetBlock();
//                Direction targetSide = event.getTargetSide();
//                targetBlock.getLocation().ifPresent(location -> {
//                    Location<World> relative = location.getRelative(targetSide);
//                    for (SfCastle gameCastle : this.plugin.getCastleContainer().getCollection())
//                    {
//                        Location<World> goldBlockPosition = gameCastle.getPositions().getCapture().getLocation().getRelative(Direction.DOWN);
//                        if (goldBlockPosition.getBlockX() == relative.getBlockX() && goldBlockPosition.getBlockY() == relative.getBlockY() && goldBlockPosition.getBlockZ() == relative.getBlockZ())
//                        {
//                            if (!relative.getBlockType().equals(BlockTypes.AIR))
//                            {
//                                return;
//                            }
//                            Task.builder()
//                                    .delayTicks(1)
//                                    .execute(() -> {relative.setBlockType(BlockTypes.GOLD_BLOCK);})
//                                    .submit(SkyFortressPlugin.getInstance());
//
//                            player.get(Keys.GAME_MODE).ifPresent(gameMode -> {
//                                if (!gameMode.equals(GameModes.CREATIVE))
//                                {
//                                    itemStack.setQuantity(itemStack.getQuantity() - 1);
//                                    player.setItemInHand(HandTypes.MAIN_HAND, itemStack);
//                                }
//                            });
//                            return;
//                        }
//                    }
//                });
//            }
//        }
    }

    @Listener
    public void onEntityInteract(InteractEntityEvent event, @First Player player)
    {
        Entity entity = event.getTargetEntity();
        if (entity.getType().equals(EntityTypes.ILLUSION_ILLAGER))
        {
            player.openInventory(ShopGui.INSTANCE.getInventory());
        }
    }

}
