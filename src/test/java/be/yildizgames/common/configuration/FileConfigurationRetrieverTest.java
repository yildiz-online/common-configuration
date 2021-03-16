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

import be.yildizgames.common.configuration.parameter.ApplicationArgs;
import be.yildizgames.common.configuration.parameter.DefaultArgName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Grégory Van den Borre
 */
class FileConfigurationRetrieverTest {

    @Test
    void happyFlow() {
        ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
        Assertions.assertNotNull(retriever);
    }

    @Test
    void fileWithBackslashes() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("logger.pattern", "%d{yyyy/MM/dd HH:mm:ss} | %level | %logger | %msg%n");
        properties.setProperty("logger.level", "info");
        properties.setProperty("logger.output", "file");
        properties.setProperty("logger.tcp.host", "localhost");
        properties.setProperty("logger.tcp.port", "12345");
        properties.setProperty("logger.file.output", "C:\test");
        properties.setProperty("logger.configuration.file", new File("").getAbsolutePath() + "/temp/");
        properties.setProperty("logger.disabled", "azerty,qwerty");
        properties.store(new FileWriter("p.properties"),"");
        ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
        Path config = Files.createTempFile("configBS",".properties");
        properties.store(Files.newBufferedWriter(config), "Test properties");
        Properties result = retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=" + config.toString()));
    }

    @Test
    void testEncoding() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("test", "éèç");
        properties.store(new FileWriter("test.properties"),"");
        ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
        Properties result = retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=test.properties"));
        Assertions.assertEquals("éèç", result.getProperty("test"));
    }

    @Test
    void fileExistWithPropertyOnlyInDefault() throws IOException {
        Properties p = new Properties();
        p.put("default", "true");
        p.put("defaultOnly", "true");

        Path config = Files.createTempFile("config",".properties");
        Properties properties = new Properties();
        properties.put("default", "false");
        properties.store(Files.newBufferedWriter(config), "Test properties");

        ConfigurationRetriever retriever = new FileConfigurationRetriever(ConfigurationNotFoundDefault.fromDefault(p));
        Properties result = retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=" + config.toString()));
        Assertions.assertEquals("true", result.getProperty("defaultOnly"));
        Assertions.assertEquals("false", result.getProperty("default"));
    }

    @Test
    void withNullParameter() {
        Assertions.assertThrows(NullPointerException.class, () -> new FileConfigurationRetriever(null));
    }

    @Nested
    class FromArgs {

        @Test
        void happyFlow() throws IOException {
            Path config = Files.createTempFile("config",".properties");
            Properties properties = new Properties();
            properties.put("value", "test");
            properties.store(Files.newBufferedWriter(config), "Test properties");
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Properties result = retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=" + config.toString()));
            Assertions.assertEquals("test", result.getProperty("value"));
        }

        @Test
        void fileNotFound() {
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(IllegalStateException.class, () -> retriever.retrieveFromArgs(ApplicationArgs.of(DefaultArgName.CONFIGURATION_FILE + "=invalid/path/config.properties")));
        }

        @Test
        void applicationArgsNull() {
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(NullPointerException.class, () -> retriever.retrieveFromArgs(null));
        }

        @Test
        void argsEmpty() throws IOException {
            Path config = Files.createTempFile("config",".properties");
            Properties properties = new Properties();
            properties.put("value", "test");
            properties.store(Files.newBufferedWriter(config), "Test properties");
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(IllegalStateException.class, () -> retriever.retrieveFromArgs(ApplicationArgs.of()));
        }

        @Test
        void argsNull() throws IOException {
            Path config = Files.createTempFile("config",".properties");
            Properties properties = new Properties();
            properties.put("value", "test");
            properties.store(Files.newBufferedWriter(config), "Test properties");
            ConfigurationRetriever retriever = new FileConfigurationRetriever(new ConfigurationNotFoundException());
            Assertions.assertThrows(IllegalStateException.class, () -> retriever.retrieveFromArgs(ApplicationArgs.of((String[]) null)));
        }

    }

}
