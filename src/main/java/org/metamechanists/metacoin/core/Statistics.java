package org.metamechanists.metacoin.core;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.metamechanists.metacoin.MetaCoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Statistics {
    private static YamlConfiguration statistics;

    public static void init() {
        final MetaCoin instance = MetaCoin.getInstance();
        final Logger logger = instance.getLogger();
        final File file = new File(instance.getDataFolder(), "statistics.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                logger.severe("Could not create the statistics.yml file!");
                logger.severe(e.getLocalizedMessage());
                return;
            }
        }

        statistics = YamlConfiguration.loadConfiguration(file);
    }

    public static void addMetaMiner(Player player) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection metaMiners = getMetaMiners();
        ConfigurationSection metaMiner = metaMiners.getConfigurationSection(uuid);
        if (metaMiner == null) {
            metaMiner = metaMiners.createSection(uuid);
            metaMiner.set("speed", 0);
            metaMiner.set("production", 0);
            metaMiner.set("reliability", 0);
        }
    }

    public static void updateUpgrades(Player player, int[] levels) {
        String uuid = player.getUniqueId().toString();
        ConfigurationSection metaMiner = getMetaMiners().getConfigurationSection(uuid);
        if (metaMiner != null) {
            metaMiner.set("speed", levels[0]);
            metaMiner.set("production", levels[1]);
            metaMiner.set("reliability", levels[2]);
        }
    }

    public static List<Integer> getAllUpgradeLevels(String type) {
        List<Integer> levels = new ArrayList<>();
        ConfigurationSection metaMiners = getMetaMiners();
        for (String uuid : metaMiners.getKeys(false)) {
            ConfigurationSection metaMiner = metaMiners.getConfigurationSection(uuid);
            if (metaMiner != null && metaMiner.isInt(type)) {
                levels.add(metaMiner.getInt(type));
            }
        }
        return levels;
    }

    public static void addVoidedWarranty(Player player) {
        String uuid = player.getUniqueId().toString();
        List<String> voidedWarranties = getVoidedWarranties();
        if (!voidedWarranties.contains(uuid)) {
            voidedWarranties.add(uuid);
            statistics.set("voided_warranties", voidedWarranties);
        }
    }

    public static void addMinedMetaCoins(long amount) {
        long total = statistics.getLong("mined_metacoins", 0);
        total += amount;
        statistics.set("mined_metacoins", total);
    }

    public static long getMinedMetaCoins() {
        return statistics.getLong("mined_metacoins", 0);
    }

    public static ConfigurationSection getMetaMiners() {
        return statistics.isConfigurationSection("meta_miners")
            ? statistics.getConfigurationSection("meta_miners")
            : statistics.createSection("meta_miners");
    }

    public static List<String> getVoidedWarranties() {
        return statistics.getStringList("voided_warranties");
    }

    public static void save() {
        final MetaCoin instance = MetaCoin.getInstance();
        final Logger logger = instance.getLogger();
        final File file = new File(instance.getDataFolder(), "statistics.yml");
        try {
            statistics.save(file);
        } catch (Exception e) {
            logger.severe("Could not save statistics.yml!");
            logger.severe(e.getLocalizedMessage());
        }
    }
}
