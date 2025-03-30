package org.metamechanists.metacoin;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metacoin.core.Groups;
import org.metamechanists.metacoin.core.Items;
import org.metamechanists.metacoin.core.Leaderboard;
import org.metamechanists.metacoin.implementation.commands.MetaCoinCommand;
import org.metamechanists.metacoin.implementation.commands.flags.FunnyFlags;
import org.metamechanists.metacoin.implementation.compat.PapiIntegration;
import org.metamechanists.metacoin.implementation.listeners.ProjectileListener;
import org.metamechanists.metacoin.implementation.listeners.MinerListeners;
import org.metamechanists.metacoin.utils.Language;

import javax.annotation.Nonnull;

public final class MetaCoin extends JavaPlugin implements SlimefunAddon {
    private static @Getter MetaCoin instance;

    @Override
    public void onEnable() {
        instance = this;

        Leaderboard.init();
        Language.init();
        Groups.init();
        Items.init();

        new PapiIntegration();

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new MinerListeners(), this);
        manager.registerEvents(new ProjectileListener(), this);
    }

    private void registerCommands() {
        final PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");
        manager.registerCommand(new MetaCoinCommand());

        saveDefaultConfig();

        if (getServer().getPluginManager().isPluginEnabled("WorldEditSlimefun")) {
            FunnyFlags.init();
        }
    }

    @Override
    public void onDisable() {}

    @Nonnull @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull @Override
    public String getBugTrackerURL() {
        return "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    }
}
