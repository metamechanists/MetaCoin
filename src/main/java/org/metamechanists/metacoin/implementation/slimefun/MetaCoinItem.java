package org.metamechanists.metacoin.implementation.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.metamechanists.metacoin.utils.Language;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MetaCoinItem extends SlimefunItem {
    public static final Map<Long, ItemStack> COINS = new LinkedHashMap<>();
    private final int damage;
    private final long value;

    public MetaCoinItem(ItemGroup itemGroup, SlimefunItemStack item, int damage, long value) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[0]);

        this.damage = damage;
        this.value = value;
        addItemHandler(onUse());
    }

    @Override
    public void postRegister() {
        setHidden(true);
    }

    public int getDamage() {
        return this.damage;
    }

    public long getValue() {
        return this.value;
    }

    public ItemUseHandler onUse() {
        return event -> {
            final Player player = event.getPlayer();
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.getItem().subtract();
            }

            player.launchProjectile(Snowball.class, player.getEyeLocation().getDirection().multiply(2), projectile -> {
                projectile.setItem(getItem());
                projectile.setShooter(player);
            });
        };
    }

    public static long valueOf(ItemStack itemStack) {
        return valueOf(itemStack, true);
    }

    public static long valueOf(ItemStack itemStack, boolean includeAmount) {
        if (SlimefunItem.getByItem(itemStack) instanceof MetaCoinItem item) {
            return item.value * (includeAmount ? itemStack.getAmount() : 1);
        }
        return 0;
    }

    public static long valueFromProductionLevel(int productionLevel) {
        if (productionLevel < 65) {
            return productionLevel;
        }

        if (productionLevel < 129) {
            return 64L * (productionLevel - 63);
        }

        return 64L * 64 * (productionLevel - 127);
    }

    public static ItemStack withValue(long value) {
        final Optional<Long> closestValue = getClosestValue(value);
        if (closestValue.isEmpty()) {
            return new ItemStack(Material.AIR);
        }

        final ItemStack result = new CustomItemStack(COINS.get(closestValue.get()), (int) Math.min(64, value / closestValue.get()));
        final ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(66613);
        result.setItemMeta(meta);
        return result;
    }
    public static ItemStack[] withTotalValue(long totalValue) {
        final List<ItemStack> coins = new ArrayList<>();
        while (totalValue > 0) {
            final ItemStack nextStack = withValue(totalValue);
            if (nextStack.isEmpty()) {
                break;
            }

            coins.add(nextStack);
            totalValue -= valueOf(nextStack);
        }
        return coins.toArray(ItemStack[]::new);
    }
    public static Optional<Long> getClosestValue(long targetValue) {
        for (long value : COINS.keySet()) {
            if (value <= targetValue) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static long getTotalCoinValue(Player player) {
        long totalValue = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            totalValue += valueOf(itemStack);
        }
        return totalValue;
    }

    public static List<ItemStack> getWeightedCoins(Player player) {
        final List<ItemStack> coins = new ArrayList<>();
        for (long value : COINS.keySet()) {
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (valueOf(itemStack, false) == value) {
                    coins.add(itemStack);
                }
            }
        }
        return coins;
    }

    public static long getRemovableCoinValue(Player player, long coins) {
        long removableCoins = 0;
        for (ItemStack itemStack : getWeightedCoins(player)) {
            final long value = valueOf(itemStack, false);
            if (value <= 0 || value > coins) {
                continue;
            }

            final int countToRemove = Math.min(itemStack.getAmount(), (int) (coins / value));
            coins -= countToRemove * value;
            removableCoins += countToRemove * value;

            if (coins <= 0) {
                break;
            }
        }
        return removableCoins;
    }

    public static void removeCoins(Player player, long coins) {
        for (ItemStack itemStack : getWeightedCoins(player)) {
            final long value = valueOf(itemStack, false);
            if (value <= 0 || value > coins) {
                continue;
            }

            final int countToRemove = Math.min(itemStack.getAmount(), (int) (coins / value));
            itemStack.subtract(countToRemove);
            coins -= countToRemove * value;

            if (coins <= 0) {
                break;
            }
        }
    }
}
