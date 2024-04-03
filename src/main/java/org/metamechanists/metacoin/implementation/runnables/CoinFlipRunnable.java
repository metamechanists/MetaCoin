package org.metamechanists.metacoin.implementation.runnables;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.justahuman.furnished.displaymodellib.models.components.ModelItem;
import me.justahuman.furnished.displaymodellib.transformations.TransformationMatrixBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3d;
import org.metamechanists.metacoin.MetaCoin;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoinFlipRunnable extends BukkitRunnable {
    private static final Map<UUID, CoinFlipRunnable> RUNNABLES = new HashMap<>();

    private int ticks = 0;
    private int landed = -1;
    private WeakReference<ItemDisplay> display = null;

    private final Player player;
    private final String message;
    private final SlimefunItem slimefunItem;

    public CoinFlipRunnable(SlimefunItem slimefunItem, Player player, String message) {
        this.slimefunItem = slimefunItem;
        this.message = message;
        this.player = player;

        runTaskTimer(MetaCoin.getInstance(), 0, 1);
        RUNNABLES.put(player.getUniqueId(), this);
    }

    @Override
    public void run() {
        // First Tick
        if (ticks == 0) {
            final Location spawnLocation = this.player.getEyeLocation().clone()
                    .subtract(0, 0.25, 0)
                    .add(this.player.getEyeLocation().getDirection().multiply(0.5));

            final ItemDisplay itemDisplay = new ModelItem()
                    .item(slimefunItem.getItem())
                    .size(0.33F)
                    .rotation(new Vector3d(Math.PI / 2, 0, 0))
                    .brightness(15)
                    .billboard(Display.Billboard.FIXED)
                    .build(spawnLocation);

            this.display = new WeakReference<>(itemDisplay);
        }

        // Display removed for some reason
        final ItemDisplay itemDisplay = display == null ? null : display.get();
        if (itemDisplay == null) {
            cancel();
            return;
        }

        // Still Falling
        if (landed == -1) {
            final TransformationMatrixBuilder builder = new TransformationMatrixBuilder();
            builder.rotate(Math.PI / 6 * Math.ceilDiv(ticks, 5), 0, 0);

            itemDisplay.setInterpolationDelay(-1);
            itemDisplay.setInterpolationDuration(4);
            itemDisplay.setTransformationMatrix(builder.buildForItemDisplay());
        }

        // Can despawn now
        if (ticks - landed == 20) {
            cancel();
            return;
        }

        // Temp
        if (ticks >= 100) {
            cancel();
            return;
        }

        ticks++;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        this.player.sendMessage(this.message);
        if (this.display != null && this.display.get() != null) {
            this.display.get().remove();
        }
        RUNNABLES.remove(this.player.getUniqueId());
        super.cancel();
    }

    public static boolean isFlipping(Entity entity) {
        return isFlipping(entity.getUniqueId());
    }

    public static boolean isFlipping(UUID uuid) {
        return RUNNABLES.containsKey(uuid);
    }
}
