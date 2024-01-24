/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2019-2024 Grégory Van den Borre
 More infos available: https://engine.yildiz-games.be
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.configuration.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to display message before the logging is configured.
 * The displayed message is time [level] message.
 *
 * @author Grégory Van den Borre
 */
public class PreLogger {

    /**
     * To format the time displayed with the message.
     */
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Display an information message.
     * @param message Message to display.
     */
    public final void info(final String message) {
        this.print(" | INFO | be.yildizgames.common.application.Prelogger | " + message);
    }

    /**
     * Display a warning message.
     * @param message Message to display.
     */
    public final void warn(final String message) {
        this.print(" | WARN | be.yildizgames.common.application.Prelogger | " + message);
    }

    /**
     * Display an error message.
     * @param message Message to display.
     */
    public final void error(final String message) {
        this.print(" | ERROR| be.yildizgames.common.application.Prelogger | " + message);
    }

    /**
     * Display an information message.
     * @param message Message to display.
     * @param e Exception to print stacktrace.
     */
    public final void error(final String message, final Exception e) {
        this.print(" | ERROR| be.yildizgames.common.application.Prelogger | " + message);
        this.print(" | ERROR| be.yildizgames.common.application.Prelogger | " + e.getMessage());
        e.printStackTrace(System.out);
    }

    /**
     * Print the message on the console.
     * @param s Message to print.
     */
    private void print(final String s) {
        Date date = new Date();
        System.out.println(dateFormat.format(date) + s);
    }

}
