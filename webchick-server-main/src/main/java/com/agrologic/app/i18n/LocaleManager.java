package com.agrologic.app.i18n;

import java.util.*;

public class LocaleManager {
    private Map<String, String> dictionary;
    private Map<String, Locale> localeMap;
    private Language currentLanguage;
    private static final Language defaultLanguage = Language.ENGLISH;
    public static final String UI_RESOURCE = "com/agrologic/app/i18n/UI";
    public static final String GRAPH_RESOURCE = "com/agrologic/app/i18n/GraphLabel";

    enum Language {

        ENGLISH("English"), HEBREW("Hebrew", "il", "IW"), FRENCH("French", "fr", "FR"),
        CHINESE("Chinese", "cn", "ZH"), RUSSIAN("Russian", "ru", "RU"), GERMAN("German", "de", "DE");

        Language(String language) {
            this(language, DEFAULT_LOCALE_COUNTRY, DEFAULT_LOCALE_LANGUAGE);
        }

        Language(String language, String localeCountry, String localeLanguage) {
            this.language = language;
            this.localeCountry = localeCountry;
            this.localeLanguage = localeLanguage;
        }

        public String getLanguage() {
            return language;
        }

        public String getLocaleCountry() {
            return localeCountry;
        }

        public String getLocaleLanguage() {
            return localeLanguage;
        }

        @Override
        public String toString() {
            return language;
        }

        String language;
        String localeCountry;
        String localeLanguage;
        private static final String DEFAULT_LOCALE_COUNTRY = "US";
        private static final String DEFAULT_LOCALE_LANGUAGE = "en";
    }

    public LocaleManager() {
        this(defaultLanguage);
    }

    public LocaleManager(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
        this.dictionary = new HashMap<String, String>();
        this.localeMap = new HashMap<String, Locale>();
        for (Language language : Language.values()) {
            localeMap.put(language.getLanguage(), new Locale(language.getLocaleLanguage(), language.getLocaleCountry()));
        }
    }

    public Locale getCurrentLocale() {
        return getLocale(currentLanguage.getLanguage());
    }

    public Locale getLocale(String currentLanguage) {
        return localeMap.get(currentLanguage);
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public void setCurrentLanguage(String newLanguage) {
        for (Language language : Language.values()) {
            if (language.getLanguage().equals(newLanguage)) {
                currentLanguage = language;
                break;
            }
        }
    }

    public Map getDictionary() {
        Locale locale = getLocale(currentLanguage.getLanguage());
        return getDictionary(UI_RESOURCE, locale);
    }

    public Map getDictionary(String resource, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(resource, locale);
        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            String value = bundle.getString(key);
            dictionary.put(key, value);
        }
        return dictionary;
    }
}
