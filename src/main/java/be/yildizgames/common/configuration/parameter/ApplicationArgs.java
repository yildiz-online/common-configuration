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

package be.yildizgames.common.configuration.parameter;

import be.yildizgames.common.configuration.logger.PreLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Argument received when starting the application, expected format is key=value.
 * Any argument without key value pattern can be also retrieved anyway.
 * @author Grégory Van den Borre
 */
public class ApplicationArgs {

    /**
     * List of key values arguments received.
     */
    private final List<Arg> args = new ArrayList<>();

    /**
     * List of values only arguments received.
     */
    private final List<String> noKeyArgs = new ArrayList<>();

    /**
     * Create a new instance.
     * @param args Application raw arguments, null is accepted.
     */
    private ApplicationArgs(String[] args) {
        super();
        if(args == null) {
            new PreLogger().info("No arg parameters passed to the application.");
        } else {
            for (String arg : args) {
                String[] value = arg.split("=");
                if (value.length == 2) {
                    this.args.add(new Arg(value[0], value[1]));
                } else {
                    this.noKeyArgs.add(arg);
                }
            }
        }
    }

    /**
     * Create a new arguments instance.
     * @param args Arguments received, can be null.
     * @return The created application argument object.
     */
    public static ApplicationArgs of(String... args) {
        return new ApplicationArgs(args);
    }

    /**
     * Provide tha application args.
     * @return The application args.
     */
    public final List<Arg> getArgs() {
        return Collections.unmodifiableList(args);
    }

    /**
     * Provide an argument depending from its key.
     * @param key Argument key.
     * @return The value matching the key.
     */
    public final Optional<String> getArg(String key) {
        return args
                .stream()
                .filter(arg -> arg.key.equalsIgnoreCase(key))
                .map(arg -> arg.value)
                .findFirst();
    }

    /**
     * Check if there is no argument.
     * @return true if no argument has been passed, false otherwise.
     */
    public final boolean isEmpty() {
        return this.args.isEmpty();
    }
}
