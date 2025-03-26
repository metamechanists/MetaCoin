package org.metamechanists.metacoin.implementation.slimefun;

import com.destroystokyo.paper.ParticleBuilder;
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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelComponent;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.metacoin.utils.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public abstract Material getFakeMaterial();
    public Material getFakeMaterial(Block block) {
        return getFakeMaterial();
    }
    public abstract ModelBuilder getDisplayModel();
    public ModelBuilder getDisplayModel(ItemStack itemStack) {
        return getDisplayModel();
    }

    public void playCrackSound(Player player, Block block) {}
    public void playBreakSound(Player player, Block block) {}

    public void playCrackingAnimation(Block block, BlockFace blockFace) {
        final double x = blockFace.getModX() / 2D;
        final double y = blockFace.getModY() / 2D;
        final double z = blockFace.getModZ() / 2D;

        new ParticleBuilder(Particle.BLOCK_CRACK)
                .data(getFakeMaterial(block).createBlockData())
                .location(block.getLocation().toCenterLocation().add(x, y, z))
                .offset(0.25 - (x / 2), 0.25 - (y / 2), 0.25 - (z / 2))
                .spawn();
    }

    public void playBreakingAnimation(Block block) {
        new ParticleBuilder(Particle.BLOCK_DUST)
                .data(getFakeMaterial(block).createBlockData())
                .location(block.getLocation().toCenterLocation())
                .offset(0.25, 0.25, 0.25)
                .count(64)
                .spawn();
    }

    protected void playBlockSound(Player player, Block block, Sound sound, float volume, float pitch) {
        player.playSound(block.getLocation(), sound, volume, pitch);
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

    protected void updateDisplayModel(Block block, DisplayGroup group, ModelBuilder builder) {
        final Map<String, ModelComponent> components = builder.getComponents();
        final Map<String, Display> displays = group.getDisplays();
        for (String name : new HashSet<>(group.getDisplays().keySet())) {
            if (!components.containsKey(name)) {
                Optional.ofNullable(group.removeDisplay(name)).ifPresent(Entity::remove);
                continue;
            }

            final ModelComponent component = components.get(name);
            if (!(component instanceof ModelCuboid cuboid)) {
                continue;
            }

            final Display display = group.getDisplays().get(name);
            final Matrix4f displayMatrix = Utils.transformationToMatrix(display.getTransformation());
            final Matrix4f matrix = cuboid.getMatrix();

            if (!displayMatrix.equals(matrix)) {
                display.setTransformationMatrix(matrix);
            }
        }

        for (Map.Entry<String, ModelComponent> component : components.entrySet()) {
            if (!displays.containsKey(component.getKey())) {
                group.addDisplay(component.getKey(), component.getValue().build(block.getLocation().add(getBuildOffset())));
            }
        }
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
