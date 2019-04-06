package ru.anatoliy.adblogger.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogParser {
    private static final Logger logger = LoggerFactory.getLogger(LogParser.class);

    public static String findException(String path) {
        logger.debug("Start parsing file {} ", path);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"))) {
            String strLine;
            String ex = null;
            StringBuilder buffer = new StringBuilder();

            while ((strLine = reader.readLine()) != null) {
                if (strLine.contains("FATAL EXCEPTION")) {
                    logger.debug("Exception found at {}", strLine);
                    ex = strLine.substring(0, 43);
                }

                if (ex != null && strLine.contains(ex)) {
                    buffer.append(strLine).append("\n");
                } else if (ex != null) {
                    buffer.append("\n\n");
                    ex = null;
                }
            }

            if (buffer.length() == 0) {
                logger.debug("Exception not founded {}", path);
                return "FATAL EXCEPTION not found.";
            } else {
                return buffer.toString();
            }
        } catch (IOException e) {
            logger.error("Error in parse file - {} ", path, e);
            e.printStackTrace();
        }

        return "Something was unexpected. Check log file";
    }
}
