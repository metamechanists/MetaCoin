package org.metamechanists.metacoin.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.metacoin.MetaCoin;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(MetaCoin.getInstance(), key);
    }
}
