package org.metamechanists.metacoin.core;

import me.justahuman.furnished.displaymodellib.models.ModelBuilder;
import me.justahuman.furnished.displaymodellib.models.components.ModelComponent;
import me.justahuman.furnished.displaymodellib.models.components.ModelCuboid;
import me.justahuman.furnished.displaymodellib.models.components.ModelDiamond;
import me.justahuman.furnished.displaymodellib.models.components.ModelItem;
import me.justahuman.furnished.displaymodellib.models.components.ModelLine;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.metacoin.api.AnimatedModelBuilder;

import java.util.Map;

import static org.metamechanists.metacoin.core.ItemStacks.META_COIN;

public class Models {
    public static AnimatedModelBuilder MINER_START_MODEL() {
        final AnimatedModelBuilder builder = new AnimatedModelBuilder();

        // First Phase of Animation, the Base
        // Grows & Spins quickly

        builder.add("base", new ModelCuboid()
                .brightness(15)
                .material(Material.GLASS)
                .size(0.3F)
                .location(0, 0, 0)
                .rotation(Math.PI))
                .interpolation(15)
                .duration(15);

        builder.nextStage().add("base", new ModelCuboid()
                .brightness(15)
                .material(Material.GLASS)
                .size(0.6F)
                .location(0, 0, 0))
                .interpolation(15)
                .duration(15);

        builder.nextStage().add("base", new ModelCuboid()
                .brightness(15)
                .material(Material.GLASS)
                .size(0.9F)
                .location(0, 0, 0)
                .rotation(Math.PI))
                .interpolation(15)
                .duration(15);

        builder.nextStage().add("base", new ModelCuboid()
                .brightness(15)
                .material(Material.GLASS)
                .size(1.2F)
                .location(0, 0, 0))
                .interpolation(15)
                .duration(15);

        // Second (Final) Stage of Animation, the Bottom Plate
        // Grows and Spins slowly

        builder.nextStage(true)
                .addAll(factorComponents(MINER_BOTTOM_PLATE(), 0.0001F, 0.0001F, 1))
                .interpolation(50)
                .duration(50);

        builder.nextStage(true)
                .addAll(MINER_BOTTOM_PLATE())
                .interpolation(50)
                .duration(50);

        return builder;
    }

    public static AnimatedModelBuilder MINER_LEVEL_2_MODEL() {
        final AnimatedModelBuilder builder = new AnimatedModelBuilder(MINER_START_MODEL());

        builder.addAll(factorComponents(MINER_TOP_PLATE(), 0.0001F, 0.0001F, 1));

        builder.nextStage(true)
                .addAll(MINER_TOP_PLATE())
                .interpolation(50)
                .duration(50);

        return builder;
    }

    public static AnimatedModelBuilder MINER_LEVEL_3_MODEL() {
        final AnimatedModelBuilder builder = new AnimatedModelBuilder(MINER_LEVEL_2_MODEL());

        builder.addAll(factorComponents(MINER_BOTTOM_SPIKE(), 0.0001F, 0.0001F, 1));

        builder.nextStage(true)
                .addAll(MINER_BOTTOM_SPIKE())
                .interpolation(50)
                .duration(50);

        return builder;
    }

    public static AnimatedModelBuilder MINER_LEVEL_4_MODEL() {
        final AnimatedModelBuilder builder = new AnimatedModelBuilder(MINER_LEVEL_3_MODEL());

        builder.addAll(factorComponents(MINER_TOP_SPIKE(), 0.0001F, 0.0001F, 1));

        builder.nextStage(true)
                .addAll(MINER_TOP_SPIKE())
                .interpolation(50)
                .duration(50);

        return builder;
    }

    public static AnimatedModelBuilder MINER_LEVEL_5_MODEL() {
        final AnimatedModelBuilder builder = new AnimatedModelBuilder(MINER_LEVEL_4_MODEL());

        builder.nextStage(true)
                .addAll(factorComponents(MINER_MIDDLE(), 0.0001F, 0.0003F, 0.5F));

        builder.nextStage(true)
                .addAll(factorComponents(MINER_MIDDLE(), 0.1F, 0.3F, 1F))
                .interpolation(10)
                .duration(10);

        builder.nextStage(true)
                .addAll(factorComponents(MINER_MIDDLE(), 1F, 3F, 0.5F))
                .interpolation(10)
                .duration(10);

        builder.nextStage(true)
                .addAll(MINER_MIDDLE_BILLBOARD()) // need to add support for getBillboard() #blame-idra
                .interpolation(50)
                .duration(50);

        return builder;
    }

    private static Map<String, ModelComponent> factorComponents(Map<String, ModelComponent> components, float scaleFactor, float locationFactor, float rotationFactor) {
        return factorComponents(components, new Vector3f(scaleFactor), new Vector3f(locationFactor), new Vector3f(rotationFactor));
    }

    private static Map<String, ModelComponent> factorComponents(Map<String, ModelComponent> components, Vector3f scaleFactor, Vector3f locationFactor, Vector3f rotationFactor) {
        for (ModelComponent component : components.values()) {
            if (component instanceof ModelCuboid cuboid) {
                cuboid.getSize().mul(scaleFactor);
                cuboid.getLocation().mul(locationFactor);
                cuboid.getRotation().mul(rotationFactor);
            } else if (component instanceof ModelItem item) {
                item.getSize().mul(scaleFactor);
                item.getLocation().mul(locationFactor);
                item.getRotation().mul(rotationFactor);
            } else if (component instanceof ModelLine line) {
                line.getFrom().mul(locationFactor);
                line.getTo().mul(locationFactor);
                line.thickness(line.getThickness() * scaleFactor.x);
                line.roll(line.getRoll() * rotationFactor.x);
            }
        }
        return components;
    }

    private static Map<String, ModelComponent> MINER_BOTTOM_PLATE() {
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
                        .location(1.06F, 0.5F, -1.06F)
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

                .add("bottom-plate-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.4F)
                        .location(0, 2.0F, 0)
                        .rotation(0))
                .add("bottom-plate-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.34F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI / 4))
                .add("bottom-plate-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.4F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI / 2))
                .add("bottom-plate-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.4F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI * 3 / 4))

                .add("bottom-plate-outside-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 2.0F, 0)
                        .rotation(0))
                .add("bottom-plate-outside-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI / 4))
                .add("bottom-plate-outside-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI / 2))
                .add("bottom-plate-outside-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 2.0F, 0)
                        .rotation(Math.PI * 3 / 4))
                .getComponents();
    }

    private static Map<String, ModelComponent> MINER_BOTTOM_SPIKE() {
        return new ModelBuilder()
                .add("bottom-slanted-pillar-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.9F, 3.5F, 0.0F)
                        .rotation(0, 0, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.64F, 3.5F, -0.64F)
                        .rotation(0, Math.PI / 4, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.0F, 3.5F, -0.9F)
                        .rotation(0, Math.PI / 2, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(-0.64F, 3.5F, -0.64F)
                        .rotation(0, Math.PI * 3 / 4, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-5", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(-0.9F, 3.5F, 0.0F)
                        .rotation(0, Math.PI, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-6", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(-0.64F, 3.5F, 0.64F)
                        .rotation(0, Math.PI * 5 / 4, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-7", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0, 3.5F, 0.9F)
                        .rotation(0, Math.PI * 3 / 2, Math.PI * 1 / 6))
                .add("bottom-slanted-pillar-8", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.64F, 3.5F, 0.64F)
                        .rotation(0, Math.PI * 7/ 4, Math.PI * 1 / 6))

                .add("bottom-shroomlight", new ModelCuboid()
                        .brightness(15)
                        .material(Material.SHROOMLIGHT)
                        .size((float) Math.sqrt(2.0))
                        .location(0.0F, 2.0F, 0.0F)
                        .rotation(new Vector3d(ModelDiamond.ROTATION)))

                .add("bottom-laser", new ModelCuboid()
                        .brightness(15)
                        .material(Material.RED_CONCRETE)
                        .size(0.1F, 2.5F, 0.1F)
                        .location(0, 4.0F, 0))
                .getComponents();
    }

    private static Map<String, ModelComponent> MINER_MIDDLE() {
        return new ModelBuilder()
                .add("metacoin", new ModelItem()
                        .brightness(15)
                        .item(META_COIN)
                        .size(2.0F)
                        .location(0, 6.0F, 0)
                        .rotation(2 * Math.PI))
                .getComponents();

    }
    private static Map<String, ModelComponent> MINER_MIDDLE_BILLBOARD() {
        return new ModelBuilder()
                .add("metacoin", new ModelItem()
                        .brightness(15)
                        .item(META_COIN)
                        .billboard(Display.Billboard.VERTICAL)
                        .size(2.0F)
                        .location(0, 6.0F, 0)
                        .rotation(2 * Math.PI))
                .getComponents();
    }

    private static Map<String, ModelComponent> MINER_TOP_SPIKE() {
        return new ModelBuilder()
                .add("top-laser", new ModelCuboid()
                        .brightness(15)
                        .material(Material.RED_CONCRETE)
                        .size(0.1F, 2.5F, 0.1F)
                        .location(0, 8.0F, 0))

                .add("top-shroomlight", new ModelCuboid()
                        .brightness(15)
                        .material(Material.SHROOMLIGHT)
                        .size((float) Math.sqrt(2.0))
                        .location(0.0F, 10.0F, 0.0F)
                        .rotation(new Vector3d(ModelDiamond.ROTATION)))

                .add("top-slanted-pillar-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.9F, 8.5F, 0.0F)
                        .rotation(0, 0, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.64F, 8.5F, -0.64F)
                        .rotation(0, Math.PI / 4, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.0F, 8.5F, -0.9F)
                        .rotation(0, Math.PI / 2, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(-0.64F, 8.5F, -0.64F)
                        .rotation(0, Math.PI * 3 / 4, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-5", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(-0.9F, 8.5F, 0.0F)
                        .rotation(0, Math.PI, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-6", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(-0.64F, 8.5F, 0.64F)
                        .rotation(0, Math.PI * 5 / 4, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-7", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0, 8.5F, 0.9F)
                        .rotation(0, Math.PI * 3 / 2, -Math.PI * 1 / 6))
                .add("top-slanted-pillar-8", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 2.5F, 0.35F)
                        .location(0.64F, 8.5F, 0.64F)
                        .rotation(0, Math.PI * 7/ 4, -Math.PI * 1 / 6))
                .getComponents();
    }

    private static Map<String, ModelComponent> MINER_TOP_PLATE() {
        return new ModelBuilder()
                .add("top-plate-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.4F)
                        .location(0, 10.0F, 0)
                        .rotation(0))
                .add("top-plate-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.34F)
                        .location(0, 10.0F, 0)
                        .rotation(Math.PI / 4))
                .add("top-plate-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.4F)
                        .location(0, 10.0F, 0)
                        .rotation(Math.PI / 2))
                .add("top-plate-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(3.4F, 1.0F, 1.4F)
                        .location(0, 10.0F, 0)
                        .rotation(Math.PI * 3 / 4))

                .add("top-plate-outside-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 10.0F, 0)
                        .rotation(0))
                .add("top-plate-outside-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 10.0F, 0)
                        .rotation(Math.PI / 4))
                .add("top-plate-outside-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 10.0F, 0)
                        .rotation(Math.PI / 2))
                .add("top-plate-outside-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.BLUE_CONCRETE)
                        .size(3.5F, 0.7F, 1.1F)
                        .location(0, 10.0F, 0)
                        .rotation(Math.PI * 3 / 4))

                .add("top-spike-pillar-1", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(0.9F, 12.7F, 0.0F)
                        .rotation(0, 0, Math.PI * 1 / 12))
                .add("top-spike-pillar-2", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(0.64F, 12.7F, -0.64F)
                        .rotation(0, Math.PI / 4, Math.PI * 1 / 12))
                .add("top-spike-pillar-3", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(0.0F, 12.7F, -0.9F)
                        .rotation(0, Math.PI / 2, Math.PI * 1 / 12))
                .add("top-spike-pillar-4", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(-0.64F, 12.7F, -0.64F)
                        .rotation(0, Math.PI * 3 / 4, Math.PI * 1 / 12))
                .add("top-spike-pillar-5", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(-0.9F, 12.7F, 0.0F)
                        .rotation(0, Math.PI, Math.PI * 1 / 12))
                .add("top-spike-pillar-6", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(-0.64F, 12.7F, 0.64F)
                        .rotation(0, Math.PI * 5 / 4, Math.PI * 1 / 12))
                .add("top-spike-pillar-7", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(0, 12.7F, 0.9F)
                        .rotation(0, Math.PI * 3 / 2, Math.PI * 1 / 12))
                .add("top-spike-pillar-8", new ModelCuboid()
                        .brightness(15)
                        .material(Material.WHITE_CONCRETE)
                        .size(0.35F, 5.0F, 0.35F)
                        .location(0.64F, 12.7F, 0.64F)
                        .rotation(0, Math.PI * 7/ 4, Math.PI * 1 / 12))
                .getComponents();
    }
}
