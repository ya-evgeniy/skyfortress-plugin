package ru.jekarus.skyfortress.v3.listener;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.PickupRule;
import org.spongepowered.api.data.type.PickupRules;
import org.spongepowered.api.entity.projectile.arrow.TippedArrow;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.CollideBlockEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;

import java.util.Optional;

public class ArrowMechanicsListener {

    private final SkyFortressPlugin plugin;

    public ArrowMechanicsListener(SkyFortressPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Sponge.getEventManager().registerListeners(this.plugin, this);
    }

    public void unregister() {
        Sponge.getEventManager().unregisterListeners(this);
    }

    @Listener
    public void onCollide(CollideBlockEvent.Impact event) {

        final Optional<TippedArrow> optionalArrow = event.getCause().first(TippedArrow.class);
        if (optionalArrow.isPresent()) {
            final TippedArrow arrow = optionalArrow.get();

            final Location<World> targetLocation = event.getTargetLocation();

            final Optional<Integer> optionalFireTicks = arrow.get(Keys.FIRE_TICKS);
            if (optionalFireTicks.isPresent()) {
                final Integer fireTicks = optionalFireTicks.get();
                if (fireTicks > 0) {

                    final Vector3d direction = event.getTargetSide().asOffset();

                    Location<World> flameLocation = targetLocation.add(direction);
                    if (flameLocation.getBlockType().equals(BlockTypes.AIR)) {
                        flameLocation.setBlockType(BlockTypes.FIRE);
                    }
                    else {
                        flameLocation = flameLocation.add(direction);
                        if (flameLocation.getBlockType().equals(BlockTypes.AIR)) {
                            flameLocation.setBlockType(BlockTypes.FIRE);
                        }
                    }

                }
            }

            final Optional<PickupRule> optionalPickupRule = arrow.get(Keys.PICKUP_RULE);
            if (optionalPickupRule.isPresent()) {
                final PickupRule pickupRule = optionalPickupRule.get();
                if (pickupRule.equals(PickupRules.CREATIVE_ONLY)) {
                    arrow.remove();
                }
            }

        }

    }
    /*
    interface Impact extends CollideBlockEvent, CollideEvent.Impact
     */
}
