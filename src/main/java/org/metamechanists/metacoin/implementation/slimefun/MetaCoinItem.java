package org.metamechanists.metacoin.implementation.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metamechanists.metacoin.implementation.runnables.CoinFlipRunnable;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class MetaCoinItem extends SlimefunItem {
    private static final Map<Long, ItemStack> COINS = new LinkedHashMap<>();
    private static final List<String> FLIP_MESSAGES = List.of(
            "The MetaCoin™ flips through the air, glinting in the light. It lands... heads!",
            "You flip the MetaCoin™. It spins, a blur of gold and silver, and lands... tails!",
            "The MetaCoin™ flips end over end, landing with a soft clink. It's... heads!",
            "You give the MetaCoin™ a flick. It twirls in the air and lands... tails!",
            "The MetaCoin™ soars through the air, spinning wildly. It lands... heads!",
            "You toss the MetaCoin™ high. It descends slowly, finally landing... tails!",
            "The MetaCoin™ flips, catching the light. It lands... heads!",
            "You flip the MetaCoin™. It spins, a blur of gold and silver, and lands... tails!",
            "The MetaCoin™ flips end over end, landing with a soft clink. It's... heads!",
            "You give the MetaCoin™ a flick. It twirls in the air and lands... tails!",
            "The MetaCoin™ soars through the air, spinning wildly. It lands... heads!",
            "You toss the MetaCoin™ high. It descends slowly, finally landing... tails!"
    );
    private static final List<String> HINTS = List.of(
            "Theᵥ MetaCoin™ᵢ fliesₗ intoₗ theₐ airᵍ.... andₑ hitsᵣ you in the head.",
            "Theᵈ MetaCoin™ᵢ fliesₛ intoₚ theₑ airₙ.... andₛ hitsₑ youᵣ in the head.",
            "Theₚ MetaCoin™ᵢ fliesᵍ intoₗ theᵢ airₙ.... and hits you in the head."
    );
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

    public ItemUseHandler onUse() {
        return event -> {
            if (event.getClickedBlock().isPresent()) {
                return;
            }

            event.cancel();

            final Player player = event.getPlayer();
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.getItem().subtract();
            }

            // If Sneaking flip the coin
            if (player.isSneaking() && !CoinFlipRunnable.isFlipping(player)) {
                new CoinFlipRunnable(this, player, RandomUtils.randomChoice(RandomUtils.chance(5) ? HINTS : FLIP_MESSAGES));
                return;
            }

            // If not sneaking, throw the coin
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

    public static Map<Long, ItemStack> getCoins() {
        return COINS;
    }
}
