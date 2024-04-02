package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.core.Leaderboard;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;

@CommandAlias("depositcoins")
public class DepositCommand extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    public void depositCoins(CommandSender sender, String playerName) {
        if ((sender instanceof Player playerSender
                && (!playerSender.isPermissionSet("metacoin.admin")
                || !playerSender.hasPermission("metacoin.admin")))) {
            return;
        }

        final Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return;
        }

        final long value = MetaCoinItem.getTotalCoinValue(player);
        MetaCoinItem.removeCoins(player, value);
        Leaderboard.updateLeaderboard(player.getUniqueId(), Leaderboard.getValue(player.getUniqueId()) + value);
    }
}
