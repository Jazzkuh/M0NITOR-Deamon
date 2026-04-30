package com.jazzkuh.m0nitor.utils.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

public class LogAppender extends AppenderBase<ILoggingEvent> {
    private static final int MAX_ENTRIES = 1000;
    private static final Deque<LogEntry> buffer = new ArrayDeque<>();
    private static volatile Consumer<LogEntry> listener;

    @Override
    protected void append(ILoggingEvent event) {
        LogEntry entry = toEntry(event);
        synchronized (buffer) {
            if (buffer.size() >= MAX_ENTRIES) buffer.pollFirst();
            buffer.addLast(entry);
        }
        Consumer<LogEntry> l = listener;
        if (l != null) l.accept(entry);
    }

    public static List<LogEntry> getEntries() {
        synchronized (buffer) {
            return new ArrayList<>(buffer);
        }
    }

    public static void setListener(Consumer<LogEntry> l) {
        listener = l;
    }

    private static LogEntry toEntry(ILoggingEvent event) {
        String logger = event.getLoggerName().toLowerCase();
        String label;
        if (logger.contains("auron")) label = "AURON";
        else if (logger.contains("web")) label = "WEB";
        else label = "CORE";

        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(event.getTimeStamp()));
        String loggerShort = event.getLoggerName().substring(Math.max(0, event.getLoggerName().length() - 36));
        String text = String.format("[%s] [%s %s]: %s - %s",
                label, time, event.getLevel(), loggerShort, event.getFormattedMessage());

        return new LogEntry(label, event.getLevel().toInt(), text);
    }

    public record LogEntry(String label, int level, String text) {}
}