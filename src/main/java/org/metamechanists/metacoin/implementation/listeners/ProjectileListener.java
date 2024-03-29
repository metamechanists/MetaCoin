package org.metamechanists.metacoin.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;
import org.metamechanists.metacoin.utils.Utils;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectileListener implements Listener {
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
            return;
        } else if (entity instanceof Piglin piglin) {
            if (coin.getValue() < 64) {
                return;
            }

            final Collection<ItemStack> drops = LootTables.PIGLIN_BARTERING.getLootTable().populateLoot(Utils.RANDOM, new LootContext.Builder(entity.getLocation()).lootedEntity(piglin).build());
            final ItemStack drop = RandomUtils.randomChoice(new ArrayList<>(drops));
            entity.getWorld().dropItemNaturally(entity.getLocation(), drop);
            return;
        }

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.damage(coin.getDamage(), (Entity) projectile.getShooter());
        }

        entity.remove();
    }
}
