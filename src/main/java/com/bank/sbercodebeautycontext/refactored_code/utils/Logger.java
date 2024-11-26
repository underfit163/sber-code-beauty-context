
package com.bank.sbercodebeautycontext.refactored_code.utils;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Сервис логирования событий
 */
@Component
public final class Logger {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);

    public static org.slf4j.Logger getLogger() {
        return logger;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(final String message, final Object ... params) {
        logger.info(message, params);
    }

    public void error(String message) {
        logger.error(message);
    }

    public <T> void error(final String message, final Throwable ex) {
        logger.error(message, ex);
    }

    public void error(final String message, final Object ... params) {
        logger.error(message, params);
    }

    public void warn(final String message) {
        logger.warn(message);
    }

    public void warn(final String message, final Object ... params) {
        logger.warn(message, params);
    }

    public void debug(final String message) {
        logger.debug(message);
    }

    public void debug(final String message, final Object ... params) {
        logger.debug(message, params);
    }

    public void trace(final String message) {
        logger.trace(message);
    }

    public void trace(final String message, final Object ... params) {
        logger.trace(message, params);
    }
}
