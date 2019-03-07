package ru.jekarus.skyfortress.v3.distribution.captain;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.player.SfPlayers;
import ru.jekarus.skyfortress.v3.scoreboard.SfScoreboard;
import ru.jekarus.skyfortress.v3.team.SfTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaptainSelection implements CaptainSelectedHandler {

    private final SkyFortressPlugin plugin;
    private final CaptainDistribution distribution;

    public ChoosingCaptain choosingCaptain;
    private Task highlightTask;

    public CaptainSelection(SkyFortressPlugin plugin, CaptainDistribution distribution) {
        this.plugin = plugin;
        this.distribution = distribution;

        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Override
    public void markTarget(ChoosingCaptain captain, CaptainTarget target) {
//        System.out.println("\n\nMARK\n\n");
        Optional<? extends Entity> optionalEntity = target.getEntity();
        if (!optionalEntity.isPresent()) {
            return;
        }
        Entity entity = optionalEntity.get();
        if (entity.getType().equals(EntityTypes.PLAYER)) {
            Player player = (Player) entity;
            markMember(captain.team, Text.of(player.getName()));
        }
        else {
            markMember(captain.team, Text.of(entity.getUniqueId()));
        }
    }

    public void markMember(SfTeam team, Text member) {
        for (SfScoreboard scoreboard : plugin.getScoreboards().asList()) {
            scoreboard.getTeams().getTeam(team).ifPresent(spongeTeam -> {
                spongeTeam.addMember(member);
            });
        }
    }

    @Override
    public void unmarkTarget(ChoosingCaptain captain, CaptainTarget target) {
//        System.out.println("\n\nUNMARK\n\n");
        Optional<? extends Entity> optionalEntity = target.getEntity();
        if (!optionalEntity.isPresent()) {
            return;
        }
        Entity entity = optionalEntity.get();
        if (entity.getType().equals(EntityTypes.PLAYER)) {
            Player player = (Player) entity;
            unmarkMember(captain.team, Text.of(player.getName()));
        }
        else {
            unmarkMember(captain.team, Text.of(entity.getUniqueId()));
        }
    }

    public void unmarkMember(SfTeam team, Text member) {
        for (SfScoreboard scoreboard : plugin.getScoreboards().asList()) {
            scoreboard.getTeams().getTeam(team).ifPresent(spongeTeam -> {
                spongeTeam.removeMember(member);
            });
        }
    }

    @Override
    public void selected(ChoosingCaptain captain, CaptainTarget target, boolean isRandom) {
        distribution.colorizeBlocks(target.changedBlocks, captain.team);
        if (isRandom) {
            Sponge.getServer().getBroadcastChannel().send(Text.of("Рандом выбрал " + target.player.getName()));
        }
        else {
            Sponge.getServer().getBroadcastChannel().send(Text.of(captain.captain.player.getName() + " выбрал " + target.player.getName()));
        }
        if (target != this.choosingCaptain.target && this.choosingCaptain.target != null) {
            this.unmarkTarget(this.choosingCaptain, this.choosingCaptain.target);
        }
        captain.team.addPlayer(SkyFortressPlugin.getInstance(), target.player);
        this.distribution.nextCaptain();
    }

    public void updateUnselected() {
        CaptainsState state = this.distribution.getState();
        List<CaptainTarget> unselectedPlayers = new ArrayList<>();
        for (CaptainTarget target : state.targetByPlayerUniqueId.values()) {
            SfTeam team = target.player.getTeam();
            if (team == null || team.getType() != SfTeam.Type.GAME) {
                unselectedPlayers.add(target);
                target.getEntity().ifPresent(entity -> {
                    entity.offer(Keys.GLOWING, true);
                });
            }
            else {
                target.getEntity().ifPresent(entity -> {
                    entity.offer(Keys.GLOWING, false);
                });
            }
        }
        state.unselectedTargets = unselectedPlayers;
    }

    @Listener
    public void onLeftClick(InteractBlockEvent event, @First Player player) {
        Optional<SfPlayer> optionalSfPlayer = SfPlayers.getInstance().getPlayer(player);
        if (optionalSfPlayer.isPresent()) {
            SfPlayer sfPlayer = optionalSfPlayer.get();
            if (choosingCaptain.captain.player == sfPlayer) {
                choosingCaptain.select(this);
            }
        }
    }

    private void onSelectionTick() {
        if (choosingCaptain == null) return;
        choosingCaptain.updateTarget(this, distribution.getState().unselectedTargets);
        choosingCaptain.showNickname();
    }

    public void start() {
        highlightTask = Task.builder()
                .name(this.getClass().getName())
                .intervalTicks(5)
                .execute(this::onSelectionTick)
                .submit(plugin);
    }

    public void stop() {
        if (highlightTask != null) {
            highlightTask.cancel();
        }
    }

    public void onEnd() {
        Sponge.getEventManager().unregisterListeners(this);
    }

}
