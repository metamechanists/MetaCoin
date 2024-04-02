package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.utils.Keys;

@CommandAlias("resetstuff")

@CommandPermission("metacoin.admin")
public class ResetCommand extends BaseCommand {
    @Default
    public void resetStuff(Player player) {
        PersistentDataAPI.remove(player, Keys.minerPlaced);
        PersistentDataAPI.remove(player, Keys.receivedMiner);
    }
}
