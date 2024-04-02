package org.metamechanists.metacoin.core;

import it.unimi.dsi.fastutil.longs.LongComparators;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.metamechanists.metacoin.MetaCoin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class Leaderboard {
    private static final List<Long> LEADERBOARD = new ArrayList<>();
    private static final Map<UUID, Long> VALUES = new HashMap<>();

    public static String getPosition(UUID uuid) {
        if (!VALUES.containsKey(uuid)) {
            return String.valueOf(LEADERBOARD.size() + 1);
        }
        return String.valueOf(LEADERBOARD.indexOf(VALUES.get(uuid)));
    }

    public static String getPlayersAt(int position) {
        if (LEADERBOARD.size() > position) {
            return "";
        }

        final long value = LEADERBOARD.get(position - 1);
        final StringBuilder players = new StringBuilder();

        for (Map.Entry<UUID, Long> entry : VALUES.entrySet()) {
            if (entry.getValue() == value) {
                if (!players.isEmpty()) {
                    players.append(", ");
                }
                players.append(Bukkit.getOfflinePlayer(entry.getKey()).getName());
            }
        }

        return players.toString();
    }

    public static long getValue(UUID uuid) {
        return VALUES.getOrDefault(uuid, 0L);
    }

    public static void updateLeaderboard(UUID uuid, long value) {
        final long oldValue = VALUES.getOrDefault(uuid, 0L);
        if (!VALUES.containsValue(oldValue)) {
            LEADERBOARD.remove(oldValue);
        }

        VALUES.put(uuid, value);
        if (!LEADERBOARD.contains(value)) {
            LEADERBOARD.add(value);
            LEADERBOARD.sort(LongComparators.NATURAL_COMPARATOR);
        }

        save();
    }

    public static void init() {
        final MetaCoin instance = MetaCoin.getInstance();
        final Logger logger = instance.getLogger();
        final File leaderboardFile = new File(instance.getDataFolder(), "leaderboard.yml");

        if (!leaderboardFile.exists()) {
            try {
                leaderboardFile.createNewFile();
            } catch (Exception e) {
                logger.severe("Could not create the leaderboard.yml file!");
                logger.severe(e.getLocalizedMessage());
                return;
            }
        }

        final YamlConfiguration leaderboard = YamlConfiguration.loadConfiguration(leaderboardFile);
        for (String uuidString : leaderboard.getKeys(false)) {
            final long value = leaderboard.getLong(uuidString, 0);
            if (value <= 0) {
                continue;
            }

            try {
                VALUES.put(UUID.fromString(uuidString), value);
                if (!LEADERBOARD.contains(value)) {
                    LEADERBOARD.add(value);
                }
            } catch (Exception e) {
                logger.severe("Could not load %s's coin value of %,d!".formatted(uuidString, value));
                logger.severe(e.getLocalizedMessage());
            }
        }

        LEADERBOARD.sort(LongComparators.NATURAL_COMPARATOR);
    }

    public static void save() {
        final YamlConfiguration leaderboard = new YamlConfiguration();
        for (UUID uuid : VALUES.keySet()) {
            leaderboard.set(uuid.toString(), VALUES.get(uuid));
        }

        final MetaCoin instance = MetaCoin.getInstance();
        final Logger logger = instance.getLogger();
        final File leaderboardFile = new File(instance.getDataFolder(), "leaderboard.yml");
        try {
            leaderboard.save(leaderboardFile);
        } catch (Exception e) {
            logger.severe("Could not save leaderboard.yml!");
            logger.severe(e.getLocalizedMessage());
        }
    }
}
