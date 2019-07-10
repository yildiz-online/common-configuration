package be.yildizgames.common.configuration.parameter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultArgNameTest {

    @Test
    public void configurationFile() {
        Assertions.assertEquals("configuration", DefaultArgName.CONFIGURATION_FILE);
    }

    @Test
    public void configurationCheck() {
        Assertions.assertEquals("configuration-check", DefaultArgName.CONFIGURATION_CHECK);
    }
}
