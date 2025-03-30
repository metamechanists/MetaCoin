package org.metamechanists.metacoin.implementation.slimefun;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.entity.display.DisplayInteractable;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class DisplayModelBlock extends SlimefunItem implements DisplayInteractable {
    protected DisplayModelBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);

        addItemHandler(blockPlaceHandler(), blockBreakHandler(), blockUseHandler());
    }

    protected DisplayModelBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        this(itemGroup, item, recipeType, recipe, new CustomItemStack(item, 1));
    }

    public abstract Vector getBuildOffset();
    public abstract Material getBaseMaterial();
    public abstract ModelBuilder getDisplayModel();
    public ModelBuilder getDisplayModel(ItemStack itemStack) {
        return getDisplayModel();
    }

    protected void onBlockPlace(@Nonnull BlockPlaceEvent event) {
        final Block block = event.getBlock();
        block.setType(getBaseMaterial());

        final DisplayGroup displayGroup = buildDisplayModel(event.getPlayer(), event.getItemInHand(), block);
        BlockStorage.addBlockInfo(block, "display-uuid", displayGroup.getParentUUID().toString());
    }

    @ParametersAreNonnullByDefault
    protected void onBlockBreak(BlockBreakEvent event, ItemStack itemStack, List<ItemStack> drops) {
        final Block block = event.getBlock();
        final Location location = block.getLocation();
        final DisplayGroup displayGroup = getDisplayGroup(location);
        if (displayGroup == null) {
            return;
        }

        displayGroup.remove();
    }

    protected void onBlockUse(@Nonnull PlayerRightClickEvent event) {

    }

    protected DisplayGroup buildDisplayModel(Player player, ItemStack itemStack, Block block) {
        final DisplayGroup group = new DisplayGroup(block.getLocation().toCenterLocation());
        group.getParentDisplay().setInteractionHeight(0.0F);
        group.getParentDisplay().setInteractionWidth(0.0F);

        final Location buildLocation = block.getLocation().add(getBuildOffset());
        for (Map.Entry<String, ModelComponent> entry : getDisplayModel(itemStack).getComponents().entrySet()) {
            group.addDisplay(entry.getKey(), entry.getValue().build(buildLocation));
        }

        return group;
    }

    public BlockPlaceHandler blockPlaceHandler() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                onBlockPlace(event);
            }
        };
    }

    public BlockBreakHandler blockBreakHandler() {
        return new BlockBreakHandler(false, false) {
            @Override
            @ParametersAreNonnullByDefault
            public void onPlayerBreak(BlockBreakEvent event, ItemStack itemStack, List<ItemStack> drops) {
                onBlockBreak(event, itemStack, drops);
                if (event.isCancelled()) {
                    return;
                }

                final BlockMenuPreset preset = BlockMenuPreset.getPreset(getId());
                if (preset == null) {
                    return;
                }

                final Block block = event.getBlock();
                final Location location = block.getLocation();
                final BlockMenu blockMenu = BlockStorage.getInventory(block);
                blockMenu.dropItems(location, preset.getSlotsAccessedByItemTransport(ItemTransportFlow.INSERT));
                blockMenu.dropItems(location, preset.getSlotsAccessedByItemTransport(ItemTransportFlow.WITHDRAW));
            }
        };
    }

    public BlockUseHandler blockUseHandler() {
        return this::onBlockUse;
    }

    @Nullable
    public UUID getUUID(@Nonnull Location location) {
        String uuid = BlockStorage.getLocationInfo(location, "display-uuid");
        if (uuid == null) {
            return null;
        }

        return UUID.fromString(uuid);
    }

    @Nullable
    public DisplayGroup getDisplayGroup(@Nonnull Block block) {
        return getDisplayGroup(block.getLocation());
    }

    @Nullable
    public DisplayGroup getDisplayGroup(@Nonnull Location location) {
        UUID uuid = getUUID(location);
        if (uuid == null) {
            return null;
        }

        return DisplayGroup.fromUUID(uuid);
    }
}
