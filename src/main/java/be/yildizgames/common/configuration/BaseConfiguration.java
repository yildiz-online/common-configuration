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
import java.util.Locale;
import java.util.Properties;

/**
 * @author Grégory Van den Borre
 */
public class BaseConfiguration {

    public static final String FRENCH = "Français";

    public static final String ENGLISH = "English";

    public static final String TURKISH = "Türkçe";

    public static final String EULA_ACCEPTED = "eula.accepted";

    public static final String LANGUAGE = "language";

    private final Properties properties;

    public BaseConfiguration(Properties properties) {
        super();
        this.properties = properties;
    }

    public final Locale getLanguage() {
        return Locale.forLanguageTag(this.properties.getProperty(LANGUAGE, "fr"));
    }

    public final void setLanguage(Locale locale) {
        this.properties.setProperty(LANGUAGE, locale.getLanguage());
        this.store();
    }

    public final void store() {
        try (var buf = Files.newBufferedWriter(Path.of("config/configuration.properties"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)){
            this.properties.store(buf, "Properties");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public final boolean isEulaAccepted(String expectedHash) {
        return this.properties.getProperty(EULA_ACCEPTED).equals(expectedHash);
    }

    public final void setEulaAccepted(String hash) {
        this.properties.setProperty(EULA_ACCEPTED, hash);
    }

    public final void setEulaNotAccepted() {
        this.properties.setProperty(EULA_ACCEPTED, "0");
    }
}
