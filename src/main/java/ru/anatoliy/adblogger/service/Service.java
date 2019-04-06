package ru.anatoliy.adblogger.service;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.anatoliy.adblogger.AdbCommands;
import ru.anatoliy.adblogger.util.CommandExecutor;
import ru.anatoliy.adblogger.util.LogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Service {
    @Getter
    @Setter
    private String apkPath;

    private ExecutorService executorLoggerService = Executors.newFixedThreadPool(4);
    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    @Setter
    @Getter
    private HashMap<String, String> devices;

    public Service() {
        devices = new HashMap<>();
    }


    public void checkDevices() throws IOException {

        BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.devices());
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.endsWith("device")) {
                line = line.replace("device", "").trim();
                logger.debug("Connected device {}", line);
                devices.put(line, "ready to use");
            }
            if (line.endsWith("unauthorized")) {
                line = line.replace("unauthorized", "").trim();
                devices.put(line, "unauthorized");
                logger.debug("Unauthorized device {}", line);
            }
        }
    }

    public void installApk(Consumer<Boolean> booleanCallback, Consumer<String> callback) {
        if (apkPath == null) {
            callback.accept("No apk path");
            return;
        }
        if (devices.size() == 0) {
            callback.accept("No connected devices. Check devices and try initialize.");
            return;
        }

        executorLoggerService.execute(() -> {
            booleanCallback.accept(true);
            devices.forEach((s, s2) -> {
                logger.debug("Start installation {}", s);
                BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.install(s, apkPath));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        logger.debug("Installation device {} is {}", s, line);
                        callback.accept(s + "  -  " + line);
                    }
                } catch (IOException ex) {
                    logger.error("Error at installation ", ex);
                    callback.accept("Something was wrong");
                }
            });
            booleanCallback.accept(false);
        });
    }

    public void uninstall(Consumer<String> callback) {
        if (devices.size() == 0) {
            callback.accept("No connected devices. Check devices and try initialize.");
            return;
        }

        executorLoggerService.execute(() -> {
            devices.forEach((s, s2) -> {
                logger.debug("Start uninstallation {}", s);
                BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.uninstall(s));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        logger.debug("Uninstall for device {} is {}", s, line);
                        callback.accept(s + "  -  " + line);
                    }
                } catch (IOException ex) {
                    logger.error("Error at uninstall ", ex);
                    callback.accept("Something was wrong");
                }
            });
        });
    }

    public void startApp(Consumer<String> callback) {
        if (devices.size() == 0) {
            callback.accept("No connected devices. Check devices and try initialize.");
            return;
        }

        executorLoggerService.execute(() -> {
            devices.forEach((s, s2) -> {
                logger.debug("Start Flex for device {}", s);
                BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.launch(s));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("Line ** No activities found to run, monkey aborted")) {
                            logger.debug("Application not installed at {}", s);
                            callback.accept("Application not installed");
                        } else {
                            logger.debug("Application run on {}", s);
                            callback.accept("Application run on " + s);
                        }
                    }
                } catch (IOException ex) {
                    logger.error("Error at launch Flex ", ex);
                }
            });
        });
    }

    public void checkLog(String path, Consumer<String> callback) {
        executorLoggerService.execute(() -> {
            logger.debug("Start parsing {}", path);
            String resultParser = LogParser.findException(path);
            logger.debug("End parsing {}", path);
            callback.accept(resultParser);
        });
    }

    public void startLogRecord(Consumer<String> callback) {
        if (devices.size() == 0) {
            callback.accept("No connected devices. Check devices and try initialize.");
            return;
        }
        File folder = new File(System.getProperty("user.home") + "\\Documents\\Log_TFa\\");

        devices.forEach((s, s2) -> {
            executorLoggerService.execute(() -> {
                        String logFile = System.getProperty("user.home") + "\\Documents\\Log_TFa\\log_" + s + ".txt";
                        Process process = null;
                        try {
                            process = Runtime.getRuntime().exec(AdbCommands.logcat(s, logFile));
                        } catch (IOException e) {
                            logger.error("Error start write log {}", s, e);
                        }
                        logger.debug("Start write log for {}", s);
                    }
            );
        });
    }

    public void stopLogRecord(Consumer<String> callback) {
        if (devices.size() == 0) {
            callback.accept("No connected devices. Check devices and try initialize.");
            return;
        }
        executorLoggerService.shutdownNow();
    }
}
