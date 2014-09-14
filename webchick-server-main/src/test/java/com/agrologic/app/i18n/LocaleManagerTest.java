package com.agrologic.app.i18n;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.agrologic.app.i18n.LocaleManager.Language;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocaleManagerTest {
    private static final int COUNT_LANGUAGES = 7;
    private static final int COUNT_GRAPH_LABELS = 17;
    private static final int COUNT_UI_LABELS = 17;
    private LocaleManager localeManager;

    @Before
    public void setUp() {
        localeManager = new LocaleManager();
    }

    @Test
    public void setCurrentLanguageChangingWithStringParameter() {
        for (Language language : Language.values()) {
            localeManager.setCurrentLanguage(language.getLanguage());
            assertEquals(language, localeManager.getCurrentLanguage());
        }
    }

    @Test
    public void setCurrentLanguageChangingWithEnumParameter() {
        for (Language language : Language.values()) {
            localeManager.setCurrentLanguage(language);
            assertEquals(language, localeManager.getCurrentLanguage());
        }
    }

    @Test
    public void uiLabelsPropertiesNotNullForeachLanguage() {
        for (Language language : Language.values()) {
            localeManager.setCurrentLanguage(language);
            Map<String, String> dictionary = localeManager.getDictionary();
            assertNotNull(dictionary);
        }
    }

    @Test
    public void graphLabelsPropertiesNotNullForeachLanguage() {
        String graphResource = LocaleManager.GRAPH_RESOURCE;
        for (Language language : Language.values()) {
            Locale locale = new Locale(language.getLocaleLanguage(), language.getLocaleCountry());
            Map<String, String> dictionary = localeManager.getDictionary(graphResource, locale);
            assertNotNull(dictionary);
        }
    }

    @Test
    public void loadedDictionariesIsEqualsToCreatedDictionaryForeachLanguage() {
        for (Language language : Language.values()) {
            Language actualLanguage = language;
            Map<String, String> actual = loadDictionaryUsingLocaleManager(actualLanguage);
            Map<String, String> expected = createDictionary(actualLanguage);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void isResourceBundleGetCorrectPropertiesFile() {
        for (Language language : Language.values()) {
            Locale expectedLocale = new Locale(language.getLocaleLanguage(), language.getLocaleCountry());

            // test ui resource
            String resourcePath = LocaleManager.UI_RESOURCE;
            Locale actualLocale = ResourceBundle.getBundle(resourcePath, expectedLocale).getLocale();
            assertEquals(expectedLocale, actualLocale);

            // test graph resource
            resourcePath = LocaleManager.GRAPH_RESOURCE;
            actualLocale = ResourceBundle.getBundle(resourcePath, expectedLocale).getLocale();
            assertEquals(expectedLocale, actualLocale);
        }
    }

    @Test
    public void countGraphLabelsForeachLanguage() {
        int expected = COUNT_GRAPH_LABELS;
        String graphResource = LocaleManager.GRAPH_RESOURCE;
        for (Language language : Language.values()) {
            Locale locale = new Locale(language.getLocaleLanguage(), language.getLocaleCountry());
            Map<String, String> dictionary = localeManager.getDictionary(graphResource, locale);
            assertEquals(expected, dictionary.size());
        }
    }

    @Test
    public void countUiLabelsForeachLanguage() {
        int expected = COUNT_UI_LABELS;
        String graphResource = LocaleManager.UI_RESOURCE;
        for (Language language : Language.values()) {
            Locale locale = new Locale(language.getLocaleLanguage(), language.getLocaleCountry());
            Map<String, String> dictionary = localeManager.getDictionary(graphResource, locale);
            assertEquals(expected, dictionary.size());
        }
    }

    @Test
    public void countLanguagesInSystem() {
        assertEquals(COUNT_LANGUAGES, Language.values().length);
    }

    public Map<String, String> loadDictionaryUsingLocaleManager(Language language) {
        Locale locale = new Locale(language.getLocaleLanguage(), language.getLocaleCountry());
        String graphResource = LocaleManager.GRAPH_RESOURCE;
        return localeManager.getDictionary(graphResource, locale);
    }

    public Map<String, String> createDictionary(Language language) {
        Locale locale = new Locale(language.getLocaleLanguage(), language.getLocaleCountry());
        ResourceBundle bundle = ResourceBundle.getBundle(LocaleManager.GRAPH_RESOURCE, locale);

        Map<String, String> dictionary = new HashMap<String, String>();
        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            String value = bundle.getString(key);
            dictionary.put(key, value);
        }
        return dictionary;
    }
}
