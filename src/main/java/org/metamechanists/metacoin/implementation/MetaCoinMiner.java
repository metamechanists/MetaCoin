package org.metamechanists.metacoin.implementation;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
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
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.metamechanists.metacoin.utils.Utils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final int[] ALL_CORES = IntStream.range(9, 35).toArray();
    private static final int[] SPEED_CORES = IntStream.range(9, 17).toArray();
    private static final int[] PRODUCTION_CORES = IntStream.range(18, 26).toArray();
    private static final int[] RELIABILITY_CORES = IntStream.range(27, 35).toArray();

    private static final Map<BlockPosition, Integer> PROGRESS = new HashMap<>();
    private static final int TICKS_PER_PROGRESS = 4;

    public MetaCoinMiner(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[0]);

        buildPreset();
        addItemHandler(new BlockTicker() {
            private int tick = 1;

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                MetaCoinMiner.this.tick(block, config);
            }

            @Override
            public void uniqueTick() {
                // TODO: Add malfunctions based on this variable
                tick++;
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
                    BlockStorage.addBlockInfo(miner, "LAST_MENU", "MINER");
                    updateMenu(menu, new BlockPosition(miner));
                });

                menu.addMenuClickHandler(PAGE_FORWARD, (player, ignored1, ignored2, ignored3) -> {
                    BlockStorage.addBlockInfo(miner, "LAST_MENU", "UPGRADES");
                    openUpgrades(player, menu, miner);
                    return false;
                });
            }
        };
    }

    @Override
    protected void onBlockPlace(@NotNull BlockPlaceEvent event) {
        BlockStorage.addBlockInfo(event.getBlock(), "OWNER", event.getPlayer().getUniqueId().toString());
    }

    public void tick(Block miner, Config config) {
        final BlockPosition position = new BlockPosition(miner);
        final int progress = PROGRESS.getOrDefault(position, 0);
        if (progress <= MINER_PROGRESS.length * TICKS_PER_PROGRESS) {
            PROGRESS.put(position, progress + Upgrades.SPEED.getLevel(miner));
            updateMenu(BlockStorage.getInventory(miner), position);
            return;
        }

        final BlockMenu menu = BlockStorage.getInventory(miner);
        if (menu == null) {
            return;
        }

        PROGRESS.put(position, 0);
        updateMenu(menu, position);
        menu.pushItem(MetaCoinItem.fromProductionLevel(Upgrades.PRODUCTION.getLevel(miner)), MINER_OUTPUT);
    }

    public void updateMenu(BlockMenu menu, BlockPosition miner) {
        if (menu == null || !menu.hasViewer()) {
            return;
        }

        int index = 1;
        final int progress = PROGRESS.getOrDefault(miner, 0);
        for (int slot : MINER_PROGRESS) {
            menu.replaceExistingItem(slot, index * 4 >= progress ? ItemStacks.MINER_PROGRESS_FALSE : ItemStacks.MINER_PROGRESS_TRUE);
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
            player.sendMessage(ChatColor.RED + "This isn't your miner!");
            return;
        }

        final BlockMenu minerMenu = BlockStorage.getInventory(miner);
        final String lastMenu = BlockStorage.getLocationInfo(miner.getLocation(), "LAST_MENU");
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

    @ParametersAreNonnullByDefault
    protected void onBlockBreak(BlockBreakEvent event, ItemStack itemStack, List<ItemStack> drops) {

    }

    public void openUpgrades(Player player, BlockMenu minerMenu, Block miner) {
        final ChestMenu menu = setupMenu("Upgrades", 2);

        menu.addMenuClickHandler(PAGE_BACK, (o1, o2, o3, o4) -> {
            minerMenu.open(player);
            return false;
        });

        menu.addMenuClickHandler(PAGE_FORWARD, (o1, o2, o3, o4) -> {
            BlockStorage.addBlockInfo(miner, "LAST_MENU", "CONTROL_PANEL");
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
            BlockStorage.addBlockInfo(miner, "LAST_MENU", "UPGRADES");
            openUpgrades(player, minerMenu, miner);
            return false;
        });

        final List<Integer> disabledCores = getDisabledCores(miner);
        addCores(menu, SPEED_CORES, "Speed", "&e", disabledCores);
        addCores(menu, PRODUCTION_CORES, "Production", "&b", disabledCores);
        addCores(menu, RELIABILITY_CORES, "Reliability", "&d", disabledCores);

        menu.addMenuCloseHandler(o1 -> {
           final StringBuilder cores = new StringBuilder();
           for (int coreSlot : ALL_CORES) {
               if (menu.getItemInSlot(coreSlot).getType() == Material.RED_STAINED_GLASS_PANE) {
                   if (!cores.isEmpty()) {
                       cores.append(",");
                   }
                   cores.append(coreSlot);
               }
           }
           BlockStorage.addBlockInfo(miner, "DISABLED_CORES", cores.toString());
        });

        menu.open(player);
    }

    public void addCores(ChestMenu menu, int[] cores, String type, String color, List<Integer> disabledCores) {
        int index = 0;
        for (int coreSlot : cores) {
            int currentIndex = index;
            menu.addItem(coreSlot, ItemStacks.core(type, color, currentIndex, disabledCores.contains(coreSlot)));
            menu.addMenuClickHandler(coreSlot, (o1, o2, itemStack, o4) -> {
                final boolean offline = itemStack.getType() == Material.RED_STAINED_GLASS_PANE;
                menu.replaceExistingItem(coreSlot, ItemStacks.core(type, color, currentIndex, !offline));
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
    }

    public UUID getOwner(Block miner) {
        final String uuidString = BlockStorage.getLocationInfo(miner.getLocation(), "OWNER");
        if (uuidString == null) {
            return null;
        }

        try {
            return UUID.fromString(uuidString);
        } catch (Exception ignored) {
            return null;
        }
    }

    public List<Integer> getDisabledCores(Block miner) {
        final String coresString = BlockStorage.getLocationInfo(miner.getLocation(), "DISABLED_CORES");
        if (coresString == null) {
            return new ArrayList<>();
        }

        final String[] cores = coresString.split(",");
        final List<Integer> disabledCores = new ArrayList<>();
        for (String core : cores) {
            try {
                disabledCores.add(Integer.parseInt(core));
            } catch (Exception ignored) {};
        }

        return disabledCores;
    }

    @Override
    public Vector getBuildOffset() {
        return new Vector(0.5, 0.5, 0.5);
    }

    @Nonnull
    public Material getBaseMaterial() {
        return Material.BARRIER;
    }

    @Override
    public Material getFakeMaterial() {
        return Material.IRON_BLOCK;
    }

    @Override
    public ModelBuilder getDisplayModel() {
        return new ModelBuilder()
                .add("base-octagon-bottom-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, -0.25F, 0.0F)
                        .rotation(Math.PI / 2))
                .add("base-octagon-bottom-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, -0.25F, 0.0F)
                        .rotation(Math.PI))
                .add("base-octagon-bottom-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, -0.25F, 0.0F)
                        .rotation(Math.PI))
                .add("base-octagon-bottom-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, -0.25F, 0.0F)
                        .rotation(Math.PI * 3 / 2))

                .add("base-octagon-top-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, 0.25F, 0.0F)
                        .rotation(Math.PI / 2))
                .add("base-octagon-top-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, 0.25F, 0.0F)
                        .rotation(Math.PI))
                .add("base-octagon-top-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, 0.25F, 0.0F)
                        .rotation(Math.PI))
                .add("base-octagon-top-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.GRAY_CONCRETE)
                        .size(2.0F, 0.5F, 1.0F)
                        .location(0.0F, 0.25F, 0.0F)
                        .rotation(Math.PI * 3 / 2));
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
                    player.sendMessage(ChatColor.RED + "That upgrade is already max level!");
                    return false;
                }

                final long cost = getCost(miner);
                final long playerValue = MetaCoinItem.getTotalCoinValue(player);
                if (playerValue < cost) {
                    player.sendMessage(ChatColor.RED + "You don't have enough coins! (%,d/%,d)".formatted(playerValue, cost));
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
    }
}
