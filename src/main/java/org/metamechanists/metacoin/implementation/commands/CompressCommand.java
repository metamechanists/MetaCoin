package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;
import org.metamechanists.metalib.utils.ItemUtils;

@CommandAlias("compresscoin")
public class CompressCommand extends BaseCommand {
    @Default
    @CommandCompletion("player")
    public void compressCoins(CommandSender sender, Player player) {
        if ((sender instanceof Player playerSender
                && (!playerSender.isPermissionSet("metaminer.admin")
                || !playerSender.hasPermission("metaminer.admin")))) {
            return;
        }

        final long value = MetaCoinItem.getTotalCoinValue(player);
        MetaCoinItem.removeCoins(player, value);
        ItemUtils.addOrDropItem(player, MetaCoinItem.withTotalValue(value));
    }
}
