package com.example.lib.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DynamicArrayStorage logs;

    public Logger() {
        this.logs = new DynamicArrayStorage(16);
    }

    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entry = String.format("[%s] [%s] %s", timestamp, level, message);
        logs.add(entry);
        writeToFile(entry);
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    private void writeToFile(String entry) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(entry);
        } catch (IOException e) {
            System.err.println("Не удалось записать в файл журнала: " + e.getMessage());
        }
    }

    public String[] getAllLogs() {
        Object[] raw = logs.toArray();
        String[] result = new String[raw.length];
        for (int i = 0; i < raw.length; i++) {
            result[i] = (String) raw[i];
        }
        return result;
    }
}
