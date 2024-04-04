package org.metamechanists.metacoin.implementation.runnables;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.metacoin.MetaCoin;

import java.lang.ref.WeakReference;

public class WarrantyVoidRunnable extends BukkitRunnable {
    private final WeakReference<Player> playerReference;
    private final Block miner;

    public WarrantyVoidRunnable(Player player, Block miner) {
        this.playerReference = new WeakReference<>(player);
        this.miner = miner;

        runTaskTimer(MetaCoin.getInstance(), 0, 1);
    }

    @Override
    public void run() {

    }
}
