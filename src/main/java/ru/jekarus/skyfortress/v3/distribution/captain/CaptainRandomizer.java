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
import ru.jekarus.skyfortress.v3.lang.SfDistributionMessages;
import ru.jekarus.skyfortress.v3.lang.SfLanguage;
import ru.jekarus.skyfortress.v3.player.SfPlayer;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CaptainRandomizer {

    private final SkyFortressPlugin plugin;
    private final CaptainDistribution distribution;
    private final CaptainConfig config;

    private Map<Locale, ServerBossBar> localizedBar;
    private int currentValue;

    private Task task;

    public CaptainRandomizer(SkyFortressPlugin plugin, CaptainDistribution distribution, CaptainConfig config) {
        this.plugin = plugin;
        this.distribution = distribution;
        this.config = config;

        this.localizedBar = new HashMap<>();

        Collection<SfLanguage> langs = plugin.getLanguages().getLanguageByLocale().values();
        for (SfLanguage lang : langs) {
            ServerBossBar bar = ServerBossBar.builder()
                    .name(Text.of())
                    .color(BossBarColors.WHITE)
                    .playEndBossMusic(false)
                    .percent(1.0f)
                    .overlay(BossBarOverlays.PROGRESS)
                    .build();
            this.localizedBar.put(lang.locale, bar);
        }

    }

    public void reset() {
        SfDistributionMessages distribution = this.plugin.getMessages().getDistribution();
        Map<Locale, Text> localizedBarText = distribution.randomSelectedTime(config.maxSelectTime);
        for (Map.Entry<Locale, Text> entry : localizedBarText.entrySet()) {
            Locale locale = entry.getKey();
            Text text = entry.getValue();

            ServerBossBar bar = this.localizedBar.get(locale);
            if (bar != null) {
                bar.setName(text);
                bar.setPercent(1.0f);
            }
        }
        this.currentValue = config.maxSelectTime;

        start();
    }

    public void start() {
        if (task != null) return;
        task = Task.builder().name(this.getClass().getName()).interval(1, TimeUnit.SECONDS).execute(this::onSecond).submit(plugin);

        CaptainsState state = this.distribution.getState();
        for (CaptainTarget target : state.targetByPlayerUniqueId.values()) {
            target.player.getPlayer().ifPresent(player -> {
                showBarTo(player, target.player);
            });
        }
        for (Captain captain : state.captainByTeam.values()) {
            captain.player.getPlayer().ifPresent(player -> {
                showBarTo(player, captain.player);
            });
        }
    }

    public void stop() {
        if (task == null) return;
        for (ServerBossBar bar : this.localizedBar.values()) {
            bar.removePlayers(bar.getPlayers());
        }
        this.task.cancel();
        this.task = null;
    }

    public void showBarTo(Player player, SfPlayer sfPlayer) {
        ServerBossBar bar = this.localizedBar.get(sfPlayer.getLocale());
        if (bar != null) {
            bar.addPlayer(player);
        }
    }

    private void onSecond() {
        float percent = 1.0f / ((float) config.maxSelectTime / (float) currentValue);

        SfDistributionMessages distribution = this.plugin.getMessages().getDistribution();
        Map<Locale, Text> localizedBarText = distribution.randomSelectedTime(this.currentValue);

        for (Map.Entry<Locale, Text> entry : localizedBarText.entrySet()) {
            Locale locale = entry.getKey();
            Text text = entry.getValue();

            ServerBossBar bar = this.localizedBar.get(locale);
            if (bar != null) {
                bar.setName(text);
                bar.setPercent(percent);
            }
        }

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
