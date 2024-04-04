package org.metamechanists.metacoin.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.metacoin.MetaCoin;

public class Keys {
    public static final NamespacedKey metaCoinItems = newKey("meta_coin_items");
    public static final NamespacedKey coinValue = newKey("coin_value");
    public static final NamespacedKey receivedMiner = newKey("received_miner");
    public static final NamespacedKey minerPlaced = newKey("miner_placed");
    public static final NamespacedKey flippingCoin = newKey("flipping_coin");
    public static final NamespacedKey owner = newKey("owner");
    public static final NamespacedKey speedLevel = newKey("speed_level");
    public static final NamespacedKey productionLevel = newKey("production_level");
    public static final NamespacedKey reliabilityLevel = newKey("reliability_level");
    public static final String BS_DISABLED_CORES = "DISABLED_CORES";
    public static final String BS_OWNER = "OWNER";
    public static final String BS_LAST_MENU = "LAST_MENU";
    public static final String BS_COIN_CACHE = "COIN_CACHE";
    public static final String BS_WARRANTY_VOID = "WARRANTY_VOID";

    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(MetaCoin.getInstance(), key);
    }
}
