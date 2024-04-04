package org.metamechanists.metacoin.implementation.runnables;

import me.justahuman.furnished.displaymodellib.models.components.ModelCuboid;
import me.justahuman.furnished.displaymodellib.sefilib.entity.display.DisplayGroup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import org.metamechanists.metacoin.MetaCoin;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.implementation.slimefun.Upgrades;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metalib.utils.ParticleUtils;
import org.metamechanists.metalib.utils.RandomUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WarrantyVoidRunnable extends BukkitRunnable {
    private final Player player;
    private final Block miner;
    private final Location location;
    private final DisplayGroup group;

    private int ticks = 0;

    public WarrantyVoidRunnable(Player player, Block miner, DisplayGroup group) {
        this.player = player;
        this.miner = miner;
        this.location = miner.getLocation().toCenterLocation();
        this.group = group;

        BlockStorage.addBlockInfo(miner, Keys.BS_WARRANTY_VOID, "TRUE");
        runTaskTimer(MetaCoin.getInstance(), 0, 1);
    }

    @Override
    public void run() {
        final Map<String, Display> displays = this.group.getDisplays();

        // Start another fire?
        if (ticks % 6 == 0) {
            final List<Display> displayList = new ArrayList<>(displays.values());
            for (int i = 0; i < displayList.size(); i++) {
                final Display display = RandomUtils.randomChoice(displayList);
                if (display instanceof BlockDisplay blockDisplay && blockDisplay.getBlock().getMaterial() == Material.FIRE) {
                    continue;
                }

                this.group.addDisplay("fire_" + ticks, new ModelCuboid()
                        .material(Material.FIRE)
                        .brightness(15)
                        .size(0.01F)
                        .location(display.getTransformation().getTranslation()
                                .add(new Vector3f((float) (0.1 * Math.pow(-1, ticks)), (float) (0.1 * Math.pow(-1, ticks + 1)), (float) (0.1 * Math.pow(-1, ticks)))))
                        .build(display.getLocation()));
                break;
            }
        }

        // Grow the existing fires
        for (String name : displays.keySet()) {
            if (!name.contains("fire")) {
                continue;
            }

            final Display display = displays.get(name);
            final Transformation transformation = display.getTransformation();
            if (transformation.getScale().x() < 1) {
                display.setInterpolationDelay(-1);
                display.setInterpolationDuration(4);
                display.setTransformation(new Transformation(
                        transformation.getTranslation(),
                        transformation.getLeftRotation(),
                        new Vector3f(transformation.getScale()).mul(1),
                        transformation.getRightRotation()
                ));
            }
        }

        // Player gets 15 seconds to run away :D
        if (ticks < 15 * 20) {
            ParticleUtils.randomParticle(location, Particle.CAMPFIRE_SIGNAL_SMOKE, 0.5, RandomUtils.randomInteger(4, 10));
            ParticleUtils.randomParticle(location, Particle.LAVA, 0.5, RandomUtils.randomInteger(5, 20));
            miner.getWorld().playSound(location, Sound.BLOCK_LAVA_EXTINGUISH, 0.1F, ThreadLocalRandom.current().nextFloat(0.1F, 1.0F));
            miner.getWorld().playSound(location, Sound.BLOCK_LAVA_POP, 0.1F, ThreadLocalRandom.current().nextFloat(0.1F, 1.0F));
            // todo figure out which of these is the alarm sounding one
            miner.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            ticks++;
            return;
        }

        // Go Kaboom
        cancel();

        miner.setType(Material.AIR);
        miner.getWorld().dropItemNaturally(location, ItemStacks.machineSlag(this.player, Upgrades.getLevels(location)));

        for (Display display : displays.values()) {
            final Transformation transformation = display.getTransformation();
            display.setInterpolationDelay(-1);
            display.setInterpolationDuration(40);
            display.setTransformation(new Transformation(new Vector3f(), transformation.getLeftRotation(), new Vector3f(), transformation.getRightRotation()));
        }

        BlockStorage.clearBlockInfo(miner);
        Bukkit.getScheduler().runTaskLater(MetaCoin.getInstance(), group::remove, 60L);
    }

    public static boolean isVoided(Block miner) {
        return BlockStorage.getLocationInfo(miner.getLocation(), Keys.BS_WARRANTY_VOID) != null;
    }
}
