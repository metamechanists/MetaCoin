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
    public @NotNull String getIdentifier() {
        return "MetaCoin™";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MetaMechanists";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String placeholder) {
        if (placeholder.equals("#")) {
            return offlinePlayer != null
                ? Leaderboard.getPosition(offlinePlayer.getUniqueId())
                : "-1";
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
}
