package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.implementation.runnables.WarrantyVoidRunnable;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinMiner;

@CommandAlias("voidWarranty")
@CommandPermission("metacoin.admin")
public class VoidWarrantyCommand extends BaseCommand {
    @Default
    public void voidWarranty(Player player) {
        final Block block = player.getTargetBlockExact(7);
        if (block != null && BlockStorage.check(block) instanceof MetaCoinMiner miner) {
            new WarrantyVoidRunnable(player, block, miner.getDisplayGroup(block));
        }
    }
}
