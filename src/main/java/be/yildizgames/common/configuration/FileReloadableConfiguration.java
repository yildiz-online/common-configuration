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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * @author Grégory Van den Borre
 */
public class FileReloadableConfiguration {

    //private final PreLogger preLogger = LogEngineProvider.getLoggerProvider().getLogEngine().getPrelogger();

    private final Path path;

    private final ConfigurationReloadedBehavior behavior;

    public FileReloadableConfiguration(Path configPath, ConfigurationReloadedBehavior behavior) {
        super();
        this.path = configPath;
        this.behavior = behavior;
    }

    void inspect() {
        inspect(Integer.MAX_VALUE);
    }

    void inspect(int max) {
        try {
            int current = 0;
            WatchService watcher = FileSystems.getDefault().newWatchService();
            this.path.getParent().register(watcher, ENTRY_MODIFY);
            try {
                WatchKey key;
                while (current < max && (key = watcher.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        this.behavior.reload();
                    }
                    key.reset();
                    current++;
                }
            } catch (InterruptedException x) {
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
