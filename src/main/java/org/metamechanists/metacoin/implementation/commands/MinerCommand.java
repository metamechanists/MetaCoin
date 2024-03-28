package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import me.justahuman.furnished.metalib.utils.ItemUtils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.utils.Keys;

@CommandAlias("metaminer")
public class MinerCommand extends BaseCommand {
    @Default
    @CommandCompletion("@players")
    public void giveMiner(ConsoleCommandSender sender, Player player) {
        if (PersistentDataAPI.hasBoolean(player, Keys.receivedMiner)) {
             return;
        }

        PersistentDataAPI.setBoolean(player, Keys.receivedMiner, true);
        ItemUtils.addOrDropItemMainHand(player, ItemStacks.metaCoinMiner(player));
    }
}
