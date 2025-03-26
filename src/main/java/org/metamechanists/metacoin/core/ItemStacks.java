package org.metamechanists.metacoin.core;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
            "&8⇨ &bCost: &7%upgrade_cost%&f\uE803",
            "&8⇨ &bUpgrade Level: &7%upgrade_level%/%max_upgrade_level%"
    );
    public static final ItemStack RELIABILITY_UPGRADE = new SlimefunItemStack(
            "_UI_MM_RELIABILITY_UPGRADE",
            Material.MAGENTA_CONCRETE,
            "&dReliability Upgrade",
            "&8⇨ &aIncreases &7machine reliability",
            "",
            "&8⇨ &dCost: &7%upgrade_cost%&f\uE803",
            "&8⇨ &dUpgrade Level: &7%upgrade_level%/%max_upgrade_level%"
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
            Material.BEACON,
            ColorUtils.MM_YELLOW + "MetaCoin™ Miner™",
            "&7The epitome of " + ColorUtils.MM_YELLOW + "blockchain &7technology,",
            "&7this " + ColorUtils.MM_YELLOW + "state-of-the-art &7encrypted " + ColorUtils.MM_YELLOW + "MetaCoin™ Miner™ &7harnesses",
            "&7the raw power of quantum computing to mine",
            ColorUtils.MM_YELLOW + "MetaCoins™ &7from the digital ether.",
            "",
            "&7With its patented " + ColorUtils.MM_YELLOW + "Proof-of-Play™ &7algorithm,",
            "&7it ensures a fair distribution of " + ColorUtils.MM_YELLOW + "&7MetaCoins™",
            "&7to all players in the " + ColorUtils.MM_YELLOW + "&7Metacoin UniVerse™.",
            "",
            "&7Experience the future of " + ColorUtils.MM_YELLOW + "decentralized &7finance",
            "&7with the " + ColorUtils.MM_YELLOW + "MetaCoin™ Miner™.",
            "",
            "&7Owner: " + ColorUtils.MM_YELLOW + "%player%",
            "&7Speed Level: " + ColorUtils.MM_YELLOW + "%speed_level%",
            "&7Production Level: " + ColorUtils.MM_YELLOW + "%production_level%",
            "&7Reliability Level: " + ColorUtils.MM_YELLOW + "%reliability_level%"
    );
    public static final SlimefunItemStack MACHINE_SLAG = new SlimefunItemStack(
            "MACHINE_SLAG",
            Material.MAGMA_BLOCK,
            ColorUtils.LAVA_ORANGE + "Machine Slag™",
            "&7Once a " + ColorUtils.LAVA_ORANGE + "beacon &7of hope and prosperity,",
            "&7the " + ColorUtils.LAVA_ORANGE + "MetaCoin™ Miner™ &7has been reduced to",
            "&7a mere husk of its former " + ColorUtils.LAVA_ORANGE + "glory&7.",
            "",
            "&7The " + ColorUtils.LAVA_ORANGE + "blockchain &7dream has turned into a " + ColorUtils.LAVA_ORANGE + "nightmare&7.",
            "&7The quantum " + ColorUtils.LAVA_ORANGE + "cores &7have overheated, the circuits",
            "&7have melted, and all that remains is this " + ColorUtils.LAVA_ORANGE + "slag&7.",
            "",
            "&7And who's to " + ColorUtils.LAVA_ORANGE + "blame&7? Look no further than the " + ColorUtils.LAVA_ORANGE + "mirror&7.",
            "&7Your insatiable greed for " + ColorUtils.LAVA_ORANGE + "MetaCoins™ &7has led to this disaster.",
            "&7But don't despair, for every end is a new " + ColorUtils.LAVA_ORANGE + "beginning&7.",
            "&7Who knows what the " + ColorUtils.LAVA_ORANGE + "future &7holds?",
            "",
            "&7Owner: " + ColorUtils.LAVA_ORANGE + "%player%",
            "&7Speed Level: " + ColorUtils.LAVA_ORANGE + "%speed_level%",
            "&7Production Level: " + ColorUtils.LAVA_ORANGE + "%production_level%",
            "&7Reliability Level: " + ColorUtils.LAVA_ORANGE + "%reliability_level%"
    );
    public static final SlimefunItemStack META_COIN = new SlimefunItemStack(
            "META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Gain " + ColorUtils.MM_YELLOW + "bragging &7rights, " + ColorUtils.MM_YELLOW + "throw &7it like a",
            "&7projectile, " + ColorUtils.MM_YELLOW + "compress &7it by the stack,",
            ColorUtils.MM_YELLOW + "settle &7a bet with a coin flip,",
            "&7or use it to upgrade the " + ColorUtils.MM_YELLOW + "MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "1&f\uE803"
    );
    public static final SlimefunItemStack COMPRESSED_META_COIN = new SlimefunItemStack(
            "COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Gain " + ColorUtils.MM_YELLOW + "bragging &7rights, " + ColorUtils.MM_YELLOW + "throw &7it like a",
            "&7projectile, " + ColorUtils.MM_YELLOW + "compress &7it by the stack,",
            ColorUtils.MM_YELLOW + "settle &7a bet with a coin flip,",
            "&7or use it to upgrade the " + ColorUtils.MM_YELLOW + "MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "64&f\uE803"
    );
    public static final SlimefunItemStack DOUBLE_COMPRESSED_META_COIN = new SlimefunItemStack(
            "DOUBLE_COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "2x Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Gain " + ColorUtils.MM_YELLOW + "bragging &7rights, " + ColorUtils.MM_YELLOW + "throw &7it like a",
            "&7projectile, " + ColorUtils.MM_YELLOW + "compress &7it by the stack,",
            ColorUtils.MM_YELLOW + "settle &7a bet with a coin flip,",
            "&7or use it to upgrade the " + ColorUtils.MM_YELLOW + "MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "4,096&f\uE803"
    );
    public static final SlimefunItemStack TRIPLE_COMPRESSED_META_COIN = new SlimefunItemStack(
            "TRIPLE_COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "3x Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Gain " + ColorUtils.MM_YELLOW + "bragging &7rights, " + ColorUtils.MM_YELLOW + "throw &7it like a",
            "&7projectile, " + ColorUtils.MM_YELLOW + "compress &7it by the stack,",
            ColorUtils.MM_YELLOW + "settle &7a bet with a coin flip,",
            "&7or use it to upgrade the " + ColorUtils.MM_YELLOW + "MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "262,144&f\uE803"
    );
    public static final SlimefunItemStack QUADRUPLE_COMPRESSED_META_COIN = new SlimefunItemStack(
            "QUADRUPLE_COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "4x Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Gain " + ColorUtils.MM_YELLOW + "bragging &7rights, " + ColorUtils.MM_YELLOW + "throw &7it like a",
            "&7projectile, " + ColorUtils.MM_YELLOW + "compress &7it by the stack,",
            ColorUtils.MM_YELLOW + "settle &7a bet with a coin flip,",
            "&7or use it to upgrade the " + ColorUtils.MM_YELLOW + "MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "16,777,216&f\uE803"
    );
    public static final SlimefunItemStack QUINTUPLE_COMPRESSED_META_COIN = new SlimefunItemStack(
            "QUINTUPLE_COMPRESSED_META_COIN",
            Material.HEART_OF_THE_SEA,
            ColorUtils.MM_YELLOW + "5x Compressed MetaCoin™",
            "&7The hallowed MetaCoin",
            "&7Gain " + ColorUtils.MM_YELLOW + "bragging &7rights, " + ColorUtils.MM_YELLOW + "throw &7it like a",
            "&7projectile, " + ColorUtils.MM_YELLOW + "compress &7it by the stack,",
            ColorUtils.MM_YELLOW + "settle &7a bet with a coin flip,",
            "&7or use it to upgrade the " + ColorUtils.MM_YELLOW + "MetaMiner™",
            "",
            "&7Value: " + ColorUtils.MM_YELLOW + "1,073,741,824&f\uE803"
    );

    // FORMAT METHODS
    // COMMANDS
    public static ItemStack metaCoinMiner(Player player) {
        return metaCoinMiner(player, 1, 1, 1);
    }
    public static ItemStack metaCoinMiner(Player player, int... levels) {
        final ItemStack miner = Utils.format(new CustomItemStack(META_COIN_MINER),
                "player", player.getName(),
                "speed_level", levels[0],
                "production_level", levels[1],
                "reliability_level", levels[2]);
        final ItemMeta itemMeta = miner.getItemMeta();
        if (itemMeta != null) {
            PersistentDataAPI.setString(itemMeta, Keys.owner, player.getUniqueId().toString());
            PersistentDataAPI.setInt(itemMeta, Keys.speedLevel, levels[0]);
            PersistentDataAPI.setInt(itemMeta, Keys.productionLevel, levels[1]);
            PersistentDataAPI.setInt(itemMeta, Keys.reliabilityLevel, levels[2]);
            miner.setItemMeta(itemMeta);
        }
        return miner;
    }
    public static ItemStack machineSlag(Player player, int... levels) {
        return Utils.format(new CustomItemStack(MACHINE_SLAG),
                "player", player.getName(),
                "speed_level", levels[0],
                "production_level", levels[1],
                "reliability_level", levels[2]);
    }
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
