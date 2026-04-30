package com.jazzkuh.m0nitor.utils.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.core.pattern.color.ANSIConstants;

public final class ColorPrefixConverter extends ClassicConverter {
    private static final String ESC = "\u001B[";
    private static final String RESET = ESC + "0;" + ANSIConstants.DEFAULT_FG + "m";

    @Override
    public String convert(ILoggingEvent event) {
        String logger = event.getLoggerName().toLowerCase();

        String prefixColor;
        String label;

        if (logger.contains("auron")) {
            prefixColor = ANSIConstants.CYAN_FG;
            label = "AURON";
        } else if (logger.contains("web")) {
            prefixColor = ANSIConstants.MAGENTA_FG;
            label = "WEB";
        } else {
            prefixColor = ANSIConstants.GREEN_FG;
            label = "CORE";
        }

        String levelColor = switch (event.getLevel().toInt()) {
            case Level.ERROR_INT -> ANSIConstants.BOLD + ANSIConstants.RED_FG;
            case Level.WARN_INT -> ANSIConstants.BOLD + ANSIConstants.YELLOW_FG;
            default -> ANSIConstants.DEFAULT_FG;
        };

        String prefix = ESC + ANSIConstants.BOLD + prefixColor + "m[" + label + "]" + RESET;
        String message = ESC + levelColor + "m[%s %s]: %s - %s" + RESET;

        return prefix + " " + String.format(message,
                new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(event.getTimeStamp())),
                event.getLevel(),
                event.getLoggerName().substring(Math.max(0, event.getLoggerName().length() - 36)),
                event.getFormattedMessage());
    }
}