package org.metamechanists.metacoin.implementation.runnables;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.metacoin.MetaCoin;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.core.Statistics;
import org.metamechanists.metacoin.implementation.slimefun.Upgrades;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.metalib.utils.ParticleUtils;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WarrantyVoidRunnable extends BukkitRunnable {
    private static final List<Sound> ALARM_SOUNDS = List.of(
            Sound.BLOCK_NOTE_BLOCK_BANJO,
            Sound.BLOCK_NOTE_BLOCK_BASS,
            Sound.BLOCK_NOTE_BLOCK_BELL,
            Sound.BLOCK_NOTE_BLOCK_BIT,
            Sound.BLOCK_NOTE_BLOCK_CHIME,
            Sound.BLOCK_NOTE_BLOCK_FLUTE,
            Sound.BLOCK_NOTE_BLOCK_GUITAR,
            Sound.BLOCK_NOTE_BLOCK_HARP,
            Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
            Sound.BLOCK_NOTE_BLOCK_PLING,
            Sound.BLOCK_NOTE_BLOCK_SNARE,
            Sound.BLOCK_NOTE_BLOCK_XYLOPHONE
    );

    private final Player player;
    private final Block miner;
    private final Location location;
    private final DisplayGroup group;
    private final ItemStack slag;

    private int ticks = 0;

    public WarrantyVoidRunnable(Player player, Block miner, DisplayGroup group) {
        this.player = player;
        this.miner = miner;
        this.location = miner.getLocation().toCenterLocation();
        this.group = group;
        this.slag = ItemStacks.machineSlag(this.player, Upgrades.getLevels(miner.getLocation()));;
        BlockStorage.addBlockInfo(miner, Keys.BS_WARRANTY_VOID, "TRUE");
        runTaskTimer(MetaCoin.getInstance(), 0, 1);
        Statistics.addVoidedWarranty(player);
    }

    @Override
    public void run() {
        final ThreadLocalRandom random = RandomUtils.randomThread;
        final Map<String, Display> displays = this.group.getDisplays();

        // Start another fire
        if (ticks % 2 == 0) {
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
                        .location(display.getTransformation().getTranslation().add(
                                (float) (0.1 * Math.pow(-1, ticks)),
                                (float) (0.1 * Math.pow(-1, ticks + 1)),
                                (float) (0.1 * Math.pow(-1, ticks))))
                        .build(display.getLocation()));
                break;
            }
        }

        // Grow the existing fires
        for (String name : displays.keySet()) {
            if (!name.contains("fire") || name.contains(String.valueOf(ticks))) {
                continue;
            }

            final Display display = displays.get(name);
            final Transformation transformation = display.getTransformation();
            if (transformation.getScale().x() <= 0.011F) {
                display.setInterpolationDelay(-1);
                display.setInterpolationDuration(4);
                display.setTransformation(new Transformation(
                        transformation.getTranslation(),
                        transformation.getLeftRotation(),
                        new Vector3f(random.nextFloat(0.4F, 0.8F)),
                        transformation.getRightRotation()
                ));
            }
        }

        // Player gets 5 seconds to run away :D
        if (ticks < 5 * 20) {
            ParticleUtils.randomParticle(location, Particle.CAMPFIRE_SIGNAL_SMOKE, 0.5, RandomUtils.randomInteger(4, 10));
            ParticleUtils.randomParticle(location, Particle.LAVA, 0.5, RandomUtils.randomInteger(5, 20));
            miner.getWorld().playSound(location, Sound.BLOCK_LAVA_EXTINGUISH, 0.5F, random.nextFloat(0.1F, 1.0F));
            miner.getWorld().playSound(location, Sound.BLOCK_LAVA_POP, 0.5F, random.nextFloat(0.1F, 1.0F));
            miner.getWorld().playSound(location, RandomUtils.randomChoice(ALARM_SOUNDS), 1, 1);
            ticks++;
            return;
        }

        // Go Kaboom
        cancel();

        miner.setType(Material.AIR);
        miner.getWorld().createExplosion(location, 4F * 8, true, true);

        for (Display display : displays.values()) {
            final Transformation transformation = display.getTransformation();
            display.setInterpolationDelay(-1);
            display.setInterpolationDuration(15);
            display.setTransformation(new Transformation(transformation.getTranslation().mul(
                    new Vector3f(RandomUtils.randomInteger(-4 , 4),
                            RandomUtils.randomInteger(-4 , 4),
                            RandomUtils.randomInteger(-4 , 4))),
                    transformation.getLeftRotation(),
                    new Vector3f(),
                    transformation.getRightRotation()));
        }

        BlockStorage.clearBlockInfo(miner);
        Bukkit.getScheduler().runTaskLater(MetaCoin.getInstance(), () -> {
            group.remove();
            ItemUtils.addOrDropItem(player, slag);
        }, 30L);
    }

    public static boolean isVoided(Block miner) {
        return BlockStorage.getLocationInfo(miner.getLocation(), Keys.BS_WARRANTY_VOID) != null;
    }
}
