package org.metamechanists.metacoin.implementation;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.justahuman.furnished.displaymodellib.builders.BlockDisplayBuilder;
import me.justahuman.furnished.displaymodellib.models.ModelBuilder;
import me.justahuman.furnished.displaymodellib.models.components.ModelCuboid;
import me.justahuman.furnished.implementation.furniture.absraction.DisplayModelBlock;
import me.justahuman.furnished.implementation.furniture.interfaces.Sittable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MetaCoinMiner extends DisplayModelBlock implements Sittable {
    public MetaCoinMiner(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[0]);

        buildPreset();
        addItemHandler(onBlockPlace(), onBlockTick(), onBlockBreak());
    }

    @Override
    public void register(@NotNull SlimefunAddon addon) {
        super.register(addon);
        setHidden(true);
    }

    public void buildPreset() {
        new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                // # # # # # # # # #
                // # # # # # # # # #
                // # # # # # # # # #
                // # # # # # # # # #
                // # # # # # # # # #
                // # # # # # # # # #
            }

            @Override
            @ParametersAreNonnullByDefault
            public boolean canOpen(Block block, Player player) {
                return Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }
        };
    }

    public BlockPlaceHandler onBlockPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {

            }
        };
    }

    public BlockTicker onBlockTick() {
        return new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {

            }
        };
    }

    public BlockBreakHandler onBlockBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent blockBreakEvent, ItemStack itemStack, List<ItemStack> list) {

            }
        };
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
}
