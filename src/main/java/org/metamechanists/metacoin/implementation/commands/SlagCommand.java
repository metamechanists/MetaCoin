package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinMiner;
import org.metamechanists.metacoin.implementation.slimefun.Upgrades;
import org.metamechanists.metalib.utils.ItemUtils;

@CommandAlias("becomeslag")
@CommandPermission("metacoin.admin")
public class SlagCommand extends BaseCommand {
    @Default
    public void becomeSlag(Player player) {
        final Block block = player.getTargetBlockExact(7);
        if (block != null && BlockStorage.check(block) instanceof MetaCoinMiner) {
            ItemUtils.addOrDropItemMainHand(player, ItemStacks.machineSlag(player, Upgrades.getLevels(block.getLocation())));
        }
    }
}
