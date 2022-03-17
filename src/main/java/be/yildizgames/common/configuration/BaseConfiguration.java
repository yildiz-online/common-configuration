/*
 * MIT License
 *
 * Copyright (c) 2019 Grégory Van den Borre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Grégory Van den Borre
 */
public class BaseConfiguration implements LanguageConfiguration {

    public static final String FRENCH = "Français";

    public static final String ENGLISH = "English";

    public static final String TURKISH = "Türkçe";

    public static final String EULA_ACCEPTED = "eula.accepted";

    public static final String LANGUAGE = "language";

    private final List<ConfigurationChangeListener> listeners = new ArrayList<>();

    private final List<LocaleChangedListener> localeChangedListeners = new ArrayList<>();

    private final List<Locale> supportedLocales = new ArrayList<>();

    private final Properties properties;

    public BaseConfiguration(Properties properties) {
        this(properties, List.of(Locale.ENGLISH));
    }

    public BaseConfiguration(Properties properties, List<Locale> supportedLocales) {
        super();
        this.properties = properties;
        this.supportedLocales.addAll(supportedLocales);
    }

    protected final String get(String key) {
        return this.properties.getProperty(key);
    }

    protected final String get(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    public final void addLocaleChangedListener(LocaleChangedListener listener) {
        this.localeChangedListeners.add(listener);
    }

    public final void addConfigurationChangedListener(ConfigurationChangeListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public final Locale getLocale() {
        return Locale.forLanguageTag(this.properties.getProperty(LANGUAGE, "fr"));
    }

    @Override
    public final List<Locale> getSupportedLocale() {
        return Collections.unmodifiableList(this.supportedLocales);
    }

    @Override
    public final void setLocale(Locale locale) {
        this.properties.setProperty(LANGUAGE, locale.getLanguage());
        this.store();
        this.localeChangedListeners.forEach(l -> l.setLanguage(locale));
    }

    protected final void store() {
        try (var buf = Files.newBufferedWriter(Path.of("config/configuration.properties"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            this.properties.store(buf, "Properties");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected final void updateValue(String changedProperty, String newValue) {
        this.properties.setProperty(changedProperty, newValue);
        this.store();
        this.listeners.stream()
                .filter(l -> l.getUsedConfigurationProperties().contains(changedProperty))
                .forEach(l -> l.onChange(changedProperty, newValue));
    }

    public final boolean isEulaAccepted(String expectedHash) {
        return this.properties.getProperty(EULA_ACCEPTED).equals(expectedHash);
    }

    public final boolean setEulaAccepted(String hash) {
        try {
            this.properties.setProperty(EULA_ACCEPTED, hash);
            this.store();
            return true;
        } catch (IllegalStateException e) {
            System.getLogger(this.getClass().getName()).log(System.Logger.Level.ERROR, e);
            return false;
        }
    }

    public final void setEulaNotAccepted() {
        this.properties.setProperty(EULA_ACCEPTED, "0");
    }
}
