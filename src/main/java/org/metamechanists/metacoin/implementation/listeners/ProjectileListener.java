package org.metamechanists.metacoin.implementation.listeners;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.Vector;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Utils;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectileListener implements Listener {
    private static final List<String> PLAYER_CAUSED_DEATH_MESSAGES = List.of(
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

    private static final List<String> BLOCK_CAUSED_DEATH_MESSAGES = List.of(
            "%s was consumed by the blockchain's insatiable hunger",
            "%s was crushed by the weight of a rogue MetaCoin™",
            "%s was swallowed by a MetaCoin™ mining sinkhole",
            "%s was vaporized by a MetaCoin™ Miner™ meltdown",
            "%s was lost in the infinite loop of a MetaCoin™ transaction",
            "%s was overwhelmed by the sheer magnitude of the MetaCoin™ blockchain",
            "%s was caught in the explosion of a MetaCoin™ Miner™",
            "%s was bankrupted by a sudden MetaCoin™ market crash",
            "%s was left in the dust of a MetaCoin™ mining operation",
            "%s was outpaced by the speed of a MetaCoin™ transaction",
            "%s was left in awe of the size of the MetaCoin™ blockchain",
            "%s was left penniless by a MetaCoin™ heist",
            "%s was left in ruins by a MetaCoin™ revolution",
            "%s was left speechless by the extravagance of a MetaCoin™",
            "%s was left in the shadow of a towering MetaCoin™ Miner™",
            "%s was left in the past by the future of MetaCoin™",
            "%s was left in the dark by the enlightenment of MetaCoin™",
            "%s was left in the cold by the warmth of MetaCoin™",
            "%s was left in the silence by the noise of MetaCoin™",
            "%s was left in the chaos by the order of MetaCoin™"
    );

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDispenserFire(BlockPreDispenseEvent event) {
        final ItemStack itemStack = event.getItemStack();
        if (!(SlimefunItem.getByItem(itemStack) instanceof MetaCoinItem)) {
            return;
        }

        final Block block = event.getBlock();
        final ItemStack snowballStack = itemStack.asQuantity(1);

        event.setCancelled(true);
        itemStack.subtract();

        if (block.getState() instanceof Dispenser dispenser && block.getBlockData() instanceof Directional directional) {
            final BlockFace blockFace = directional.getFacing();
            block.getWorld().spawn(block.getLocation().clone().add(0.5, 0.5, 0.5).add(blockFace.getDirection()), Snowball.class, snowball -> {
                snowball.setItem(snowballStack);
                snowball.setShooter(dispenser.getBlockProjectileSource());
                snowball.setVelocity(blockFace.getDirection().add(new Vector(0, 0.1, 0)).multiply(1.1)
                        .multiply(new Vector(RandomUtils.randomDouble(0.9, 1.1), RandomUtils.randomDouble(0.9, 1.1), RandomUtils.randomDouble(0.9, 1.1))));
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof ThrowableProjectile projectile)
                || !(SlimefunItem.getByItem(projectile.getItem()) instanceof MetaCoinItem coin)) {
            return;
        }

        if (PersistentDataAPI.hasBoolean(projectile, Keys.flippingCoin)) {
            if (projectile.getShooter() instanceof Player player) {
                MetaCoinItem.sendFlipResult(player);
                PersistentDataAPI.remove(player, Keys.flippingCoin);
            }
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
            final DamageSource.Builder source = DamageSource.builder(DamageType.MOB_PROJECTILE);
            source.withCausingEntity(projectile.getShooter() instanceof Entity shooter ? shooter : projectile);
            source.withDirectEntity(projectile);
            livingEntity.damage(coin.getDamage(), source.build());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getPlayer();
        if (player.getLastDamageCause() == null) {
            return;
        }

        final DamageSource damageSource = player.getLastDamageCause().getDamageSource();
        if (damageSource.getCausingEntity() instanceof Player damager
                && damageSource.getDirectEntity() instanceof Snowball snowball
                && SlimefunItem.getByItem(snowball.getItem()) instanceof MetaCoinItem) {
            event.setDeathMessage(RandomUtils.randomChoice(PLAYER_CAUSED_DEATH_MESSAGES).formatted(player.getName(), damager.getName()));
        } else if (damageSource.getCausingEntity() instanceof Snowball
                && damageSource.getDirectEntity() instanceof  Snowball snowball
                && SlimefunItem.getByItem(snowball.getItem()) instanceof MetaCoinItem) {
            event.setDeathMessage(RandomUtils.randomChoice(BLOCK_CAUSED_DEATH_MESSAGES).formatted(player.getName()));
        }
    }
}
