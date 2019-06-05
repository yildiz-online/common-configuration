package be.yildizgames.common.configuration;

import be.yildizgames.common.logging.LoggerConfiguration;
import be.yildizgames.common.logging.LoggerLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Logger configuration built from a property file.
 * @author Grégory Van den Borre.
 */
public class LoggerPropertiesConfiguration implements LoggerConfiguration {

    /**
     * Pattern to display to message.
     */
    private final String loggerPattern;

    /**
     * Level of the logger.
     */
    private final LoggerLevel loggerLevel;

    /**
     * Output of the logger.
     */
    private final SupportedOutput loggerOutput;

    /**
     * Host in case of TCP output.
     */
    private final String loggerTcpHost;

    /**
     * Port in case of TCP output.
     */
    private final int loggerTcpPort;

    /**
     * File in case of FILE output.
     */
    private final String loggerFile;

    /**
     * File where the configuration will be written.
     */
    private final String configurationFile;

    private final List<String> loggerToDisable;

    /**
     * Build the instance.
     * @param p Properties to build from.
     */
    private LoggerPropertiesConfiguration(final Properties p) {
        Objects.requireNonNull(p);
        this.loggerPattern = PropertiesHelper.getValue(p, "logger.pattern");
        this.loggerLevel = LoggerLevel.valueOf(PropertiesHelper.getValue(p, "logger.level").toUpperCase());
        this.loggerOutput = SupportedOutput.valueOf(PropertiesHelper.getValue(p, "logger.output").toUpperCase());
        this.loggerTcpHost = PropertiesHelper.getValue(p, "logger.tcp.host");
        this.loggerTcpPort = PropertiesHelper.getIntValue(p, "logger.tcp.port");
        this.loggerFile = PropertiesHelper.getValue(p, "logger.file.output");
        this.configurationFile = PropertiesHelper.getValue(p, "logger.configuration.file");
        String disable = PropertiesHelper.getValue(p, "logger.disabled");
        this.loggerToDisable = Arrays.asList(disable.split(","));
    }

    /**
     * Create a new configuration from a properties file.
     * Expected properties are:
     * logger.pattern: String.
     * logger.level: TRACE, DEBUG, INFO, WARN, ERROR.
     * logger.output: FILE, TCP, CONSOLE.
     * logger.tcp.host: String.
     * logger.tcp.port: a valid port number.
     * logger.file.output: String.
     * logger.configuration.file: String.
     * @param p Properties to build from.
     * @return The built configuration.
     */
    public static LoggerConfiguration fromProperties(Properties p) {
        return new LoggerPropertiesConfiguration(p);
    }

    @Override
    public final String getLoggerPattern() {
        return this.loggerPattern;
    }

    @Override
    public final LoggerLevel getLoggerLevel() {
        return this.loggerLevel;
    }

    @Override
    public final SupportedOutput getLoggerOutput() {
        return this.loggerOutput;
    }

    @Override
    public final String getLoggerTcpHost() {
        return this.loggerTcpHost;
    }

    @Override
    public final int getLoggerTcpPort() {
        return this.loggerTcpPort;
    }

    @Override
    public final String getLoggerOutputFile() {
        return this.loggerFile;
    }

    @Override
    public final String getLoggerConfigurationFile() {
        return this.configurationFile;
    }

    @Override
    public final List<String> getLoggerToDisable() {
        return this.loggerToDisable;
    }

}
