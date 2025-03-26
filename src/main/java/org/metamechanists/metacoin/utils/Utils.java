package org.metamechanists.metacoin.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metamechanists.metacoin.MetaCoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class Utils {
    public static final Random RANDOM = new Random();

    public static final boolean WARRANTIES_VOIDED = MetaCoin.getInstance().getConfig().getBoolean("warrantiesVoided");

    public static void drawBackground(ChestMenu menu, ItemStack itemStack, int... slots) {
        drawBackground(menu, itemStack, ChestMenuUtils.getEmptyClickHandler(), slots);
    }

    public static void drawBackground(ChestMenu menu, ItemStack itemStack, ChestMenu.MenuClickHandler clickHandler, int... slots) {
        for (int slot : slots) {
            menu.addItem(slot, itemStack, clickHandler);
        }
    }

    public static ItemStack format(ItemStack itemStack, Object... placeholders) {
        itemStack = new ItemStack(itemStack);
        
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final List<String> lore = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        String displayName = itemMeta.getDisplayName();
        for (int i = 0; i < placeholders.length; i++) {
            if (i % 2 == 0) {
                for (int li = 0; li < lore.size(); li++) {
                    lore.set(li, lore.get(li).replace("%" + placeholders[i] + "%", ChatColors.color(String.valueOf(placeholders[i + 1]))));
                }
                displayName = displayName.replace("%" + placeholders[i] + "%", ChatColors.color(String.valueOf(placeholders[i + 1])));
            }
        }
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static <E> boolean containsAny(List<E> disabledCores, List<E> productionCores) {
        for (E e : productionCores) {
            if (disabledCores.contains(e)) {
                return true;
            }
        }
        return false;
    }
}
