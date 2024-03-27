package org.metamechanists.metacoin.implementation;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.justahuman.furnished.displaymodellib.builders.BlockDisplayBuilder;
import me.justahuman.furnished.displaymodellib.models.ModelBuilder;
import me.justahuman.furnished.displaymodellib.sefilib.entity.display.DisplayGroup;
import me.justahuman.furnished.implementation.furniture.absraction.DisplayModelBlock;
import me.justahuman.furnished.implementation.furniture.interfaces.Sittable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metacoin.api.AnimatedModelBuilder;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.core.Models;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Language;
import org.metamechanists.metacoin.utils.Utils;
import org.metamechanists.metalib.utils.ParticleUtils;
import org.metamechanists.metalib.utils.RandomUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


@SuppressWarnings("deprecation")
public class MetaCoinMiner extends DisplayModelBlock implements Sittable {
    // All Pages
    private static final int[] SPEED_DISPLAY = { 0, 1, 2, 36, 37, 38 };
    private static final int[] PRODUCTION_DISPLAY = { 3, 4, 5, 39, 40, 41 };
    private static final int[] RELIABILITY_DISPLAY = { 6, 7, 8, 42, 43, 44 };
    private static final int[] BOTTOM_BACKGROUND = { 45, 47, 48, 49, 50, 51, 53 };
    private static final int PAGE_BACK = 46;
    private static final int PAGE_FORWARD = 52;
    // Miner Page
    private static final int[] MINER_PROGRESS = { 10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24, 30, 31, 32, 40 };
    private static final int[] MINER_BACKGROUND = { 9, 17, 18, 19, 25, 26, 27, 28, 29, 33, 34, 35};
    private static final int MINER_OUTPUT = 49;
    // Upgrades Page
    private static final int UPGRADE_SPEED = 19;
    private static final int UPGRADE_PRODUCTION = 22;
    private static final int UPGRADE_RELIABILITY = 25;
    // Panel Page
    private static final List<Integer> ALL_CORES = IntStream.range(9, 36).boxed().toList();
    private static final List<Integer> SPEED_CORES = List.of(9, 10, 11, 18, 19, 20, 27, 28, 29);
    private static final List<Integer> PRODUCTION_CORES = List.of(12, 13, 14, 21, 22, 23, 30, 31, 32);
    private static final List<Integer> RELIABILITY_CORES = List.of(15, 16, 17, 24, 25, 26, 33, 34, 35);

    private static final Map<BlockPosition, Integer> PROGRESS = new HashMap<>();
    private static final Map<BlockPosition, UUID> ACCESSING_CONTROL_PANEL = new HashMap<>();
    private static final Set<BlockPosition> MALFUNCTIONING = new HashSet<>();
    private static final int TICKS_PER_PROGRESS = 4;

    public MetaCoinMiner(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[0]);

        buildPreset();
        addItemHandler(onTick());
    }

    // Item Handlers & Presets {

    @Override
    public void postRegister() {
        setHidden(true);
    }

    public void buildPreset() {
        new BlockMenuPreset(getId(), "&fMeta Miner - Drill") {
            @Override
            public void init() {
                setupMenu(this, 1);

                drawBackground(MINER_BACKGROUND);
                drawBackground(ItemStacks.MINER_PROGRESS_FALSE, MINER_PROGRESS);
                addItem(MINER_OUTPUT, new ItemStack(Material.AIR), (o1, o2, o3, o4) -> true);
            }

            @Override
            @ParametersAreNonnullByDefault
            public boolean canOpen(Block block, Player player) {
                return player.getUniqueId().equals(getOwner(block));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }

            @Override
            @ParametersAreNonnullByDefault
            public void newInstance(BlockMenu menu, Block miner) {
                final Location minerLocation = miner.getLocation();
                menu.addMenuOpeningHandler(player -> {
                    BlockStorage.addBlockInfo(miner, Keys.BS_LAST_MENU, "MINER");
                    updateMinerMenu(menu, new BlockPosition(miner));
                });

                menu.addMenuClickHandler(PAGE_FORWARD, (player, ignored1, ignored2, ignored3) -> {
                    BlockStorage.addBlockInfo(miner, Keys.BS_LAST_MENU, "UPGRADES");
                    openUpgrades(player, menu, minerLocation);
                    return false;
                });
            }
        };
    }

    protected BlockTicker onTick() {
        return new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block miner, SlimefunItem slimefunItem, Config config) {
                final BlockPosition minerPosition = new BlockPosition(miner);
                final Location minerLocation = miner.getLocation();
                final int[] levels = Upgrades.getLevels(minerLocation);
                final int[] realLevels = { levels[0] - 1, levels[1] - 1, levels[2] - 1 };
                if (!MALFUNCTIONING.contains(minerPosition) && RandomUtils.chance(realLevels[0] + realLevels[1] - 2 * realLevels[2])) {
                    MetaCoinMiner.this.malfunction(minerLocation, realLevels);
                }
                MetaCoinMiner.this.tick(minerLocation, minerPosition, levels);
            }
        };
    }
    public void tick(Location minerLocation, BlockPosition minerPosition, int[] levels) {
        final List<Integer> disabledCores = getDisabledCores(minerLocation);
        final boolean malfunctioning = !disabledCores.isEmpty();
        boolean productionMalfunction = malfunctioning && Utils.containsAny(disabledCores, PRODUCTION_CORES);
        boolean speedMalfunction = malfunctioning && Utils.containsAny(disabledCores, SPEED_CORES);
        int malfunctionLevel = getMalfunctionLevel(minerLocation);

        if (malfunctioning) {
            malfunctionLevel++;
            MALFUNCTIONING.add(minerPosition);
            malfunctionTick(minerLocation);
            setMalfunctionLevel(minerLocation, malfunctionLevel);
        } else {
            if (malfunctionLevel > 0) {
                malfunctionLevel--;
                setMalfunctionLevel(minerLocation, malfunctionLevel);
            }
            MALFUNCTIONING.remove(minerPosition);
        }

        final int progress = PROGRESS.getOrDefault(minerPosition, 0);
        if (progress < MINER_PROGRESS.length * TICKS_PER_PROGRESS) {
            PROGRESS.put(minerPosition, progress + (speedMalfunction ? 1 : levels[0]));
            updateMinerMenu(BlockStorage.getInventory(minerLocation), minerPosition);
            return;
        }

        final BlockMenu menu = BlockStorage.getInventory(minerLocation);
        if (menu == null) {
            return;
        }

        PROGRESS.put(minerPosition, 0);
        updateMinerMenu(menu, minerPosition);
        menu.pushItem(productionMalfunction ? MetaCoinItem.withValue(1) : MetaCoinItem.fromProductionLevel(levels[1]), MINER_OUTPUT);
    }


    @Override
    protected void onBlockPlace(@NotNull BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setType(this.getBaseMaterial());

        DisplayGroup group = new DisplayGroup(block.getLocation().toCenterLocation());
        group.getParentDisplay().setInteractionHeight(0.0F);
        group.getParentDisplay().setInteractionWidth(0.0F);

        Location buildLocation = block.getLocation().add(this.getBuildOffset());
        Models.MINER_START_MODEL().build(group, buildLocation);

        BlockStorage.addBlockInfo(block, "display-uuid", group.getParentUUID().toString());
        BlockStorage.addBlockInfo(event.getBlock(), Keys.BS_OWNER, event.getPlayer().getUniqueId().toString());
        BlockStorage.addBlockInfo(event.getBlock(), Keys.BS_MALFUNCTION_LEVEL, "0");
    }

    @Override
    protected void onBlockUse(@NotNull PlayerRightClickEvent event) {
        final Player player = event.getPlayer();
        final Block miner = event.getClickedBlock().orElse(null);
        if (player.isSneaking() || miner == null) {
            return;
        }

        event.setUseBlock(Event.Result.DENY);
        if (!player.getUniqueId().equals(getOwner(miner))) {
            Language.sendMessage(player, "miner.menu.no-permission");
            return;
        }

        final Location minerLocation = miner.getLocation();
        final BlockMenu minerMenu = BlockStorage.getInventory(miner);
        final String lastMenu = BlockStorage.getLocationInfo(miner.getLocation(), Keys.BS_LAST_MENU);
        if (lastMenu == null) {
            minerMenu.open(player);
            return;
        }

        switch (lastMenu) {
            case "UPGRADES" -> openUpgrades(player, minerMenu, minerLocation);
            case "CONTROL_PANEL" -> openControlPanel(player, minerMenu, minerLocation);
            default -> minerMenu.open(player);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void onBlockBreak(BlockBreakEvent event, ItemStack itemStack, List<ItemStack> drops) {
        super.onBlockBreak(event, itemStack, drops);
        final BlockMenu menu = BlockStorage.getInventory(event.getBlock());
        if (menu != null) {
            menu.dropItems(event.getBlock().getLocation(), MINER_OUTPUT);
        }
    }

    // Malfunction Mechanic

    public void malfunction(Location miner, int[] levels) {
        final List<Integer> enabledCores = new ArrayList<>(ALL_CORES);
        final List<Integer> disabledCores = new ArrayList<>();

        if (!getDisabledCores(miner).isEmpty()) {
            return;
        }

        for (int i = 0; i < Math.max(1, levels[0] + levels[1] - 2 * levels[2]); i++) {
            if (enabledCores.isEmpty()) {
                break;
            }

            final Integer core = RandomUtils.randomChoice(enabledCores);
            enabledCores.remove(core);
            disabledCores.add(core);
        }
        setDisabledCores(miner, disabledCores);

        final UUID uuid = ACCESSING_CONTROL_PANEL.remove(new BlockPosition(miner));
        if (uuid != null) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                Slimefun.runSync(() -> {
                    player.closeInventory();
                    openControlPanel(player, BlockStorage.getInventory(miner), miner);
                });
            }
        }
    }

    public void malfunctionTick(Location miner) {
        ParticleUtils.randomParticle(miner.toCenterLocation(), Particle.CAMPFIRE_SIGNAL_SMOKE, 0.5, RandomUtils.randomInteger(1, 3));
        ParticleUtils.randomParticle(miner.toCenterLocation(), Particle.LAVA, 0.5, RandomUtils.randomInteger(1, 3));
        Slimefun.runSync(() -> miner.getWorld().playSound(miner.toCenterLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.1F, ThreadLocalRandom.current().nextFloat(0.1F, 1.0F)));
    }

    // Menus

    public void updateMinerMenu(BlockMenu minerMenu, BlockPosition miner) {
        if (minerMenu == null || !minerMenu.hasViewer()) {
            return;
        }

        int index = 1;
        final int progress = PROGRESS.getOrDefault(miner, 0);
        for (int slot : MINER_PROGRESS) {
            minerMenu.replaceExistingItem(slot, index * 4 > progress ? ItemStacks.MINER_PROGRESS_FALSE : ItemStacks.MINER_PROGRESS_TRUE);
            index++;
        }
    }

    public void openUpgrades(Player player, BlockMenu minerMenu, Location miner) {
        final ChestMenu menu = setupMenu("Upgrades", 2);

        menu.addMenuClickHandler(PAGE_BACK, (o1, o2, o3, o4) -> {
            minerMenu.open(player);
            return false;
        });

        menu.addMenuClickHandler(PAGE_FORWARD, (o1, o2, o3, o4) -> {
            BlockStorage.addBlockInfo(miner, Keys.BS_LAST_MENU, "CONTROL_PANEL");
            openControlPanel(player, minerMenu, miner);
            return false;
        });

        menu.addItem(UPGRADE_SPEED, Upgrades.SPEED.getDisplay(miner), Upgrades.SPEED.getClickHandler(menu, miner));
        menu.addItem(UPGRADE_PRODUCTION, Upgrades.PRODUCTION.getDisplay(miner), Upgrades.PRODUCTION.getClickHandler(menu, miner));
        menu.addItem(UPGRADE_RELIABILITY, Upgrades.RELIABILITY.getDisplay(miner), Upgrades.RELIABILITY.getClickHandler(menu, miner));

        menu.open(player);
    }

    public void openControlPanel(Player player, BlockMenu minerMenu, Location miner) {
        final ChestMenu menu = setupMenu("Control Panel", 3);

        menu.addMenuClickHandler(PAGE_BACK, (o1, o2, o3, o4) -> {
            BlockStorage.addBlockInfo(miner, Keys.BS_LAST_MENU, "UPGRADES");
            openUpgrades(player, minerMenu, miner);
            return false;
        });

        final List<Integer> disabledCores = getDisabledCores(miner);
        addCores(miner, menu, SPEED_CORES, "Speed", "&e", disabledCores);
        addCores(miner, menu, PRODUCTION_CORES, "Production", "&b", disabledCores);
        addCores(miner, menu, RELIABILITY_CORES, "Reliability", "&d", disabledCores);

        menu.addMenuCloseHandler(o1 -> ACCESSING_CONTROL_PANEL.remove(new BlockPosition(miner)));
        menu.open(player);

        ACCESSING_CONTROL_PANEL.put(new BlockPosition(miner), player.getUniqueId());
    }

    public void addCores(Location miner, ChestMenu menu, List<Integer> cores, String type, String color, List<Integer> disabledCores) {
        int index = 1;
        for (Integer coreSlot : cores) {
            int currentIndex = index;
            menu.addItem(coreSlot, ItemStacks.core(type, color, currentIndex, !disabledCores.contains(coreSlot)));
            menu.addMenuClickHandler(coreSlot, (o1, o2, itemStack, o4) -> {
                final List<Integer> newDisabledCores = getDisabledCores(miner);
                final boolean running = itemStack.getType() == Material.LIME_STAINED_GLASS_PANE;
                if (running) {
                    newDisabledCores.add(coreSlot);
                } else {
                    newDisabledCores.remove(coreSlot);
                }


                menu.replaceExistingItem(coreSlot, ItemStacks.core(type, color, currentIndex, !running));
                setDisabledCores(miner, newDisabledCores);
                return false;
            });
            index++;
        }
    }

    public ChestMenu setupMenu(String suffix, int page) {
        final ChestMenu menu = new ChestMenu("&fMetaMiner - " + suffix);
        setupMenu(menu, page);
        return menu;
    }

    public void setupMenu(ChestMenu menu, int page) {
        Utils.drawBackground(menu, ItemStacks.SPEED_DISPLAY, SPEED_DISPLAY);
        Utils.drawBackground(menu, ItemStacks.PRODUCTION_DISPLAY, PRODUCTION_DISPLAY);
        Utils.drawBackground(menu, ItemStacks.RELIABILITY_DISPLAY, RELIABILITY_DISPLAY);

        ChestMenuUtils.drawBackground(menu, BOTTOM_BACKGROUND);

        menu.addItem(PAGE_BACK, ItemStacks.pageBack(page, 3), ChestMenuUtils.getEmptyClickHandler());
        menu.addItem(PAGE_FORWARD, ItemStacks.pageForward(page, 3), ChestMenuUtils.getEmptyClickHandler());
        menu.setPlayerInventoryClickable(true);
    }

    // Level Model Stuff

    public static void levelModel(Location miner, int level, AnimatedModelBuilder builder) {
        setModelLevel(miner, level);
        final DisplayGroup group = getGroup(miner);
        builder.build(group, miner.add(0.5, 0.5, 0.5));
    }

    // Block Storage Stuff

    public static UUID getOwner(Block miner) {
        final String uuidString = BlockStorage.getLocationInfo(miner.getLocation(), Keys.BS_OWNER);
        if (uuidString == null) {
            return null;
        }

        try {
            return UUID.fromString(uuidString);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void setMalfunctionLevel(Location miner, int level) {
        BlockStorage.addBlockInfo(miner, Keys.BS_MALFUNCTION_LEVEL, String.valueOf(level));
    }

    public static int getMalfunctionLevel(Location miner) {
        try {
            return Integer.parseInt(BlockStorage.getLocationInfo(miner, Keys.BS_MALFUNCTION_LEVEL));
        } catch (Exception ignored) {
            BlockStorage.addBlockInfo(miner, Keys.BS_MALFUNCTION_LEVEL, "0");
        }
        return 0;
    }

    public static void setModelLevel(Location miner, int level) {
        BlockStorage.addBlockInfo(miner, Keys.BS_MODEL_LEVEL, String.valueOf(level));
    }

    public static int getModelLevel(Location miner) {
        try {
            return Integer.parseInt(BlockStorage.getLocationInfo(miner, Keys.BS_MODEL_LEVEL));
        } catch (Exception ignored) {
            BlockStorage.addBlockInfo(miner, Keys.BS_MALFUNCTION_LEVEL, "1");
        }
        return 1;
    }

    public static void setDisabledCores(Location miner, List<Integer> disabledCores) {
        final StringBuilder cores = new StringBuilder();
        for (int coreSlot : disabledCores) {
            if (!cores.isEmpty()) {
                cores.append(",");
            }
            cores.append(coreSlot);
        }
        BlockStorage.addBlockInfo(miner, Keys.BS_DISABLED_CORES, cores.toString());
    }

    public static List<Integer> getDisabledCores(Location miner) {
        final String coresString = BlockStorage.getLocationInfo(miner, Keys.BS_DISABLED_CORES);
        if (coresString == null) {
            return new ArrayList<>();
        }

        final String[] cores = coresString.split(",");
        final List<Integer> disabledCores = new ArrayList<>();
        for (String core : cores) {
            try {
                disabledCores.add(Integer.parseInt(core));
            } catch (Exception ignored) {}
        }

        return disabledCores;
    }

    // Display Model Block Stuff

    @Override
    public Vector getBuildOffset() {
        return new Vector(0.5, 0.5, 0.5);
    }

    @Override
    @Nonnull
    public Material getBaseMaterial() {
        return Material.BEACON;
    }

    @Override
    public Material getFakeMaterial() {
        return Material.BEACON;
    }

    @Override
    public ModelBuilder getDisplayModel() {
        return new ModelBuilder();
    }

    @Override
    public Display getSeat(Block block) {
        return new BlockDisplayBuilder()
                .material(Material.AIR)
                .build(block.getLocation().add(this.getBuildOffset()).add(0.0, 0.3, 0.0));
    }

    // Display Group Stuff

    @Nullable
    public static UUID getUniqueId(@Nonnull Location location) {
        String uuid = BlockStorage.getLocationInfo(location, "display-uuid");
        return uuid == null ? null : UUID.fromString(uuid);
    }

    @Nullable
    public static DisplayGroup getGroup(@Nonnull Block block) {
        return getGroup(block.getLocation());
    }

    @Nullable
    public static DisplayGroup getGroup(@Nonnull Location location) {
        UUID uuid = getUniqueId(location);
        return uuid == null ? null : DisplayGroup.fromUUID(uuid);
    }
}
