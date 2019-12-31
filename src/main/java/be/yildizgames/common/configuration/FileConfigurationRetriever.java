/*
 *
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 * Copyright (c) 2019 Grégory Van den Borre
 *
 * More infos available: https://engine.yildiz-games.be
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 *
 */

package be.yildizgames.common.configuration;

import be.yildizgames.common.configuration.logger.PreLogger;
import be.yildizgames.common.configuration.parameter.ApplicationArgs;
import be.yildizgames.common.configuration.parameter.DefaultArgName;
import be.yildizgames.common.file.FileProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Grégory Van den Borre
 */
class FileConfigurationRetriever implements ConfigurationRetriever {

    private final PreLogger preLogger = new PreLogger();

    private final ConfigurationNotFoundStrategy notFoundStrategy;
    private Path configPath;

    FileConfigurationRetriever(ConfigurationNotFoundStrategy strategy) {
        super();
        Objects.requireNonNull(strategy);
        this.notFoundStrategy = strategy;
    }

    @Override
    public Properties retrieveFromArgs(ApplicationArgs args) {
        this.preLogger.info("Loading configuration file...");
        Optional<String> path = args.getArg(DefaultArgName.CONFIGURATION_FILE);
        if(path.isEmpty()) {
            path = Optional.of("configuration.properties");
            Path defaultConfigFile = Paths.get("configuration.properties");
            this.configPath = defaultConfigFile;
            if(Files.notExists(defaultConfigFile)) {
                this.preLogger.warn("Configuration file not found, default configuration file 'configuration.properties' was not found and no application arg provider with '" + DefaultArgName.CONFIGURATION_FILE + "' key");
                return this.storeConfiguration(this.notFoundStrategy.notFound());
            }
        }
        Properties properties;
        Properties result = new Properties();
        try {
            this.configPath = Paths.get(path.get());
            properties = FileProperties.getPropertiesFromFile(this.configPath);
            this.preLogger.info("Loading configuration file success.");
            Properties[] p = {this.notFoundStrategy.getProperties(), properties};
            Arrays.stream(p).forEach(result::putAll);
            return this.storeConfiguration(result);
            //FileReloadableConfiguration reloadableConfiguration = new FileReloadableConfiguration(this.configPath);
        } catch (IllegalStateException e) {
            this.preLogger.error("Configuration file not found", e);
            return this.notFoundStrategy.notFound();
        }
    }

    private Properties storeConfiguration(final Properties result) {
        try {
            result.store(Files.newBufferedWriter(this.configPath,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE), "Properties");
        } catch (IOException e) {
            this.preLogger.error("Error writing configuration file", e);
        }
        return result;
    }

    @Override
    public final void onReload(ConfigurationReloadedBehavior behavior) {
        Optional.ofNullable(configPath)
                .ifPresent(path -> new FileReloadableConfiguration(this.configPath, behavior).inspect());
    }
}
