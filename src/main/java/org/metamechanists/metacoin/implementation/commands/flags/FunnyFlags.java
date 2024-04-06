package org.metamechanists.metacoin.implementation.commands.flags;

import dev.j3fftw.worldeditslimefun.acf.BukkitCommandCompletionContext;
import dev.j3fftw.worldeditslimefun.commands.flags.CommandFlag;
import dev.j3fftw.worldeditslimefun.commands.flags.CommandFlags;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.implementation.runnables.WarrantyVoidRunnable;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinMiner;

import java.util.Collection;
import java.util.List;

public class FunnyFlags {
    public static void init() {
        CommandFlags.addFlagType("--void_warranty", new VoidWarrantyFlag());
    }

    public static class VoidWarrantyFlag extends CommandFlag<Boolean> {
        @Override
        public void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block) {
            new WarrantyVoidRunnable(player, block, ((MetaCoinMiner) sfItem).getDisplayGroup(block));
        }

        @Override
        public boolean canApply(SlimefunItem sfItem) {
            return this.value && sfItem instanceof MetaCoinMiner;
        }

        @Override
        public Collection<String> getTabSuggestions(BukkitCommandCompletionContext context) {
            return List.of("true", "false");
        }

        @Override
        public VoidWarrantyFlag ofValue(String value) {
            return (VoidWarrantyFlag) new VoidWarrantyFlag().setValue(Boolean.parseBoolean(value));
        }
    }
}
