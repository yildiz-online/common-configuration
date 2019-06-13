package be.yildizgames.common.configuration;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class FileReloadableConfigurationTest {

    @Test
    public void happyFlow() {
        FileReloadableConfiguration configuration = new FileReloadableConfiguration(Paths.get("C:\\data\\test.txt"), ()->{});
        configuration.inspect(0);
    }
}
