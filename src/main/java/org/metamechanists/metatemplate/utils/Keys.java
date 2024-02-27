package org.metamechanists.metatemplate.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.metatemplate.MetaTemplate;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(MetaTemplate.getInstance(), key);
    }
}
