package org.metamechanists.metacoin.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.metacoin.MetaCoin;

public class Keys {
    public static final NamespacedKey metaCoinItems = Keys.newKey("meta_coin_items");
    public static final NamespacedKey coinValue = Keys.newKey("coin_value");
    public static final String BS_DISABLED_CORES = "DISABLED_CORES";
    public static final String BS_OWNER = "OWNER";
    public static final String BS_LAST_MENU = "LAST_MENU";
    public static final String BS_MALFUNCTION_LEVEL = "MALFUNCTION_LEVEL";

    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(MetaCoin.getInstance(), key);
    }
}
