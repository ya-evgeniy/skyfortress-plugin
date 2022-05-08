package ru.jekarus.skyfortress.module.fight;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class FightRecord {

    private final Deque<DamageRecord> records = new LinkedList<>();
    private long lastDamageTime = System.currentTimeMillis();

    public boolean isActual() {
        return lastDamageTime + TimeUnit.SECONDS.toMillis(10) >= System.currentTimeMillis();
    }

    public void recordDamage(DamageRecord record) {
        this.lastDamageTime = System.currentTimeMillis();
        this.records.addLast(record);
    }

    public DamageRecord getKillerRecord() {
        DamageRecord lastEntityKillerRecord = null;

        final var iterator = records.descendingIterator();
        while (iterator.hasNext()) {
            final var damageRecord = iterator.next();
            if (damageRecord.damagerType() == EntityType.PLAYER) {
                return damageRecord;
            }

            if (lastEntityKillerRecord == null) {
                lastEntityKillerRecord = damageRecord;
            }
        }

        return lastEntityKillerRecord;
    }

    public Stream<OfflinePlayer> assistants() {
        DamageRecord killerLastRecord = null;
        final var assistants = new HashSet<UUID>();

        final var iterator = records.descendingIterator();
        while (iterator.hasNext()) {
            final var damageRecord = iterator.next();
            if (killerLastRecord == null && damageRecord.damagerType() == EntityType.PLAYER) {
                killerLastRecord = damageRecord;
                continue;
            }

            if (damageRecord.damagerType() == EntityType.PLAYER &&
                    !damageRecord.damagerUuid().equals(killerLastRecord.damagerUuid())) {
                assistants.add(damageRecord.damagerUuid());
            }
        }

        return assistants.stream()
                .map(Bukkit::getOfflinePlayer);
    }

}
