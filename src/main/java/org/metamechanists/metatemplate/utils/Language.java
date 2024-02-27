package org.metamechanists.metatemplate.utils;

import org.metamechanists.metalib.language.LanguageStorage;
import org.metamechanists.metatemplate.MetaTemplate;

public class Language {
    private static LanguageStorage languageStorage;

    public static void initialize() {
        languageStorage = new LanguageStorage(MetaTemplate.getInstance());
    }

    @SafeVarargs
    public static String getLanguageEntry(String path, Object... args) {
        return languageStorage.getLanguageEntry(path, args);
    }
}
