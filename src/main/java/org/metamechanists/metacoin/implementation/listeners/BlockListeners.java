package org.metamechanists.metacoin.implementation.listeners;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockBreakEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockPlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinMiner;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Language;

public class BlockListeners implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSlimefunBlockPlace(SlimefunBlockPlaceEvent event) {
        if (!(event.getSlimefunItem() instanceof MetaCoinMiner)) {
            return;
        }

        final Player player = event.getPlayer();
        if (PersistentDataAPI.getOptionalBoolean(player, Keys.minerPlaced).orElse(false)) {
            Language.sendMessage(player, "miner.error.placed-already");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSlimefunBlockPlace(SlimefunBlockBreakEvent event) {
        if (!(event.getSlimefunItem() instanceof MetaCoinMiner)) {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.getUniqueId().equals(MetaCoinMiner.getOwner(event.getBlockBroken()))) {
            Language.sendMessage(player, "miner.error.no-permission");
            event.setCancelled(true);
        }
    }
}
