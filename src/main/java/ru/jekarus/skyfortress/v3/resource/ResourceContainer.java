package ru.jekarus.skyfortress.v3.resource;

import com.flowpowered.math.vector.Vector3d;
import lombok.Getter;
import lombok.val;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeamContainer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ResourceContainer {

    @Getter List<ItemResource> items = new ArrayList<>();

    @Getter List<BlockResource> blocks = new ArrayList<>();

    @Getter List<EntityResource> entities = new ArrayList<>();

    private Random random = new Random();

    public void tick() {
        tickItems();
        tickBlocks();
        tickEntities();
    }

    public void init(SkyFortressPlugin plugin) {

        final SfTeamContainer teamContainer = plugin.getTeamContainer();

        for (ItemResource resource : this.items) {
            this.initResource(resource, teamContainer);
        }

        for (BlockResource resource : this.blocks) {
            this.initResource(resource, teamContainer);
        }

        for (EntityResource resource : this.entities) {
            this.initResource(resource, teamContainer);
        }

    }

    private void initResource(SfResource resource, SfTeamContainer container) {
        final val teamId = resource.getTeamId();
        if (teamId != null) {
            Optional<SfGameTeam> optionalTeam = container.fromUniqueId(teamId);
            resource.setTeam(
                    optionalTeam.orElseThrow(() -> new UnsupportedOperationException(String.format("Team with id '%s' is not registered", teamId)))
            );
        }
    }

    private void tickItems() {
        final val iterator = this.items.iterator();
        while (iterator.hasNext()) {
            final val resource = iterator.next();
            final val resourceOptions = resource.getOptions();

            if (resourceOptions.isDisableIfTeamLost()) {
                final val team = resource.getTeam();
                if (!team.getCastle().isAlive()) {
                    resource.itemEntityReference.clear();
                    iterator.remove();
                    continue;
                }
            }

            resource.leftTicks --;
            if (resource.leftTicks < 1) {
                resource.leftTicks = resource.getSpawnDelay();
                boolean spawn = resourceOptions.isCanStack()
                        ? spawnItemCanStack(resource)
                        : spawnItemCantStack(resource);
                if (spawn) {
                    resource.itemEntityReference.clear();
                    Item item = (Item) resource.getLocation().createEntity(EntityTypes.ITEM);
                    final ItemStackSnapshot snapshot = resource.getItem().createSnapshot();
                    item.offer(Keys.REPRESENTED_ITEM, snapshot);
                    item.offer(Keys.VELOCITY, Vector3d.from(0, -1, 0));
                    if (resource.getLocation().spawnEntity(item)) {
                        resource.itemEntityReference = new WeakReference<>(item);
                    }
                }
            }
        }
    }

    private boolean spawnItemCanStack(ItemResource resource) {
        Item itemEntity = resource.itemEntityReference.get();
        if (itemEntity != null && !itemEntity.isRemoved()) {
            itemEntity.remove();
            itemEntity = null;
        }

        if (itemEntity == null) {
            return true;
        }

        final Optional<ItemStackSnapshot> optionalStack = itemEntity.get(Keys.REPRESENTED_ITEM);
        if (!optionalStack.isPresent()) {
            return true;
        }

        final ItemStackSnapshot stack = optionalStack.get();
        return stack.getQuantity() < stack.getType().getMaxStackQuantity();
    }

    private boolean spawnItemCantStack(ItemResource resource) {
        final Item itemEntity = resource.itemEntityReference.get();
        if (itemEntity != null) {
            if (itemEntity.isRemoved()) {
                return true;
            }

            final Optional<ItemStackSnapshot> optionalStack = itemEntity.get(Keys.REPRESENTED_ITEM);
            return !optionalStack.isPresent();
        }
        return true;
    }

    private void tickBlocks() {
        final val iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            final val resource = iterator.next();
            final val resourceOptions = resource.getOptions();

            if (!resource.isRunned()) {
                continue;
            }

            if (resourceOptions.isDisableIfTeamLost()) {
                final val team = resource.getTeam();
                if (!team.getCastle().isAlive()) {
                    iterator.remove();
                    continue;
                }
            }

            resource.leftTicks --;
            if (resource.leftTicks < 1) {
                resource.leftTicks = resource.getSpawnDelay();
                resource.setRunned(false);

                if (resourceOptions.isUseBottomBlock()) {
                    final val bottomPosition = resource.getLocation().add(0, -1, 0);
                    if (bottomPosition.getBlockType().equals(BlockTypes.AIR)) {
                        bottomPosition.setBlock(resource.getBottomBlock());
                    }
                }
                resource.getLocation().setBlock(resource.getBlock());
            }
        }
    }

    private void tickEntities() {

        final Iterator<EntityResource> iterator = this.entities.iterator();
        while (iterator.hasNext()) {
            final EntityResource resource = iterator.next();
            final EntityResource.Options options = resource.getOptions();

            if (!resource.isRunned()) {
                continue;
            }

            if (options.isDisableIfTeamLost()) {
                final SfGameTeam team = resource.getTeam();
                if (!team.getCastle().isAlive()) {
                    iterator.remove();
                    continue;
                }
            }

            resource.leftTicks --;
            if (resource.leftTicks < 1) {
                resource.leftTicks = resource.getSpawnDelay();

                final int squaredRange = options.getRange() * options.getRange();
                final Iterator<WeakReference<Entity>> entitiesIterator = resource.getEntities().iterator();
                while (entitiesIterator.hasNext()) {
                    final WeakReference<Entity> weakEntity = entitiesIterator.next();
                    final Entity entity = weakEntity.get();
                    if (entity == null || entity.isRemoved()) {
                        entitiesIterator.remove();
                        continue;
                    }

                    final Vector3d entityPosition = entity.getLocation().getPosition();
                    if (entityPosition.getY() <= 0) {
                        entity.remove();
                        entitiesIterator.remove();
                        continue;
                    }

                    if (options.isRemove()) {
                        final Vector3d resourcePosition = resource.getLocation().getPosition();

                        final double distanceSquared = entityPosition.distanceSquared(resourcePosition);
                        if (distanceSquared > squaredRange) {
                            entity.remove();
                            entitiesIterator.remove();
                            continue;
                        }
                    }
                }

                final int currentEntities = resource.getEntities().size();
                final int maxCountEntities = options.getCount();

                final int needSpawn = maxCountEntities - currentEntities;
                System.out.println("currEnt: " + currentEntities + " max: " + maxCountEntities + " needS: " + needSpawn);

                if (needSpawn > 0) {
                    final TeleportHelper teleportHelper = Sponge.getGame().getTeleportHelper();

                    Location<World> spawnLocation = getSpawnLocation(resource);
                    Optional<Location<World>> optionalSafeLocation = teleportHelper.getSafeLocation(spawnLocation);
                    if (optionalSafeLocation.isPresent()) {
                        final Location<World> safeLocation = optionalSafeLocation.get();
                        final Entity createdEntity = safeLocation.createEntity(resource.getEntityType());
                        createdEntity.tryOffer(Keys.GLOWING, true);
                        if (safeLocation.spawnEntity(createdEntity)) {
                            resource.getEntities().add(new WeakReference<>(createdEntity));
                            System.out.println("SPAWNED at loc: " + safeLocation.getPosition());
                        }
                        else {
                            System.out.println("Entity cannot be spawned");
                        }
                    }
                    else {
                        System.out.println("SAFE LOC NO PRESENT");
                    }

                }
            }
        }

    }

    private Location<World> getSpawnLocation(EntityResource resource) {
        final EntityResource.Options options = resource.getOptions();
        final int range = options.getRange();

        final Location<World> location = resource.getLocation();
        if (range < 2) {
            return location;
        }

        final Vector3d position = location.getPosition();
        final Vector3d angle = Vector3d.createRandomDirection(random);
        final double scale = random.nextDouble() * range;
        final Vector3d scaled = angle.mul(scale);
        final Vector3d randomPosition = position.add(scaled);

        return new Location<>(location.getExtent(), randomPosition);
    }

}
