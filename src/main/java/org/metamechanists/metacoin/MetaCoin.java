package org.metamechanists.metacoin;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metacoin.core.Groups;
import org.metamechanists.metacoin.core.Items;
import org.metamechanists.metacoin.implementation.commands.CompressCommand;
import org.metamechanists.metacoin.implementation.commands.MinerCommand;
import org.metamechanists.metacoin.implementation.commands.SlagCommand;
import org.metamechanists.metacoin.implementation.listeners.BlockListeners;
import org.metamechanists.metacoin.utils.Language;

import javax.annotation.Nonnull;

public final class MetaCoin extends JavaPlugin implements SlimefunAddon {
    private static @Getter MetaCoin instance;

    @Override
    public void onEnable() {
        instance = this;

        Language.init();
        Groups.init();
        Items.init();

        registerListeners();
        registerRunnables();
        registerCommands();
    }

    private void registerListeners() {
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BlockListeners(), this);
    }

    private void registerRunnables() {
        // Register any runnables here!
    }

    private void registerCommands() {
        final PaperCommandManager manager = new PaperCommandManager(this);
        final CommandCompletions<BukkitCommandCompletionContext> completions = manager.getCommandCompletions();
        manager.enableUnstableAPI("help");
        manager.registerCommand(new MinerCommand());
        manager.registerCommand(new CompressCommand());
        manager.registerCommand(new SlagCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public String getBugTrackerURL() {
        return "discord.metamechanists.org";
    }
}
