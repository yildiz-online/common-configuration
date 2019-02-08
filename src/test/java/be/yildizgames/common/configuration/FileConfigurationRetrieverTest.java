/*
 *
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 * Copyright (c) 2018 Grégory Van den Borre
 *
 * More infos available: https://www.yildiz-games.be
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

import be.yildizgames.common.configuration.parameter.ApplicationArgs;
import be.yildizgames.common.configuration.parameter.DefaultArgName;
import be.yildizgames.common.exception.implementation.ImplementationException;
import be.yildizgames.common.exception.initialization.InitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Grégory Van den Borre
 */
public class FileConfigurationRetrieverTest {

    @Test
    public void happyFlow() {
        ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
        Assertions.assertNotNull(retriever);
    }

    @Test
    public void withNullParameter() {
        Assertions.assertThrows(ImplementationException.class, () -> new FileConfigurationRetriever(null));
    }

    @Nested
    public class FromArgs {

        @Test
        public void happyFlow() throws IOException {
            Path config = Files.createTempFile("config",".properties");
            Properties properties = new Properties();
            properties.put("value", "test");
            properties.store(Files.newBufferedWriter(config), "Test properties");
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Properties result = retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=" + config.toString()));
            Assertions.assertEquals("test", result.getProperty("value"));
        }

        @Test
        public void fileNotFound() {
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(InitializationException.class, () -> retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=invalid/path/config.properties")));
        }

        @Test
        public void applicationArgsNull() {
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(ImplementationException.class, () -> retriever.retrieveFromArgs(null));
        }

        @Test
        public void argsEmpty() throws IOException {
            Path config = Files.createTempFile("config",".properties");
            Properties properties = new Properties();
            properties.put("value", "test");
            properties.store(Files.newBufferedWriter(config), "Test properties");
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(InitializationException.class, () -> retriever.retrieveFromArgs(ApplicationArgs.of()));
        }

        @Test
        public void argsNull() throws IOException {
            Path config = Files.createTempFile("config",".properties");
            Properties properties = new Properties();
            properties.put("value", "test");
            properties.store(Files.newBufferedWriter(config), "Test properties");
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(InitializationException.class, () -> retriever.retrieveFromArgs(ApplicationArgs.of((String[]) null)));
        }

    }

}
