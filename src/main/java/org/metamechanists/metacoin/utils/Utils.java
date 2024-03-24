package org.metamechanists.metacoin.utils;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void drawBackground(ChestMenu menu, ItemStack itemStack, int... slots) {
        drawBackground(menu, itemStack, ChestMenuUtils.getEmptyClickHandler(), slots);
    }

    public static void drawBackground(ChestMenu menu, ItemStack itemStack, ChestMenu.MenuClickHandler clickHandler, int... slots) {
        for (int slot : slots) {
            menu.addItem(slot, itemStack, clickHandler);
        }
    }

    public static ItemStack format(ItemStack itemStack, Object... placeholders) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final List<String> lore = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        String displayName = itemMeta.getDisplayName();
        for (int i = 0; i < placeholders.length; i++) {
            if (i % 2 == 0) {
                for (int li = 0; li < lore.size(); li++) {
                    lore.set(li, lore.get(li).replace("%" + placeholders[i] + "%", String.valueOf(placeholders[i + 1])));
                }
                displayName = displayName.replace("%" + placeholders[i] + "%", String.valueOf(placeholders[i + 1]));
            }
        }
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static long getClosest(long i1, long i2, long target) {
        long d1 = Math.abs(target - i1);
        long d2 = Math.abs(target - i2);
        return d1 > d2 ? i2 : i1;
    }
}
