package ru.jekarus.skyfortress.module.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;

public class SfDragon extends SfObjectModule.SfEntity<EnderDragon> {

    public enum State {
        WANDERING,
        GET_ON_POSITION,
        ATTACK_TARGET;
    }
    private State state = State.WANDERING;
    private Player target;
    private long lastAttackTick = Bukkit.getCurrentTick();
    private boolean agro = false;


    public SfDragon(EnderDragon entity, Location location) {
        super(entity, location);

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

    @Override
    public void tickState() {
        if(this.state == SfDragon.State.GET_ON_POSITION) return;
        final var players = this.location.getNearbyEntitiesByType(Player.class, SfConfig.OBJECT_HOME_RADIUS);
        if(players.size() > 0) {
            if(this.state == SfDragon.State.ATTACK_TARGET) return;
            this.target = players.iterator().next();
            this.setState(SfDragon.State.GET_ON_POSITION);
        } else {
            this.agro = false;
            this.setState(SfDragon.State.WANDERING);
        }
    }

    @Override
    public void tickMoving() {
        if(this.state != SfDragon.State.ATTACK_TARGET) return;
        final var loc = this.entity.getLocation().clone();
        final var yaw = loc.getYaw();  // [-180:180]
        final var pitch = loc.getPitch();  // [-90:90]
        final var delta = this.target.getLocation().add(0, 3, 0).subtract(loc).toVector();
        loc.setDirection(
                delta.multiply(-1)  // invert dragon look
        );
        var dyaw = Location.normalizeYaw(loc.getYaw()) - yaw;
        if(dyaw > 180) dyaw = 360 - dyaw;
        if(dyaw < -180) dyaw = 360 + dyaw;
        var dpitch = Location.normalizePitch(loc.getPitch()) - pitch;

        final var rotSpeed = this.entity.getHeadRotationSpeed();
        if(dyaw > rotSpeed) dyaw = rotSpeed;
        else if(dyaw < -rotSpeed) dyaw = -rotSpeed;
        if(dpitch > rotSpeed) dpitch = rotSpeed;
        else if(dpitch < rotSpeed) dpitch = -rotSpeed;
        this.entity.setRotation(yaw + dyaw, pitch + dpitch);

        if(!this.agro) return;

        if(Math.abs(dyaw) < 45) {
            this.tryAttack();
        }

        double flySpeed = 0.3f;
        double dy = delta.getY();
        if(dy > flySpeed) dy = flySpeed;
        if(dy < -flySpeed) dy = -flySpeed;
        this.entity.teleport(this.entity.getLocation().clone().subtract(0, dy, 0));
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
                            loc.clone().add(0, 0.7, 0), AreaEffectCloud.class,
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

    @Override
    public void hitBy(EntityDamageByEntityEvent event, Player player) {
        this.agro = true;
        if(this.homeArea.contains(new Vec3i(player.getLocation()))) {
            this.target = player;
        }
    }

    @Override
    public void cleanup() {
        entity.remove();
    }

}
