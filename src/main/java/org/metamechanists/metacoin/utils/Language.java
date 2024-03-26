package org.metamechanists.metacoin.utils;

import org.bukkit.entity.Player;
import org.metamechanists.metacoin.MetaCoin;
import org.metamechanists.metalib.language.LanguageStorage;

public class Language {
    private static LanguageStorage languageStorage;

    public static void init() {
        languageStorage = new LanguageStorage(MetaCoin.getInstance());
    }

    @SafeVarargs
    public static void sendMessage(Player player, String path, Object... placeholders) {
        player.sendMessage(getLanguageEntry(path, placeholders));
    }

    @SafeVarargs
    public static void sendFormatted(Player player, String path, Object... args) {
        player.sendMessage(getLanguageEntry(path).formatted(args));
    }

    @SafeVarargs
    public static String getLanguageEntry(String path, Object... placeholders) {
        return languageStorage.getLanguageEntry(path, placeholders);
    }
}
