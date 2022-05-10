package ru.jekarus.skyfortress.module.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SfDragon {

    public enum State {
        WANDERING,
        GET_ON_POSITION,
        ATTACK_TARGET;
    }

    public final EnderDragon entity;
    public final Location location;
    public State state = State.WANDERING;
    public Player target;
    public long lastAttackTick = Bukkit.getCurrentTick();


    public SfDragon(EnderDragon entity, Location location) {
        this.entity = entity;
        this.location = location;
        entity.setSilent(true);
        entity.getPathfinder().stopPathfinding();
        Bukkit.getMobGoals().removeAllGoals(entity);
        entity.setPodium(location);
        actState();
    }

    public void setState(State state) {
        if (state == this.state) return;
        this.state = state;
        actState();
    }

    private void actState() {
        switch (state) {
            case WANDERING -> {
                moveTo(entity.getLocation().clone().add(entity.getLocation().getDirection().multiply(7)), 4, () -> {
                    moveTo(location, 0, null);
                });
            }
            case GET_ON_POSITION -> {
                moveTo(location, 4, () -> {
                    setState(State.ATTACK_TARGET);
                });
            }
            case ATTACK_TARGET -> {
                entity.setPhase(EnderDragon.Phase.HOVER);
            }
        }
    }

    public void moveTo(Location moveTo, double distance, @Nullable Runnable onDone) {
        double distanceSquared = distance * distance;
        DragonNms.setPhase(entity, EnderDragon.Phase.LEAVE_PORTAL, new DragonNms.SfDragonPhase() {

            @Override
            public void serverTick() {
                if (onDone == null) return;
                double d = entity.getLocation().distanceSquared(moveTo);
                if (d < distanceSquared) {
                    onDone.run();
                }
            }

            @Override
            public Vector getFlyTargetLocation() {
                return moveTo.toVector();
            }
        });
    }

    public void tryAttack() {
        final var currentTick = Bukkit.getCurrentTick();
        if(currentTick > (lastAttackTick + 20 * 7)) {
            breathAttack(() -> {
                entity.setPhase(EnderDragon.Phase.HOVER);
            });
            lastAttackTick = currentTick;
        }
    }

    public void breathAttack(@NotNull Runnable onDone) {
        DragonNms.setPhase(entity, EnderDragon.Phase.BREATH_ATTACK, new DragonNms.SfDragonPhase() {

            int flameTicks = 1;
            @Override
            public void serverTick() {
                ++this.flameTicks;
                if (this.flameTicks >= 200) {
                    onDone.run();
                } else if(this.flameTicks == 10) {
                    final var loc = target.getLocation();
                    loc.getWorld().spawn(
                            loc, AreaEffectCloud.class,
                            CreatureSpawnEvent.SpawnReason.DEFAULT,
                            cloud -> {
                                cloud.setSource(entity);
                                cloud.setRadius(5.0f);
                                cloud.setDuration(200);
                                cloud.setParticle(Particle.DRAGON_BREATH);
                                cloud.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 0, 0), false);
                            }
                    );
                }
            }

        });
    }

}
