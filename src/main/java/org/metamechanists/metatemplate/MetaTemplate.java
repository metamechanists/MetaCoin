package org.metamechanists.metatemplate;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metatemplate.core.Groups;
import org.metamechanists.metatemplate.core.Items;

import javax.annotation.Nonnull;

public final class MetaTemplate extends JavaPlugin implements SlimefunAddon {
    private static @Getter MetaTemplate instance;

    @Override
    public void onEnable() {
        instance = this;

        Groups.init();
        Items.init();

        registerListeners();
        registerRunnables();
        registerCommands();
    }

    private void registerListeners() {
        final PluginManager manager = getServer().getPluginManager();
        // Register any listeners here!
    }

    private void registerRunnables() {
        // Register any runnables here!
    }

    private void registerCommands() {
        final PaperCommandManager manager = new PaperCommandManager(this);
        final CommandCompletions<BukkitCommandCompletionContext> completions = manager.getCommandCompletions();
        manager.enableUnstableAPI("help");
        // Register any commands here!
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
