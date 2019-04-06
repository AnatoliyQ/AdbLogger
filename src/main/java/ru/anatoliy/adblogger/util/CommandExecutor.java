package ru.anatoliy.adblogger.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    private CommandExecutor() {
    }

    public static BufferedReader executeCommand(String command) {
        Process process = null;
        try {
            logger.debug("Executing command ", command);
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            logger.error("Error in executing command", e);
        }
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }
}
