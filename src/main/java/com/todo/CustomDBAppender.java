package com.todo;

import ch.qos.logback.classic.db.DBAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Map;

public class CustomDBAppender extends DBAppender {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDBAppender.class);

    @Override
    protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement insert) {
        try {
            insert.setLong(1, event.getTimeStamp());
            insert.setString(2, event.getFormattedMessage());
            insert.setString(3, event.getLoggerName());
            insert.setString(4, event.getLevel().toString());
            insert.setString(5, event.getThreadName());
            insert.setString(6, event.getThrowableProxy() == null
                    ? null
                    : Arrays.toString(event.getThrowableProxy().getStackTraceElementProxyArray()).replaceAll(", ", "\n")
            );

            insert.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Error while inserting in DB log", e);
            addWarn("Failed to insert loggingEvent");
        }
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO system_log (" +
                "timestmp, " +
                "formatted_message, " +
                "logger_name, " +
                "level_string, " +
                "thread_name," +
                "stack_trace" +
                ") VALUES (?, ?, ? ,?, ?, ?)";
    }

    @Override
    protected void secondarySubAppend(ILoggingEvent event, Connection connection, long eventId) {
    }

    @Override
    protected void insertProperties(Map<String, String> mergedMap, Connection connection, long eventId) {
    }
}