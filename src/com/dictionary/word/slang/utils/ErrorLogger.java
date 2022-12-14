package com.dictionary.word.slang.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLogger {
    private static final Logger logger;
    private static final FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(Constant.Path.ERROR_LOG);
            fileHandler.setFormatter(new SimpleFormatter());

            logger = Logger.getLogger(ErrorLogger.class.getName());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Log the stack trace of the exception to the log file and the console.
     * @param exception The exception to be logged
     */
    public static void severe(Exception exception) {
        logger.severe(convertStackTraceToString(exception));
    }

    /**
     * @param exception The exception
     * @return The string representation of the stack trace
     */
    private static String convertStackTraceToString(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}
