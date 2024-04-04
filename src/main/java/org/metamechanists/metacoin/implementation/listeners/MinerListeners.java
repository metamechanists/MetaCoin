package org.metamechanists.metacoin.implementation.listeners;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockBreakEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metacoin.implementation.runnables.WarrantyVoidRunnable;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinMiner;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Language;

public class MinerListeners implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPickupItem(EntityPickupItemEvent event) {
        final ItemStack itemStack = event.getItem().getItemStack();
        if (!(SlimefunItem.getByItem(itemStack) instanceof MetaCoinMiner)) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!player.getUniqueId().toString().equals(PersistentDataAPI.getString(itemStack.getItemMeta(), Keys.owner))) {
            Language.sendMessage(player, "miner.error.no-permission");
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSlimefunBlockPlace(SlimefunBlockPlaceEvent event) {
        if (!(event.getSlimefunItem() instanceof MetaCoinMiner)) {
            return;
        }

        final Player player = event.getPlayer();
        if (PersistentDataAPI.getOptionalBoolean(player, Keys.minerPlaced).orElse(false)) {
            Language.sendMessage(player, "miner.error.placed-already");
            event.setCancelled(true);
            return;
        }

        if (!player.getUniqueId().toString().equals(PersistentDataAPI.getString(event.getItemStack().getItemMeta(), Keys.owner))) {
            Language.sendMessage(player, "miner.error.no-permission");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSlimefunBlockBreak(SlimefunBlockBreakEvent event) {
        if (!(event.getSlimefunItem() instanceof MetaCoinMiner)) {
            return;
        }

        final Player player = event.getPlayer();
        final Block miner = event.getBlockBroken();
        if (!player.getUniqueId().equals(MetaCoinMiner.getOwner(miner))) {
            Language.sendMessage(player, "miner.error.no-permission");
            event.setCancelled(true);
            return;
        }

        if (WarrantyVoidRunnable.isVoided(miner)) {
            event.setCancelled(true);
        }
    }
}
