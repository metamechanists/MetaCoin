package org.metamechanists.metacoin.implementation.slimefun;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Language;

@Getter
public enum Upgrades {
    SPEED(64),
    PRODUCTION(192) {
        @Override
        public long getCost(int currentLevel) {
            if (currentLevel < 65) {
                return (long) Math.ceil(Math.pow(2, 0.2 * currentLevel) - 1 / (double) currentLevel);
            }

            if (currentLevel < 129) {
                return (long) Math.ceil(Math.pow(64, 1 + (1.5 * currentLevel) / 64));
            }

            return (long) Math.ceil(Math.pow(64 * 64, currentLevel / 60D));
        }
    },
    RELIABILITY(256);

    private final int maxLevel;

    Upgrades(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public ItemStack getDisplay(Location miner) {
        return switch (this) {
            case SPEED -> ItemStacks.speedUpgrade(getCost(miner), getLevel(miner), getMaxLevel());
            case PRODUCTION -> ItemStacks.productionUpgrade(getCost(miner), getLevel(miner), getMaxLevel());
            case RELIABILITY -> ItemStacks.reliabilityUpgrade(getCost(miner), getLevel(miner), getMaxLevel());
        };
    }

    public ChestMenu.MenuClickHandler getClickHandler(ChestMenu menu, Location miner) {
        return (player, i, o2, o3) -> {
            final int level = getLevel(miner);
            if (level >= getMaxLevel()) {
                Language.sendMessage(player, "miner.upgrade.max-level");
                return false;
            }

            final long cost = getCost(miner);
            final long playerValue = MetaCoinItem.getTotalCoinValue(player);
            if (playerValue < cost) {
                Language.sendFormatted(player, "miner.upgrade.too-expensive", playerValue, cost);
                return false;
            }

            final long removableValue = MetaCoinItem.getRemovableCoinValue(player, cost);
            if (removableValue < cost) {
                Language.sendFormatted(player, "miner.upgrade.cant-remove", removableValue, cost);
                return false;
            }

            MetaCoinItem.removeCoins(player, cost);
            setLevel(miner, level + 1);
            menu.replaceExistingItem(i, getDisplay(miner));
            return false;
        };
    }

    public long getCost(Location miner) {
        return getCost(getLevel(miner));
    }

    public long getCost(int currentLevel) {
        return currentLevel;
    }

    public int getLevel(Location miner) {
        try {
            return Integer.parseInt(BlockStorage.getLocationInfo(miner, name()));
        } catch (Exception ignored) {
            BlockStorage.addBlockInfo(miner, name(), "1");
            return 1;
        }
    }

    public void setLevel(Location miner, int level) {
        BlockStorage.addBlockInfo(miner, name(), String.valueOf(level));
    }

    public static void setLevels(Location miner, ItemStack itemStack) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        BlockStorage.addBlockInfo(miner, "SPEED", String.valueOf(PersistentDataAPI.getInt(itemMeta, Keys.speedLevel)));
        BlockStorage.addBlockInfo(miner, "PRODUCTION", String.valueOf(PersistentDataAPI.getInt(itemMeta, Keys.productionLevel)));
        BlockStorage.addBlockInfo(miner, "RELIABILITY", String.valueOf(PersistentDataAPI.getInt(itemMeta, Keys.reliabilityLevel)));
    }

    public static int[] getLevels(Location miner) {
        return new int[]{SPEED.getLevel(miner), PRODUCTION.getLevel(miner), RELIABILITY.getLevel(miner)};
    }
}
