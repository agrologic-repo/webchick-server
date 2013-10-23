package com.agrologic.app.service;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 */
public class MessageLocaleService {
    protected ResourceBundle messages;
    protected ResourceBundle defaultLocaleMessages;

    public MessageLocaleService(Locale locale) {
        this.messages = ResourceBundle.getBundle("messages", locale);
        this.defaultLocaleMessages = ResourceBundle.getBundle("messages");
    }

    /**
     * Return messages for ui messages
     *
     * @return messages the resource bundle with locale that was in session
     */
    public ResourceBundle getMessages() {
        return messages;
    }

    /**
     * Return defaultLocaleMessages for logging messages
     *
     * @return defaultLocaleMessages the resource bundle with locale english
     */
    public ResourceBundle getDefaultLocaleMessages() {
        return defaultLocaleMessages;
    }
}
