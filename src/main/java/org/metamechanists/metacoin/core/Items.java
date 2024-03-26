package org.metamechanists.metacoin.core;

import dev.sefiraat.sefilib.slimefun.itemgroup.DummyItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.metamechanists.metacoin.MetaCoin;
import org.metamechanists.metacoin.implementation.MetaCoinItem;
import org.metamechanists.metacoin.implementation.MetaCoinMiner;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metalib.utils.ColorUtils;

public class Items {
    public static void init() {
        final MetaCoin addon = MetaCoin.getInstance();
        final ItemGroup group = new DummyItemGroup(Keys.metaCoinItems, new CustomItemStack(
                ItemStacks.META_COIN,
                ColorUtils.MM_YELLOW + "MetaCoin™"
        ));

        new MetaCoinItem(group, ItemStacks.META_COIN, 1).register(addon);
        new MetaCoinItem(group, ItemStacks.COMPRESSED_META_COIN, 64).register(addon);
        new MetaCoinItem(group, ItemStacks.DOUBLE_COMPRESSED_META_COIN, 4096).register(addon);
        new MetaCoinItem(group, ItemStacks.TRIPLE_COMPRESSED_META_COIN, 262144).register(addon);

        MetaCoinItem.COINS.put(262144L, ItemStacks.TRIPLE_COMPRESSED_META_COIN);
        MetaCoinItem.COINS.put(4096L, ItemStacks.DOUBLE_COMPRESSED_META_COIN);
        MetaCoinItem.COINS.put(64L, ItemStacks.COMPRESSED_META_COIN);
        MetaCoinItem.COINS.put(1L, ItemStacks.META_COIN);

        new MetaCoinMiner(group, ItemStacks.META_COIN_MINER).register(addon);
    }
}
