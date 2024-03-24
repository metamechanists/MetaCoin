package org.metamechanists.metacoin.utils;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.core.services.CustomTextureService;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static Config modelConfig = null;

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

    public static long getClosest(long i1, long i2, long target) {
        long d1 = Math.abs(target - i1);
        long d2 = Math.abs(target - i2);
        return d1 > d2 ? i2 : i1;
    }

    public static void setModel(String id, long model) {
        if (modelConfig == null) {
            try {
                Field configField = CustomTextureService.class.getDeclaredField("config");
                configField.setAccessible(true);
                modelConfig = (Config) configField.get(Slimefun.getItemTextureService());
                modelConfig.setValue(id, model);
                modelConfig.save();
            } catch (Exception e) {
                Slimefun.logger().severe("Couldn't get the model config!");
                Slimefun.logger().severe("Some items may not have the custom texture!");
            }
            return;
        }

        modelConfig.setValue(id, model);
        modelConfig.save();
    }
}
