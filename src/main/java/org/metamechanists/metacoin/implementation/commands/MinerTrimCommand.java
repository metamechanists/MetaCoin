package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

@CommandAlias("minertrim")
@CommandPermission("metacoin.admin")
public class MinerTrimCommand extends BaseCommand {
    @Default
    @CommandCompletion("@trim_patterns @trim_materials")
    public void applyTrim(Player player, String trimPattern, String trimMaterial) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isEmpty() || !(itemStack.getItemMeta() instanceof ArmorMeta armorMeta)) {
            return;
        }

        try {
            armorMeta.setTrim(new ArmorTrim(Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(trimMaterial)), Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(trimPattern))));
            itemStack.setItemMeta(armorMeta);
        } catch (Exception ignored) {
            player.sendMessage(ChatColor.RED + "Invalid arguments!");
        }
    }
}
