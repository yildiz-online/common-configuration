/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2019-2024 Grégory Van den Borre
 More infos available: https://engine.yildiz-games.be
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.configuration;

import be.yildizgames.common.configuration.logger.PreLogger;

import java.util.Objects;
import java.util.Properties;

/**
 * Provide default configuration values.
 * @author Grécory Van den Borre
 */
public class ConfigurationNotFoundDefault implements ConfigurationNotFoundStrategy {

    /**
     * As the logger has not read its config, using prelogger.
     */
    private final PreLogger preLogger = new PreLogger();

    /**
     * Default properties to use.
     */
    private final Properties properties;

    /**
     * Create a new instance from a properties object.
     * @param properties Default properties.
     */
    private ConfigurationNotFoundDefault(final Properties properties) {
        super();
        Objects.requireNonNull(properties);
        this.properties = properties;
    }

    /**
     * Create a new instance from a properties object.
     * @param properties Default properties.
     */
    private ConfigurationNotFoundDefault(final Properties properties, ConfigurationNotFoundAdditionalBehavior behavior) {
        this(properties);
        behavior.execute();
    }

    public static ConfigurationNotFoundStrategy fromDefault(Properties properties) {
        return new ConfigurationNotFoundDefault(properties);
    }

    public static ConfigurationNotFoundStrategy fromDefault(Properties properties, ConfigurationNotFoundAdditionalBehavior behavior) {
        return new ConfigurationNotFoundDefault(properties, behavior);
    }

    @Override
    public final Properties notFound() {
        this.preLogger.info("Loading properties failed, fallback to default values.");
        return this.properties;
    }

    public Properties getProperties() {
        return this.properties;
    }
}
