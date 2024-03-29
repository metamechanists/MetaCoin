package org.metamechanists.metacoin.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.util.Vector;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;

public class CrossbowListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCrossbowFired(EntityShootBowEvent event) {
        if (event.getBow() == null || !(event.getBow().getItemMeta() instanceof CrossbowMeta)) {
            return;
        }

        if (!(SlimefunItem.getByItem(event.getConsumable()) instanceof MetaCoinItem coin)) {
            return;
        }

        final Entity eventProjectile = event.getProjectile();
        final Vector velocity = eventProjectile.getVelocity();
        event.getEntity().launchProjectile(ThrowableProjectile.class, velocity, projectile -> {
            projectile.setItem(coin.getItem());
            projectile.setShooter(event.getEntity());
            eventProjectile.remove();
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof ThrowableProjectile projectile)
                || !(SlimefunItem.getByItem(projectile.getItem()) instanceof MetaCoinItem coin)) {
            return;
        }

        final Entity entity = event.getHitEntity();
        if (entity == null) {
            return;
        }

        if (entity instanceof Merchant merchant) {
            if (coin.getValue() < 64 || merchant.getRecipeCount() < 1) {
                return;
            }

            final MerchantRecipe recipe = RandomUtils.randomChoice(merchant.getRecipes());
            entity.getWorld().dropItemNaturally(entity.getLocation(), recipe.getResult());
        } else if (entity instanceof Piglin piglin) {
            if (coin.getValue() < 64 || piglin.getBarterList().isEmpty()) {
                return;
            }

            final Material drop = RandomUtils.randomChoice(new ArrayList<>(piglin.getBarterList()));
            entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(drop));
        } else if (entity instanceof LivingEntity livingEntity) {
            livingEntity.damage(coin.getDamage());
        }

        entity.remove();
    }
}
