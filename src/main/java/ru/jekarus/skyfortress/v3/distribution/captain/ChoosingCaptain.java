package ru.jekarus.skyfortress.v3.distribution.captain;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import ru.jekarus.skyfortress.v3.team.SfGameTeam;
import ru.jekarus.skyfortress.v3.team.SfTeam;
import ru.jekarus.skyfortress.v3.utils.TargetUtils;

import java.util.List;
import java.util.Optional;

public class ChoosingCaptain {

    public Captain captain;
    public SfGameTeam team;

    public CaptainTarget target;

    public ChoosingCaptain(Captain captain, SfGameTeam team) {
        this.captain = captain;
        this.team = team;
    }

    public void select(CaptainSelectedHandler handler) {
        if (target == null) {

            return;
        }
        handler.selected(this, this.target, false);
    }

    public void updateTarget(CaptainSelectedHandler handler, List<CaptainTarget> targets) {
        Optional<? extends Entity> optionalCaptainEntity = captain.getEntity();
        if (optionalCaptainEntity.isPresent()) {
//            System.out.println("CAPTAIN PRESENT");
            Entity captainEntity = optionalCaptainEntity.get();
            Optional<CaptainTarget> optionalTarget = TargetUtils.getCustomTargetEntity(captainEntity, 1.5, targets);
            if (optionalTarget.isPresent()) {
//                System.out.println("TARGET PRESENT");
                CaptainTarget newTarget = optionalTarget.get();
                if (this.target != null) {
                    if (this.target != newTarget) {
                        handler.unmarkTarget(this, this.target);
                        this.target = newTarget;
                        handler.markTarget(this, this.target);
                    }
                }
                else {
                    this.target = newTarget;
                    handler.markTarget(this, this.target);
                }
            }
            else {
//                System.out.println("TARGET NOT PRESENT");
                if (this.target != null) {
                    handler.unmarkTarget(this, this.target);
                    this.target = null;
                }
            }
        }
    }

    public void showNickname() {
        captain.player.getPlayer().ifPresent(player -> {
            if (target != null) {
                Title title = Title.builder()
                        .fadeIn(0)
                        .stay(20)
                        .fadeOut(10)
                        .title(Text.EMPTY)
                        .subtitle(Text.of(target.player.getName()))
                        .build();
                player.sendTitle(title);
            }
        });
    }

}
