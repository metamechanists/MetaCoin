package org.metamechanists.metacoin.core;

import dev.sefiraat.sefilib.slimefun.itemgroup.DummyItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metacoin.MetaCoin;
import org.metamechanists.metacoin.utils.Keys;

public class Groups {
    public static final ItemGroup group = new DummyItemGroup(Keys.metaCoinItems, new ItemStack(Material.DEAD_BUSH));

    public static void init() {
        MetaCoin addon = MetaCoin.getInstance();
        group.register(addon);
    }
}
