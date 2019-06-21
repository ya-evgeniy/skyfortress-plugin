package ru.jekarus.skyfortress.v3.distribution.captain;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.SfLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class CaptainTarget implements Supplier<Optional<Entity>> {

    public SfPlayer player;
    public UUID entityUniqueId = null;

    public SfLocation cell;
    public List<SfLocation> changedBlocks = new ArrayList<>();

    public CaptainTarget(SfPlayer player, SfLocation cell, List<SfLocation> changedBlocks) {
        this.player = player;
        this.cell = cell;
        this.changedBlocks = changedBlocks;
    }

    public void onConnect(Player player) {
        cell.getLocation().getExtent().getEntity(entityUniqueId).ifPresent(entity -> {
            player.setLocationAndRotation(
                    entity.getLocation(),
                    entity.getRotation()
            );
            entity.remove();
        });
        this.entityUniqueId = null;
        SfTeam team = this.player.getTeam();
        team.addPlayer(SkyFortressPlugin.getInstance(), this.player);
    }

    public void onDisconnect(Player player) {
        setOffline(player.getLocation(), player.getRotation());
    }

    public boolean isOffline() {
        return this.entityUniqueId != null;
    }

    public void setOffline() {
        this.setOffline(cell.getLocation(), cell.getRotation());
    }

    public void setOffline(Location<World> location, Vector3d rotation) {
        World world = location.getExtent();

        world.loadChunk(location.getChunkPosition(), false);
        Entity village = location.createEntity(EntityTypes.VILLAGER);
        village.offer(Keys.AI_ENABLED, false);
        village.offer(Keys.IS_SILENT, true);
        village.offer(Keys.DISPLAY_NAME, Text.of(player.getName()));
        village.offer(Keys.CUSTOM_NAME_VISIBLE, true);
        village.setRotation(rotation);

        world.spawnEntity(village);

        this.entityUniqueId = village.getUniqueId();
    }

    public Optional<? extends Entity> getEntity() {
        Optional<Player> optionalPlayer = this.player.getPlayer();
        return optionalPlayer.isPresent() ? optionalPlayer : getOfflineEntity();
    }

    private Optional<Entity> getOfflineEntity() {
        return this
                .cell
                .getLocation()
                .getExtent()
                .getEntity(this.entityUniqueId);
    }

    public UUID getUniqueId() {
        if (entityUniqueId != null) {
            return entityUniqueId;
        }
        return player.getUniqueId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaptainTarget target = (CaptainTarget) o;
        return player.equals(target.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    public Optional<Entity> get() {
        return (Optional<Entity>) this.getEntity();
    }
}
