package org.metamechanists.metacoin;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.Registry;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metacoin.core.Groups;
import org.metamechanists.metacoin.core.Items;
import org.metamechanists.metacoin.core.Leaderboard;
import org.metamechanists.metacoin.implementation.commands.CompressCommand;
import org.metamechanists.metacoin.implementation.commands.DepositCommand;
import org.metamechanists.metacoin.implementation.commands.MinerCommand;
import org.metamechanists.metacoin.implementation.commands.MinerTrimCommand;
import org.metamechanists.metacoin.implementation.commands.ResetCommand;
import org.metamechanists.metacoin.implementation.commands.SlagCommand;
import org.metamechanists.metacoin.implementation.compat.PapiIntegration;
import org.metamechanists.metacoin.implementation.listeners.ProjectileListener;
import org.metamechanists.metacoin.implementation.listeners.MinerListeners;
import org.metamechanists.metacoin.utils.Language;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MetaCoin extends JavaPlugin implements SlimefunAddon {
    private static @Getter MetaCoin instance;
    private static @Getter Map<String, TrimPattern> trimPatterns = new HashMap<>();
    private static @Getter Map<String, TrimMaterial> trimMaterials = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        Leaderboard.init();
        Language.init();
        Groups.init();
        Items.init();

        new PapiIntegration();

        registerListeners();
        registerRunnables();
        registerCommands();
    }

    private void registerListeners() {
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new MinerListeners(), this);
        manager.registerEvents(new ProjectileListener(), this);
    }

    private void registerRunnables() {
        // Register any runnables here!
    }

    private void registerCommands() {
        final PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");
        manager.registerCommand(new MinerCommand());
        manager.registerCommand(new CompressCommand());
        manager.registerCommand(new DepositCommand());
        manager.registerCommand(new SlagCommand());
        manager.registerCommand(new ResetCommand());
        manager.registerCommand(new MinerTrimCommand());

        final CommandCompletions<BukkitCommandCompletionContext> completions = manager.getCommandCompletions();

        Registry.TRIM_PATTERN.stream().forEach(pattern -> {
            trimPatterns.put(pattern.getKey().getKey(), pattern);
        });
        Registry.TRIM_MATERIAL.stream().forEach(material -> {
            trimMaterials.put(material.getKey().getKey(), material);
        });
        completions.registerStaticCompletion("trim_patterns", () -> trimPatterns.keySet());
        completions.registerStaticCompletion("trim_materials", () -> trimMaterials.keySet());
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
