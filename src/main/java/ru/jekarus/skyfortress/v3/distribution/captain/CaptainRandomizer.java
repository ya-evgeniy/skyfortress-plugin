package ru.jekarus.skyfortress.v3.distribution.captain;

import org.spongepowered.api.boss.BossBar;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.jekarus.skyfortress.v3.SkyFortressPlugin;
import ru.jekarus.skyfortress.v3.distribution.captain.config.CaptainConfig;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CaptainRandomizer {

    private final SkyFortressPlugin plugin;
    private final CaptainDistribution distribution;
    private final CaptainConfig config;

    private ServerBossBar bar;
    private int currentValue;

    private Task task;

    public CaptainRandomizer(SkyFortressPlugin plugin, CaptainDistribution distribution, CaptainConfig config) {
        this.plugin = plugin;
        this.distribution = distribution;
        this.config = config;
        bar = ServerBossBar.builder()
                .name(Text.of())
                .color(BossBarColors.WHITE)
                .playEndBossMusic(false)
                .percent(1.0f)
                .overlay(BossBarOverlays.PROGRESS)
                .build();
    }

    public void reset() {
        this.bar.setName(Text.of(TextColors.GRAY, config.maxSelectTime));
        this.bar.setPercent(1.0f);
        this.currentValue = config.maxSelectTime;

        start();
    }

    public void start() {
        if (task != null) return;
        task = Task.builder().name(this.getClass().getName()).interval(1, TimeUnit.SECONDS).execute(this::onSecond).submit(plugin);

        CaptainsState state = this.distribution.getState();
        for (CaptainTarget target : state.targetByPlayerUniqueId.values()) {
            target.player.getPlayer().ifPresent(player -> {
                this.bar.addPlayer(player);
            });
        }
        for (Captain captain : state.captainByTeam.values()) {
            captain.player.getPlayer().ifPresent(this::showBarTo);
        }
    }

    public void stop() {
        if (task == null) return;
        this.bar.removePlayers(this.bar.getPlayers());
        this.task.cancel();
        this.task = null;
    }

    public void showBarTo(Player player) {
        this.bar.addPlayer(player);
    }

    private void onSecond() {
        float percent = 1.0f / ((float) config.maxSelectTime / (float) currentValue);
        this.bar.setName(Text.of(TextColors.GRAY, this.currentValue));
        bar.setPercent(percent);
        if (this.currentValue < 1) {
            randomSelect();
        }
        else {
            this.currentValue --;
        }
    }

    private void randomSelect() {
        CaptainsState state = this.distribution.getState();
        List<CaptainTarget> unselectedTargets = state.unselectedTargets;

        int next = new Random().nextInt(unselectedTargets.size());
        CaptainTarget target = unselectedTargets.get(next);

        CaptainSelection selection = this.distribution.getSelection();
        ChoosingCaptain choosingCaptain = selection.choosingCaptain;

        selection.markTarget(choosingCaptain, target);
        selection.selected(choosingCaptain, target, true);
    }

}
