package org.metamechanists.metacoin.api;

import me.justahuman.furnished.displaymodellib.models.components.ModelComponent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AnimatedModelStage {
    private final Map<String, ModelComponent> components = new HashMap<>();
    private int interpolation;
    private int duration;

    public AnimatedModelStage() {
        this(new HashMap<>(), 0, 0);
    }

    public AnimatedModelStage(Map<String, ModelComponent> components) {
        this(components, 0, 0);
    }

    public AnimatedModelStage(Map<String, ModelComponent> components, int interpolation, int duration) {
        this.components.putAll(components);
        this.interpolation = interpolation;
        this.duration = duration;
    }

    public void interpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    public void duration(int duration) {
        this.duration = duration;
    }

    public void add(@NotNull String name, @NotNull ModelComponent component) {
        this.components.put(name, component);
    }

    public void addAll(@NotNull Map<String, ModelComponent> components) {
        this.components.putAll(components);
    }

    public int interpolation() {
        return this.interpolation;
    }

    public int duration() {
        return this.duration;
    }

    public Map<String, ModelComponent> components() {
        return this.components;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnimatedModelStage otherStage && this.duration == otherStage.duration && this.interpolation == otherStage.interpolation && this.components.equals(otherStage.components);
    }
}
