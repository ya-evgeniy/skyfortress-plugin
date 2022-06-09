package ru.jekarus.skyfortress.module.object;

import fr.skytasul.guardianbeam.Laser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import ru.jekarus.skyfortress.Vec3i;
import ru.jekarus.skyfortress.config.SfConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SfWither extends SfObjectModule.SfEntity<Wither> {

    public enum State {
        WANDERING,
        GET_ON_POSITION,
        ATTACK_TARGET;
    }

    private final Plugin plugin;
    private final List<Laser> lasers = new ArrayList<>();
    private State state = State.WANDERING;
    public Player target;
    private long lastAttackTick = Bukkit.getCurrentTick();
    private final Location[] wanderingCircle = new Location[12];
    private Location moveLocation;
    private Runnable onMoveDone;
    private int wanderingIdx = -1;
    public boolean agro = false;

    public SfWither(Plugin plugin, Wither entity, Location location) {
        super(entity, location);
        this.plugin = plugin;
        entity.setSilent(true);
        entity.getPathfinder().stopPathfinding();
        Bukkit.getMobGoals().removeAllGoals(entity);
        this.entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        this.entity.getBossBar().setVisible(false);
        // make not falling
        WitherNms.setGravity(entity, false);
        // make pathfinding though blocks
        WitherNms.setBlockMalus(entity, 0f);
        WitherNms.setPhysics(entity, false);

        int sector = 360 / wanderingCircle.length;
        for (int i = 0; i < wanderingCircle.length; i++) {
            final var start = this.location.clone();
            start.setYaw(start.getYaw() + sector * i);
            final var point = start.add(start.getDirection().multiply(7));
            point.setYaw(point.getYaw() + 90);
            wanderingCircle[i] = point;
        }

        actState();
    }

    public void setState(State state) {
        if (state == this.state) return;
//        System.out.println(this.state + " -> " + state);
        this.state = state;
        actState();
    }

    public void moveTo(Location location, Runnable onDone) {
        this.onMoveDone = onDone;
        this.moveLocation = location;
//        this.entity.getPathfinder().moveTo(this.moveLocation);
        WitherNms.followPath(this.entity, Arrays.asList(
                new Vec3i(this.entity.getLocation()),
                new Vec3i(this.moveLocation)
        ), 1.3);
    }

    @Override
    public void tickState() {
        if(this.state == SfWither.State.GET_ON_POSITION) return;
        final var players = this.location.getNearbyEntitiesByType(Player.class, SfConfig.OBJECT_HOME_RADIUS);
        if(players.size() > 0) {
            if(this.state == SfWither.State.ATTACK_TARGET) return;
            this.target = players.iterator().next();
            this.setState(SfWither.State.GET_ON_POSITION);
        } else {
            this.target = null;
            this.agro = false;
            for (Laser laser : this.lasers) {
                laser.stop();
            }
            this.lasers.clear();
            this.setState(SfWither.State.WANDERING);
        }
    }

    @Override
    public void tickMoving() {
//        if(this.moveLocation == null || this.entity.getLocation().distanceSquared(this.moveLocation) < 1.75) {
        if(this.moveLocation != null && WitherNms.isDoneMoving(this.entity)) {
            this.moveLocation = null;
            if(onMoveDone != null) onMoveDone.run();
        }

        if(this.state != State.ATTACK_TARGET) return;
        var loc = this.entity.getLocation();
        final var yaw = loc.getYaw();  // [-180:180]
        final var delta = this.target.getLocation().subtract(loc).add(0, 0.5, 0).toVector();
        loc.setDirection(delta);
        var dyaw = Location.normalizeYaw(loc.getYaw()) - yaw;
        if(dyaw > 180) dyaw = 360 - dyaw;
        if(dyaw < -180) dyaw = 360 + dyaw;
        this.entity.lookAt(this.target);
        if(!this.agro) return;

        // /execute at @e[type=minecraft:wither] run tp 7d888a49-744f-4011-b51d-1642074aec2b ~ ~ ~ 50 0
//        System.out.println("dst:%.2f srd:%.2f delta:%.2f res:%.2f".formatted(Location.normalizeYaw(loc.getYaw()), yaw, dyaw, Location.normalizeYaw(this.entity.getLocation().getYaw())));
        if(Math.abs(dyaw) < 45) {
            this.tryAttack();
        }

        double flySpeed = 0.3f;
        double dy = delta.getY();
        if(Math.abs(dy) >= 0.01) {
            if(dy > flySpeed) dy = flySpeed;
            if(dy < -flySpeed) dy = -flySpeed;
            this.entity.teleport(this.entity.getLocation().clone().add(0, dy, 0));
            try {
                for (Laser laser : lasers) {
                    laser.moveStart(this.entity.getLocation());
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void tryAttack() {
        final var currentTick = Bukkit.getCurrentTick();
        if(currentTick > (lastAttackTick + 20 * 7)) {
            lastAttackTick = currentTick;
            try {
                final var laser = new Laser.GuardianLaser(this.entity.getEyeLocation(), this.target, 3, 128);
                laser.start(plugin);
                lasers.add(laser);
                laser.executeEnd(() -> {
                    if(target != null) target.damage(3.0);
                    lasers.remove(laser);
                });
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
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
        target = null;
        for (Laser laser : new ArrayList<>(lasers)) {
            laser.stop();
        }
        lasers.clear();
        entity.remove();
    }

    public void wanderingStep() {
        wanderingIdx++;
        if(wanderingIdx >= wanderingCircle.length) wanderingIdx = 0;
        Location next = wanderingCircle[wanderingIdx];
        moveTo(next, this::wanderingStep);
    }
    private void actState() {
        switch (state) {
            case WANDERING -> {
                wanderingIdx = -1;
                wanderingStep();
            }
            case GET_ON_POSITION -> {
                wanderingIdx = -1;
                moveTo(location, () -> {
                    if(this.target!= null) {
                        setState(State.ATTACK_TARGET);
                    } else {
                        setState(State.WANDERING);
                    }
                });
            }
            case ATTACK_TARGET -> {
                if(this.target!= null) this.entity.lookAt(this.target);
//                entity.setPhase(EnderDragon.Phase.HOVER);
            }
        }
    }

}
