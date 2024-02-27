package org.metamechanists.metacoin.utils;

import org.metamechanists.metacoin.MetaCoin;
import org.metamechanists.metalib.language.LanguageStorage;

public class Language {
    private static LanguageStorage languageStorage;

    public static void initialize() {
        languageStorage = new LanguageStorage(MetaCoin.getInstance());
    }

    @SafeVarargs
    public static String getLanguageEntry(String path, Object... args) {
        return languageStorage.getLanguageEntry(path, args);
    }
}
