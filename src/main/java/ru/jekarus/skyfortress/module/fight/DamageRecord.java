package ru.jekarus.skyfortress.module.fight;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public record DamageRecord(long recordTime,
                           EntityType damagerType, UUID damagerUuid,
                           EntityDamageEvent.DamageCause cause, EntityType causeEntityType,
                           double value) {

    public Entity getEntity() {
        return Bukkit.getEntity(this.damagerUuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.damagerUuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.damagerUuid);
    }

}
