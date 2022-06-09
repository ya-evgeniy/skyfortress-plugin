package ru.jekarus.skyfortress.module.object;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class ObjectHealth {

    private final LivingEntity obj;
    private final int width = 30;

    public ObjectHealth(LivingEntity obj) {
        this.obj = obj;
        this.obj.setCustomNameVisible(true);
        this.update();
    }

    public void update() {
        final var maxHealth = this.obj.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        final var health = this.obj.getHealth();
        int lcount = (int) (width * health / maxHealth);
        int rcount = width - lcount;
        final var sb = new StringBuilder();
        sb.append(ChatColor.WHITE);
        sb.append('[');
        sb.append(ChatColor.GREEN);
        for (int i = 0; i < lcount; i++) sb.append('■');
        sb.append(ChatColor.GREEN);
        for (int i = 0; i < rcount; i++) sb.append(' ');
        sb.append(ChatColor.WHITE);
        sb.append(']');
        sb.append(' ');
        sb.append(ChatColor.WHITE);
        sb.append('[');
        sb.append(ChatColor.YELLOW);
        sb.append((int) health);
        sb.append(ChatColor.WHITE);
        sb.append('/');
        sb.append(ChatColor.YELLOW);
        sb.append((int) maxHealth);
        sb.append(ChatColor.WHITE);
        sb.append(']');
        this.obj.customName(Component.text(sb.toString()));
    }

}
