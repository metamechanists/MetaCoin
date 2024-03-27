package org.metamechanists.metacoin.api;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import me.justahuman.furnished.displaymodellib.models.components.ModelComponent;
import me.justahuman.furnished.displaymodellib.sefilib.entity.display.DisplayGroup;
import me.justahuman.furnished.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnimatedModelBuilder {
    @Getter
    private final List<AnimatedModelStage> stages = new ArrayList<>();
    private AnimatedModelStage currentStage = new AnimatedModelStage();

    public AnimatedModelBuilder() {}

    public AnimatedModelBuilder(AnimatedModelBuilder oldBuilder) {
        currentStage = oldBuilder.currentStage;
        currentStage.interpolation(0);
        currentStage.duration(0);
    }

    public AnimatedModelBuilder interpolation(int interpolation) {
        currentStage.interpolation(interpolation);
        return this;
    }

    public AnimatedModelBuilder duration(int duration) {
        currentStage.duration(duration);
        return this;
    }

    public AnimatedModelBuilder add(@NotNull String name, @NotNull ModelComponent component) {
        currentStage.add(name, component);
        return this;
    }

    public AnimatedModelBuilder addAll(Map<String, ModelComponent> components) {
        currentStage.addAll(components);
        return this;
    }

    public AnimatedModelBuilder nextStage() {
        return nextStage(false);
    }

    public AnimatedModelBuilder nextStage(boolean retainComponents) {
        stages.add(currentStage);
        currentStage = retainComponents ? new AnimatedModelStage(currentStage.components()) : new AnimatedModelStage();
        return this;
    }

    public void build(DisplayGroup group, Location location) {
        if (!currentStage.equals(new AnimatedModelStage())) {
            stages.add(currentStage);
            currentStage = new AnimatedModelStage();
        }

        if (stages.isEmpty()) {
            return;
        }

        AnimatedModelStage previousStage = stages.remove(0);
        scheduleStage(previousStage, group, location, 0);

        for (AnimatedModelStage stage : stages) {
            scheduleStage(stage, group, location, previousStage.duration());
            previousStage = stage;
        }
    }

    private void scheduleStage(AnimatedModelStage stage, DisplayGroup group, Location location, int delay) {
        Slimefun.runSync(() -> {
            if (location.getBlock().isEmpty() || !group.getParentDisplay().isValid()) {
                return;
            }

            Map<String, Display> displays = group.getDisplays();
            Map<String, ModelComponent> components = stage.components();

            for (String name : displays.keySet()) {
                ModelComponent component = components.get(name);
                Display display = displays.get(name);
                if (component == null || display == null) {
                    Optional.ofNullable(display).ifPresent(Entity::remove);
                    continue;
                }

                Matrix4f matrix = component.getMatrix();
                if (!matrix.equals(Utils.transformationToMatrix(display.getTransformation()))) {
                    display.setInterpolationDelay(-1);
                    display.setInterpolationDuration(stage.interpolation());
                    display.setTransformationMatrix(matrix);
                }
            }

            for (Map.Entry<String, ModelComponent> entry : components.entrySet()) {
                if (!displays.containsKey(entry.getKey())) {
                    final Display display = entry.getValue().build(location);
                    display.setInterpolationDelay(-1);
                    display.setInterpolationDuration(stage.interpolation());
                    group.addDisplay(entry.getKey(), display);
                }
            }
        }, delay);
    }
}