package org.metamechanists.metacoin.implementation;

import com.google.common.collect.Lists;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MetaCoinItem extends SlimefunItem {
    public static final Map<Long, ItemStack> COINS = new LinkedHashMap<>();
    private final long value;

    public MetaCoinItem(ItemGroup itemGroup, SlimefunItemStack item, long value) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[0]);

        this.value = value;
    }

    @Override
    public void register(@NotNull SlimefunAddon addon) {
        super.register(addon);
        setHidden(true);
    }

    public static int getValueFromProductionLevel(int productionLevel) {
        if (productionLevel < 65) {
            return productionLevel;
        }

        if (productionLevel < 129) {
            return 64 * productionLevel;
        }

        return 64 * 64 * productionLevel;
    }

    public static ItemStack fromProductionLevel(int productionLevel) {
        return withValue(getValueFromProductionLevel(productionLevel));
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
    public static Optional<Long> getClosestValue(long targetValue) {
        for (long value : getWeightedValues()) {
            if (value <= targetValue) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static long getTotalCoinValue(Player player) {
        long totalValue = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (SlimefunItem.getByItem(itemStack) instanceof MetaCoinItem metaCoinItem) {
                totalValue += metaCoinItem.value * itemStack.getAmount();
            }
        }
        return totalValue;
    }

    public static List<Long> getWeightedValues() {
        final List<Long> coins = new ArrayList<>(COINS.keySet());
        coins.sort(Comparator.comparingLong(Long::longValue));
        Lists.reverse(coins);
        return coins;
    }

    public static List<ItemStack> getWeightedCoins(Player player) {
        final List<ItemStack> coins = new ArrayList<>();
        for (long value : COINS.keySet()) {
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (SlimefunItem.getByItem(itemStack) instanceof MetaCoinItem coin && coin.value == value) {
                    coins.add(itemStack);
                }
            }
        }
        return coins;
    }

    public static long getRemovableCoinValue(Player player, long coins) {
        long removableCoins = 0;
        for (ItemStack itemStack : getWeightedCoins(player)) {
            if (!(SlimefunItem.getByItem(itemStack) instanceof MetaCoinItem metaCoinItem) || metaCoinItem.value > coins) {
                continue;
            }

            final int countToRemove = Math.min(itemStack.getAmount(), (int) (coins / metaCoinItem.value));
            coins -= countToRemove * metaCoinItem.value;
            removableCoins += countToRemove * metaCoinItem.value;

            if (coins <= 0) {
                break;
            }
        }
        return removableCoins;
    }

    public static void removeCoins(Player player, long coins) {
        for (ItemStack itemStack : getWeightedCoins(player)) {
            if (!(SlimefunItem.getByItem(itemStack) instanceof MetaCoinItem metaCoinItem) || metaCoinItem.value > coins) {
                continue;
            }

            final int countToRemove = Math.min(itemStack.getAmount(), (int) (coins / metaCoinItem.value));
            itemStack.subtract(countToRemove);
            coins -= countToRemove * metaCoinItem.value;

            if (coins <= 0) {
                break;
            }
        }
    }
}
