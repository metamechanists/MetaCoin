package org.metamechanists.metacoin.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;
import org.metamechanists.metacoin.implementation.event.MetaCoinDamageEvent;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;
import org.metamechanists.metacoin.utils.Utils;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectileListener implements Listener {
    private static final List<String> DEATH_MESSAGES = List.of(
            "%s was annihilated by the volatile fluctuations of %s's MetaCoins™",
            "%s was crushed under the weight of %s's blockchain",
            "%s was bankrupted by %s's MetaCoin™ empire",
            "%s was overwhelmed by the complexity of %s's blockchain",
            "%s was lost in the labyrinth of %s's MetaCoin™ transactions",
            "%s was caught in the crossfire of %s's MetaCoin™ mining operation",
            "%s was obliterated by the sheer power of %s's MetaCoin™ Miner™",
            "%s was caught off guard by %s's sudden MetaCoin™ wealth",
            "%s was left in the dust of %s's MetaCoin™ mining speed",
            "%s was outpaced by %s's MetaCoin™ production level",
            "%s was left in awe of %s's MetaCoin™ reliability level",
            "%s was left penniless by %s's MetaCoin™ fortune",
            "%s was left in ruins by %s's MetaCoin™ revolution",
            "%s was left speechless by %s's MetaCoin™ extravagance",
            "%s was left in the shadow of %s's MetaCoin™ empire",
            "%s was left in the past by %s's MetaCoin™ future",
            "%s was left in the dark by %s's MetaCoin™ enlightenment",
            "%s was left in the cold by %s's MetaCoin™ warmth",
            "%s was left in the silence by %s's MetaCoin™ noise",
            "%s was left in the chaos by %s's MetaCoin™ order"
    );

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
            final MetaCoinDamageEvent damageEvent = new MetaCoinDamageEvent((Entity) projectile.getShooter(), livingEntity, EntityDamageEvent.DamageCause.PROJECTILE, coin.getDamage());
            Bukkit.getPluginManager().callEvent(damageEvent);
            if (!damageEvent.isCancelled()) {
                livingEntity.setLastDamage(damageEvent.getDamage());
                livingEntity.setLastDamageCause(damageEvent);
                livingEntity.damage(damageEvent.getDamage());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getPlayer();
        if (player.getLastDamageCause() instanceof MetaCoinDamageEvent damageEvent) {
            event.setDeathMessage(RandomUtils.randomChoice(DEATH_MESSAGES).formatted(player.getName(), damageEvent.getDamager().getName()));
        }
    }
}
