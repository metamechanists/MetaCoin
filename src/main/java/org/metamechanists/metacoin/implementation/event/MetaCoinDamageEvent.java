package org.metamechanists.metacoin.implementation.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class MetaCoinDamageEvent extends EntityDamageByEntityEvent {
    public MetaCoinDamageEvent(@NotNull Entity damager, @NotNull Entity damagee, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damager, damagee, cause, damage);
    }
}
