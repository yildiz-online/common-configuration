package be.yildizgames.common.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReloadableConfigurationTest {

    private boolean run = false;

    @Disabled
    @Test
    public void happyFlow() throws IOException, InterruptedException {
        this.run = false;
        Path config = Files.createTempFile("test" + System.currentTimeMillis(), ".txt");
        FileReloadableConfiguration configuration = new FileReloadableConfiguration(config, this::run);
        configuration.inspect();
        Assertions.assertFalse(this.run);
        Files.write(config, "test".getBytes());
        Thread.sleep(1000);
        Assertions.assertTrue(this.run);
    }

    private void run() {
        System.out.println("updated");
        run = true;
    }
}
