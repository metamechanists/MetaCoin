package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import me.justahuman.furnished.metalib.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.utils.Keys;

@CommandAlias("metaminer")
@CommandPermission("metacoin.admin")
public class MinerCommand extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    public void giveMiner(CommandSender sender, String playerName) {
        if ((sender instanceof Player playerSender
                && (!playerSender.isPermissionSet("metacoin.admin")
                || !playerSender.hasPermission("metacoin.admin")))) {
             return;
        }

        final Player player = Bukkit.getPlayer(playerName);
        if (player == null || PersistentDataAPI.getOptionalBoolean(player, Keys.receivedMiner).orElse(false)) {
            return;
        }

        PersistentDataAPI.setBoolean(player, Keys.receivedMiner, true);
        ItemUtils.addOrDropItemMainHand(player, ItemStacks.metaCoinMiner(player));
    }
}
