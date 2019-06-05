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

import be.yildizgames.common.logging.LoggerConfiguration;
import be.yildizgames.common.logging.LoggerLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

public class LoggerPropertiesConfigurationTest {

    @Nested
    public class FromProperties {


        @Test
        public void nullProperties() {
            Assertions.assertThrows(NullPointerException.class, () -> LoggerPropertiesConfiguration.fromProperties(null));
        }

        @Test
        public void happyFlow() {
            Properties properties = new Properties();
            properties.setProperty("logger.pattern", "%n");
            properties.setProperty("logger.level", "info");
            properties.setProperty("logger.output", "console");
            properties.setProperty("logger.tcp.host", "localhost");
            properties.setProperty("logger.tcp.port", "12345");
            properties.setProperty("logger.file.output", "root/dr");
            properties.setProperty("logger.configuration.file", "file.log");
            properties.setProperty("logger.disabled", "azerty,qwerty");
            LoggerConfiguration configuration = LoggerPropertiesConfiguration.fromProperties(properties);
            Assertions.assertEquals("%n", configuration.getLoggerPattern());
            Assertions.assertEquals("file.log", configuration.getLoggerConfigurationFile());
            Assertions.assertEquals("root/dr", configuration.getLoggerOutputFile());
            Assertions.assertEquals(LoggerConfiguration.SupportedOutput.CONSOLE, configuration.getLoggerOutput());
            Assertions.assertEquals("localhost", configuration.getLoggerTcpHost());
            Assertions.assertEquals(12345, configuration.getLoggerTcpPort());
            Assertions.assertEquals(LoggerLevel.INFO, configuration.getLoggerLevel());
            Assertions.assertEquals(Arrays.asList("azerty","qwerty"), configuration.getLoggerToDisable());
        }
    }

}
