package org.metamechanists.metacoin.core;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Utils;
import org.metamechanists.metalib.utils.ColorUtils;

public class ItemStacks {
    // GUI ITEMS
    // ALL PAGES
    public static final ItemStack SPEED_DISPLAY = new SlimefunItemStack(
            "_UI_MM_SPEED_DISPLAY",
            Material.YELLOW_STAINED_GLASS_PANE,
            " "
    );
    public static final ItemStack PRODUCTION_DISPLAY = new SlimefunItemStack(
            "_UI_MM_PRODUCTION_DISPLAY",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            " "
    );
    public static final ItemStack RELIABILITY_DISPLAY = new SlimefunItemStack(
            "_UI_MM_RELIABILITY_DISPLAY",
            Material.MAGENTA_STAINED_GLASS_PANE,
            " "
    );
    public static final ItemStack PAGE_BACK_ENABLED = new SlimefunItemStack(
            "_UI_MM_PAGE_BACK_ENABLED",
            Material.LIME_STAINED_GLASS_PANE,
            "&f⇦ Previous Page",
            "",
            "&7(%current_page% / %page_count%)"
    );
    public static final ItemStack PAGE_BACK_DISABLED= new SlimefunItemStack(
            "_UI_MM_PAGE_BACK_DISABLED",
            Material.BLACK_STAINED_GLASS_PANE,
            "&8⇦ Previous Page",
            "",
            "&7(%current_page% / %page_count%)"
    );
    public static final ItemStack PAGE_FORWARD_ENABLED = new SlimefunItemStack(
            "_UI_MM_PAGE_FORWARD_ENABLED",
            Material.LIME_STAINED_GLASS_PANE,
            "&fNext Page ⇨",
            "",
            "&7(%current_page% / %page_count%)"
    );
    public static final ItemStack PAGE_FORWARD_DISABLED = new SlimefunItemStack(
            "_UI_MM_PAGE_FORWARD_DISABLED",
            Material.BLACK_STAINED_GLASS_PANE,
            "&8Next Page ⇨",
            "",
            "&7(%current_page% / %page_count%)"
    );
    // MINER PAGE
    public static final ItemStack MINER_PROGRESS_FALSE = new SlimefunItemStack(
            "_UI_MM_MINER_PROGRESS_FALSE",
            Material.RED_STAINED_GLASS_PANE,
            " "
    );
    public static final ItemStack MINER_PROGRESS_TRUE = new SlimefunItemStack(
            "_UI_MM_MINER_PROGRESS_TRUE",
            Material.LIME_STAINED_GLASS_PANE,
            " "
    );
    // UPGRADES PAGE
    public static final ItemStack SPEED_UPGRADE = new SlimefunItemStack(
            "_UI_MM_SPEED_UPGRADE",
            Material.YELLOW_CONCRETE,
            "&eSpeed Upgrade",
            "&8⇨ &aIncreases &7the rate of production",
            "&8⇨ &cDecreases &7machine reliability",
            "",
            "&8⇨ &eCost: &7%upgrade_cost%\uE803",
            "&8⇨ &eUpgrade Level: &7%upgrade_level%/%max_upgrade_level%"
    );
    public static final ItemStack PRODUCTION_UPGRADE = new SlimefunItemStack(
            "_UI_MM_PRODUCTION_UPGRADE",
            Material.LIGHT_BLUE_CONCRETE,
            "&bProduction Upgrade",
            "&8⇨ &aIncreases &7the production amount",
            "&8⇨ &cDecreases &7machine reliability",
            "",
            "&8⇨ &eCost: &7%upgrade_cost%&f\uE803",
            "&8⇨ &eUpgrade Level: &7%upgrade_level%/%max_upgrade_level%"
    );
    public static final ItemStack RELIABILITY_UPGRADE = new SlimefunItemStack(
            "_UI_MM_RELIABILITY_UPGRADE",
            Material.MAGENTA_CONCRETE,
            "&dReliability Upgrade",
            "&8⇨ &aIncreases &7machine reliability",
            "",
            "&8⇨ &eCost: &7%upgrade_cost%&f\uE803",
            "&8⇨ &eUpgrade Level: &7%upgrade_level%/%max_upgrade_level%"
    );
    // PANEL PAGE
    public static final ItemStack CORE_OFFLINE = new SlimefunItemStack(
            "_UI_MM_CORE_DISABLED",
            Material.RED_STAINED_GLASS_PANE,
            "%color%%type% &8Core &f#%number%",
            "&8⇨ %color%Click &7to toggle",
            "",
            "&8⇨ %color%Status: &cOffline"
    );
    public static final ItemStack CORE_RUNNING = new SlimefunItemStack(
            "_UI_MM_CORE_ENABLED",
            Material.LIME_STAINED_GLASS_PANE,
            "%color%%type% &8Core &f#%number%",
            "&8⇨ %color%Click &7to toggle",
            "",
            "&8⇨ %color%Status: &aOnline"
    );

    // SLIMEFUN ITEMS
    public static final SlimefunItemStack META_COIN_MINER = new SlimefunItemStack(
            "META_COIN_MINER",
            Material.BLAST_FURNACE,
            ColorUtils.MM_YELLOW + "MetaCoin™ Miner™",
            "&7>tba<"
    );
    public static final SlimefunItemStack META_COIN = new SlimefunItemStack(
            "META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Can be placed, thrown, compressed",
            "&7or used to upgrade the MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "1&f\uE803"
    );
    public static final SlimefunItemStack COMPRESSED_META_COIN = new SlimefunItemStack(
            "COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Can be placed, thrown, compressed",
            "&7or used to upgrade the MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "64&f\uE803"
    );
    public static final SlimefunItemStack DOUBLE_COMPRESSED_META_COIN = new SlimefunItemStack(
            "DOUBLE_COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "2x Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Can be placed, thrown, compressed",
            "&7or used to upgrade the MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "4,096&f\uE803"
    );
    public static final SlimefunItemStack TRIPLE_COMPRESSED_META_COIN = new SlimefunItemStack(
            "TRIPLE_COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "3x Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Can be placed, thrown, compressed",
            "&7or used to upgrade the MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "262,144&f\uE803"
    );

    // FORMAT METHODS
    // ALL PAGES
    public static ItemStack pageBack(int currentPage, int pageCount) {
        return Utils.format(currentPage > 1
                ? PAGE_BACK_ENABLED
                : PAGE_BACK_DISABLED,
                "current_page", currentPage,
                "page_count", pageCount
        );
    }
    public static ItemStack pageForward(int currentPage, int pageCount) {
        return Utils.format(currentPage < pageCount
                        ? PAGE_FORWARD_ENABLED
                        : PAGE_FORWARD_DISABLED,
                "current_page", currentPage,
                "page_count", pageCount
        );
    }
    // MINER PAGE
    public static long getCoinValue(ItemStack itemStack) {
        if (itemStack.getItemMeta() != null && PersistentDataAPI.hasLong(itemStack.getItemMeta(), Keys.coinValue)) {
            return PersistentDataAPI.getLong(itemStack.getItemMeta(), Keys.coinValue);
        }
        return 0;
    }
    public static ItemStack coinDisplay(ItemStack previousStack, long value) {
        return coinDisplay(getCoinValue(previousStack) + value);
    }
    public static ItemStack coinDisplay(long value) {
        return new CustomItemStack(
                META_COIN,
                meta -> {
                    meta.setDisplayName(ChatColors.color("&7Coins ready to Collect: %s%,d&f\uE803".formatted(ColorUtils.MM_YELLOW, value)));
                    meta.setLore(null);
                    PersistentDataAPI.setLong(meta, Keys.coinValue, value);
                }
        );
    }
    // UPGRADES PAGE
    public static ItemStack upgrade(ItemStack base, long cost, long level, long maxLevel) {
        return Utils.format(
                base,
                "upgrade_cost", "%,d".formatted(cost),
                "upgrade_level", "%,d".formatted(level),
                "max_upgrade_level", "%,d".formatted(maxLevel)
        );
    }
    public static ItemStack speedUpgrade(long cost, long level, long maxLevel) {
        return upgrade(SPEED_UPGRADE, cost, level, maxLevel);
    }
    public static ItemStack productionUpgrade(long cost, long level, long maxLevel) {
        return upgrade(PRODUCTION_UPGRADE, cost, level, maxLevel);
    }
    public static ItemStack reliabilityUpgrade(long cost, long level, long maxLevel) {
        return upgrade(RELIABILITY_UPGRADE, cost, level, maxLevel);
    }
    // CONTROL PANEL
    public static ItemStack core(String type, String color, int number, boolean running) {
        return Utils.format(
                running ? CORE_RUNNING : CORE_OFFLINE,
                "type", type,
                "color", color,
                "number", number
        );
    }
}
