package org.metamechanists.metacoin;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.BlobBuildUpdater;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metacoin.core.Groups;
import org.metamechanists.metacoin.core.Items;
import org.metamechanists.metacoin.core.Leaderboard;
import org.metamechanists.metacoin.core.Statistics;
import org.metamechanists.metacoin.implementation.commands.MetaCoinCommand;
import org.metamechanists.metacoin.implementation.commands.flags.FunnyFlags;
import org.metamechanists.metacoin.implementation.compat.PapiIntegration;
import org.metamechanists.metacoin.implementation.listeners.ProjectileListener;
import org.metamechanists.metacoin.implementation.listeners.MinerListeners;
import org.metamechanists.metacoin.utils.Language;

import javax.annotation.Nonnull;
import java.util.List;

public final class MetaCoin extends JavaPlugin implements SlimefunAddon {
    private static @Getter MetaCoin instance;

    @Override
    public void onEnable() {
        instance = this;

        Statistics.init();
        Leaderboard.init();
        Language.init();
        Groups.init();
        Items.init();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiIntegration();
        }

        saveDefaultConfig();

        if (getConfig().getBoolean("auto-update") && getDescription().getVersion().startsWith("DEV")) {
            new BlobBuildUpdater(this, getFile(), "MetaCoin").start();
        }
        
        Metrics metrics = new Metrics(this, 25310);
        metrics.addCustomChart(new SingleLineChart("global_mined_metacoins", () -> (int) Math.min(Integer.MAX_VALUE, Statistics.getMinedMetaCoins())));
        metrics.addCustomChart(new SingleLineChart("global_unsafe_mined_metacoins", () -> (int) Statistics.getMinedMetaCoins()));
        metrics.addCustomChart(new SingleLineChart("global_mined_metacoins_millions", () -> (int) Math.min(Integer.MAX_VALUE, Statistics.getMinedMetaCoins() / 1_000_000)));
        metrics.addCustomChart(new SingleLineChart("global_acquired_metaminers", () -> Statistics.getMetaMiners().getKeys(false).size()));
        metrics.addCustomChart(new SingleLineChart("global_voided_warranties", () -> Statistics.getVoidedWarranties().size()));
        metrics.addCustomChart(new SimplePie("top_of_server_leaderboard", () -> Leaderboard.getValueAt(1)));
        addUpgradeGraphs(metrics, "speed");
        addUpgradeGraphs(metrics, "production");
        addUpgradeGraphs(metrics, "reliability");
        
        registerListeners();
        registerCommands();
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Statistics::save, 20 * 60L, 20 * 60 * 10L);
    }
    
    private void addUpgradeGraphs(Metrics metrics, String upgrade) {
        metrics.addCustomChart(new SimplePie("highest_" + upgrade + "_upgrade", () -> Statistics.getAllUpgradeLevels(upgrade).stream().max(Integer::compareTo).orElse(0).toString()));
        metrics.addCustomChart(new SimplePie("average_" + upgrade + "_upgrade", () -> String.valueOf(Statistics.getAllUpgradeLevels(upgrade).stream().mapToInt(Integer::intValue).average().orElse(0))));
        metrics.addCustomChart(new SimplePie("median_" + upgrade + "_upgrade", () -> {
            List<Integer> levels = Statistics.getAllUpgradeLevels(upgrade);
            levels.sort(Integer::compareTo);
            int size = levels.size();
            if (size % 2 == 0) {
                return size ==  0 ? "0" : String.valueOf(levels.get(size / 2 - 1));
            } else {
                return String.valueOf(levels.get(size / 2));
            }
        }));
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

        if (getServer().getPluginManager().isPluginEnabled("WorldEditSlimefun")) {
            FunnyFlags.init();
        }
    }

    @Override
    public void onDisable() {
        Statistics.save();
    }

    @Nonnull @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull @Override
    public String getBugTrackerURL() {
        return "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    }
}
