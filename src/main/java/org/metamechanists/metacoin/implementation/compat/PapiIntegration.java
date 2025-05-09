package org.metamechanists.metacoin.implementation.compat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metacoin.core.Leaderboard;

public class PapiIntegration extends PlaceholderExpansion {
    public PapiIntegration() {
        register();
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String placeholder) {
        if (placeholder.equals("position")) {
            return offlinePlayer != null
                ? Leaderboard.getPosition(offlinePlayer.getUniqueId())
                : "-1";
        } else if (placeholder.equals("value")) {
            return offlinePlayer != null
                ? "%,d".formatted(Leaderboard.getValue(offlinePlayer.getUniqueId()))
                : "0";
        }

        if (placeholder.contains("_value")) {
            final String position = placeholder.substring(0, placeholder.indexOf("_value"));
            try {
                return Leaderboard.getValueAt(Integer.parseInt(position));
            } catch (Exception ignored) {
                return "";
            }
        }

        try {
            return Leaderboard.getPlayersAt(Integer.parseInt(placeholder));
        } catch (Exception ignored) {
            return "";
        }
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "MetaCoin™";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JustAHuman, ADankCoconut, Idra";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
}
