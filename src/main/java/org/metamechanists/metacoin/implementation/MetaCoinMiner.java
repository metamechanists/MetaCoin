package org.metamechanists.metacoin.implementation;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import me.justahuman.furnished.displaymodellib.builders.BlockDisplayBuilder;
import me.justahuman.furnished.displaymodellib.models.ModelBuilder;
import me.justahuman.furnished.displaymodellib.models.components.ModelCuboid;
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
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metacoin.utils.Language;
import org.metamechanists.metacoin.utils.Utils;
import org.metamechanists.metalib.utils.RandomUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

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
    private static final int[] SPEED_CORES = { 9, 10, 11, 18, 19, 20, 27, 28, 29 };
    private static final int[] PRODUCTION_CORES = { 12, 13, 14, 21, 22, 23, 30, 31, 32 };
    private static final int[] RELIABILITY_CORES = { 15, 16, 17, 24, 25, 26, 33, 34, 35 };

    private static final Map<BlockPosition, Integer> PROGRESS = new HashMap<>();
    private static final Map<BlockPosition, UUID> ACCESSING_CONTROL_PANEL = new HashMap<>();
    private static final Set<BlockPosition> MALFUNCTIONING = new HashSet<>();
    private static final int TICKS_PER_PROGRESS = 4;

    public MetaCoinMiner(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[0]);

        buildPreset();
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block miner, SlimefunItem slimefunItem, Config config) {
                final int[] levels = Upgrades.getLevels(miner);
                final int[] realLevels = { levels[0] - 1, levels[1] - 1, levels[2] - 1 };
                final BlockPosition minerPosition = new BlockPosition(miner);
                if (!MALFUNCTIONING.contains(minerPosition) && RandomUtils.chance(realLevels[0] + realLevels[1] - 2 * realLevels[2])) {
                    MetaCoinMiner.this.malfunction(miner, realLevels);
                }
                MetaCoinMiner.this.tick(minerPosition, levels);
            }
        });
    }

    @Override
    public void register(@NotNull SlimefunAddon addon) {
        super.register(addon);
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
                menu.addMenuOpeningHandler(player -> {
                    BlockStorage.addBlockInfo(miner, Keys.BS_LAST_MENU, "MINER");
                    updateMenu(menu, new BlockPosition(miner));
                });

                menu.addMenuClickHandler(PAGE_FORWARD, (player, ignored1, ignored2, ignored3) -> {
                    BlockStorage.addBlockInfo(miner, Keys.BS_LAST_MENU, "UPGRADES");
                    openUpgrades(player, menu, miner);
                    return false;
                });
            }
        };
    }

    @Override
    protected void onBlockPlace(@NotNull BlockPlaceEvent event) {
        super.onBlockPlace(event);
        BlockStorage.addBlockInfo(event.getBlock(), Keys.BS_OWNER, event.getPlayer().getUniqueId().toString());
    }

    public void malfunction(Block miner, int[] levels) {
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

    public void tick(BlockPosition minerPosition, int[] levels) {
        final Location minerLocation = minerPosition.toLocation();
        final String disabledCores = BlockStorage.getLocationInfo(minerLocation, Keys.BS_DISABLED_CORES);
        final boolean malfunctioning = disabledCores != null && !disabledCores.isBlank();
        if (malfunctioning) {
            MALFUNCTIONING.add(minerPosition);
            malfunctionTick(minerLocation, levels);
        } else {
            MALFUNCTIONING.remove(minerPosition);
        }

        final int progress = PROGRESS.getOrDefault(minerPosition, 0);
        if (progress < MINER_PROGRESS.length * TICKS_PER_PROGRESS) {
            PROGRESS.put(minerPosition, progress + (malfunctioning ? 1 : levels[0]));
            updateMenu(BlockStorage.getInventory(minerLocation), minerPosition);
            return;
        }

        final BlockMenu menu = BlockStorage.getInventory(minerLocation);
        if (menu == null) {
            return;
        }

        PROGRESS.put(minerPosition, 0);
        updateMenu(menu, minerPosition);
        menu.pushItem(malfunctioning ? MetaCoinItem.withValue(1) : MetaCoinItem.fromProductionLevel(levels[1]), MINER_OUTPUT);
    }

    public void malfunctionTick(Location miner, int[] levels) {
        new ParticleBuilder(Particle.CAMPFIRE_SIGNAL_SMOKE).count(levels[0] + levels[1] - 2 * levels[2]).location(miner).offset(0.5, 0.5, 0.5).spawn();
    }

    public void updateMenu(BlockMenu menu, BlockPosition miner) {
        if (menu == null || !menu.hasViewer()) {
            return;
        }

        int index = 1;
        final int progress = PROGRESS.getOrDefault(miner, 0);
        for (int slot : MINER_PROGRESS) {
            menu.replaceExistingItem(slot, index * 4 > progress ? ItemStacks.MINER_PROGRESS_FALSE : ItemStacks.MINER_PROGRESS_TRUE);
            index++;
        }
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

        final BlockMenu minerMenu = BlockStorage.getInventory(miner);
        final String lastMenu = BlockStorage.getLocationInfo(miner.getLocation(), Keys.BS_LAST_MENU);
        if (lastMenu == null) {
            minerMenu.open(player);
            return;
        }

        switch (lastMenu) {
            case "UPGRADES" -> openUpgrades(player, minerMenu, miner);
            case "CONTROL_PANEL" -> openControlPanel(player, minerMenu, miner);
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

    public void openUpgrades(Player player, BlockMenu minerMenu, Block miner) {
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

    public void openControlPanel(Player player, BlockMenu minerMenu, Block miner) {
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

    public void addCores(Block miner, ChestMenu menu, int[] cores, String type, String color, List<Integer> disabledCores) {
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

    public UUID getOwner(Block miner) {
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

    public void setDisabledCores(Block miner, List<Integer> disabledCores) {
        final StringBuilder cores = new StringBuilder();
        for (int coreSlot : disabledCores) {
            if (!cores.isEmpty()) {
                cores.append(",");
            }
            cores.append(coreSlot);
        }
        BlockStorage.addBlockInfo(miner, Keys.BS_DISABLED_CORES, cores.toString());
    }

    public List<Integer> getDisabledCores(Block miner) {
        final String coresString = BlockStorage.getLocationInfo(miner.getLocation(), Keys.BS_DISABLED_CORES);
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
        return new ModelBuilder()
                .add("base-pillar-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(1.5F, 0.5F, 0.0F)
                        .rotation(0))
                .add("base-pillar-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(1.06F, 0.5F, 1.06F)
                        .rotation(Math.PI / 4))
                .add("base-pillar-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(0.0F, 0.5F, 1.5F)
                        .rotation(0))
                .add("base-pillar-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(-1.06F, 0.5F, 1.06F)
                        .rotation(Math.PI / 4))
                .add("base-pillar-5", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(-1.5F, 0.5F, 0.0F)
                        .rotation(0))
                .add("base-pillar-6", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(1.06F, 1.5F, -1.06F)
                        .rotation(Math.PI / 4))
                .add("base-pillar-7", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(0.0F, 0.5F, -1.5F)
                        .rotation(0))
                .add("base-pillar-8", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.0F, 0.35F)
                        .location(-1.06F, 0.5F, -1.06F)
                        .rotation(Math.PI / 4))

                .add("pillar-top-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.0F, 1.0F, 1.0F)
                        .location(0, 0.5F, 0)
                        .rotation(0))
                .add("pillar-top-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.0F, 1.0F, 1.0F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI / 4))
                .add("pillar-top-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.0F, 1.0F, 1.0F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI / 2))
                .add("pillar-top-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.0F, 1.0F, 1.0F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI * 3 / 4));
    }

    @Override
    public Display getSeat(Block block) {
        return new BlockDisplayBuilder()
                .material(Material.AIR)
                .build(block.getLocation().add(this.getBuildOffset()).add(0.0, 0.3, 0.0));
    }

    @Getter
    public enum Upgrades {
        SPEED(64),
        PRODUCTION(192) {
            @Override
            public long getCost(int currentLevel) {
                if (currentLevel < 65) {
                    return (long) Math.ceil(Math.pow(2, 0.2 * currentLevel) - 1 / (double) currentLevel);
                }

                if (currentLevel < 129) {
                    return (long) Math.ceil(Math.pow(64, 1 + (1.5 * currentLevel) / 64));
                }

                return (long) Math.ceil(Math.pow(64 * 64, currentLevel / 60D));
            }
        },
        RELIABILITY(256);

        private final int maxLevel;

        Upgrades(int maxLevel) {
            this.maxLevel = maxLevel;
        }

        public ItemStack getDisplay(Block miner) {
            return switch (this) {
                case SPEED -> ItemStacks.speedUpgrade(getCost(miner), getLevel(miner), getMaxLevel());
                case PRODUCTION -> ItemStacks.productionUpgrade(getCost(miner), getLevel(miner), getMaxLevel());
                case RELIABILITY -> ItemStacks.reliabilityUpgrade(getCost(miner), getLevel(miner), getMaxLevel());
            };
        }

        public ChestMenu.MenuClickHandler getClickHandler(ChestMenu menu, Block miner) {
            return (player, i, o2, o3) -> {
                final int level = getLevel(miner);
                if (level >= getMaxLevel()) {
                    Language.sendMessage(player, "miner.upgrade.max-level");
                    return false;
                }

                final long cost = getCost(miner);
                final long playerValue = MetaCoinItem.getTotalCoinValue(player);
                if (playerValue < cost) {
                    Language.sendFormatted(player, "miner.upgrade.too-expensive", playerValue, cost);
                    return false;
                }

                final long removableValue = MetaCoinItem.getRemovableCoinValue(player, cost);
                if (removableValue < cost) {
                    Language.sendFormatted(player, "miner.upgrade.cant-remove", removableValue, cost);
                    return false;
                }

                MetaCoinItem.removeCoins(player, cost);
                setLevel(miner, level + 1);
                menu.replaceExistingItem(i, getDisplay(miner));
                return false;
            };
        }

        public long getCost(Block miner) {
            return getCost(getLevel(miner));
        }

        public long getCost(int currentLevel) {
            return currentLevel;
        }

        public int getLevel(Block miner) {
            try {
                return Integer.parseInt(BlockStorage.getLocationInfo(miner.getLocation(), name()));
            } catch (Exception ignored) {
                BlockStorage.addBlockInfo(miner, name(), "1");
                return 1;
            }
        }

        public void setLevel(Block miner, int level) {
            BlockStorage.addBlockInfo(miner, name(), String.valueOf(level));
        }

        public static int[] getLevels(Block miner) {
            return new int[] { SPEED.getLevel(miner), PRODUCTION.getLevel(miner), RELIABILITY.getLevel(miner) };
        }
    }
}
