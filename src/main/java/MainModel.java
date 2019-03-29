import lombok.Getter;
import lombok.Setter;
import util.CommandExecutor;
import util.LogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.*;

public class MainModel {
    @Getter
    @Setter
    private String apkPath;

    private ExecutorService executorLoggerService;

    @Setter
    @Getter
    private HashMap<String, String> devices;

    public MainModel() {
        devices = new HashMap<>();
    }


    public void checkDevices() throws IOException {

        BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.devices());
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.endsWith("device")) {
                line = line.replace("device", "").trim();
                devices.put(line, "ready to use");
            }
            if (line.endsWith("unauthorized")) {
                line = line.replace("unauthorized", "").trim();
                devices.put(line, "unauthorized");
            }
        }
    }

    public void installApk(BooleanCallback booleanCallback, ProcessCallback callback) {
        if (apkPath == null) {
            callback.onExecute("No apk path");
            return;
        }
        if (devices.size() == 0) {
            callback.onExecute("No connected devices. Check devices and try initialize.");
            return;
        }

        Thread installation = new Thread(() -> {
            booleanCallback.some(true);
            System.out.println("Here in thread");
            devices.forEach((s, s2) -> {
                System.out.println("Start installation for " + s);
                BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.install(s, apkPath));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Line " + line);
                        callback.onExecute(s + "  -  " + line);
                    }
                } catch (IOException ex) {
                    callback.onExecute("Something was wrong");
                    ex.printStackTrace();

                }
            });
            booleanCallback.some(false);
        }, "installationThread");

        installation.start();
    }

    public void uninstall(ProcessCallback callback) {
        if (devices.size() == 0) {
            callback.onExecute("No connected devices. Check devices and try initialize.");
            return;
        }

        Thread uninstallation = new Thread(() -> {
            devices.forEach((s, s2) -> {
                System.out.println("Start uninstallation for " + s);
                BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.uninstall(s));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Line " + line);
                        callback.onExecute(s + "  -  " + line);
                    }
                } catch (IOException ex) {
                    callback.onExecute("Something was wrong");
                    ex.printStackTrace();

                }
            });
        }, "uninstallationThread");

        uninstallation.start();

    }

    public void startApp(ProcessCallback callback) {
        if (devices.size() == 0) {
            callback.onExecute("No connected devices. Check devices and try initialize.");
            return;
        }

        Thread launch = new Thread(() -> {
            devices.forEach((s, s2) -> {
                System.out.println("Start Flex for " + s);
                BufferedReader reader = CommandExecutor.executeCommand(AdbCommands.launch(s));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("Line ** No activities found to run, monkey aborted")) {
                            callback.onExecute("Application not installed");
                        } else {
                            callback.onExecute("Application run on " + s);
                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();

                }
            });
        }, "launchThread");
        launch.start();
    }

    public void checkLog(String path, ProcessCallback callback) {
        Thread parserThread = new Thread(() -> {
            String resultParser = LogParser.findExeption(path);
            callback.onExecute(resultParser);
        }, "parserThread");

        parserThread.start();
    }

    public void startLogRecord(ProcessCallback callback) {
        if (devices.size() == 0) {
            callback.onExecute("No connected devices. Check devices and try initialize.");
            return;
        }
        File folder = new File(System.getProperty("user.home") + "\\Documents\\Log_TFa\\");

        executorLoggerService = Executors.newFixedThreadPool(4);

        devices.forEach((s, s2) -> {
            executorLoggerService.execute(() -> {
                        String logFile = System.getProperty("user.home") + "\\Documents\\Log_TFa\\log_" + s + ".txt";
                        Process process = null;
                        try {
                            process = Runtime.getRuntime().exec(AdbCommands.logcat(s, logFile));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("start log fo " + s);
                    }
            );
            System.out.println("Is shutDown after start " + executorLoggerService.isShutdown());
        });

    }

    public void stopLogRecord(ProcessCallback callback) {
        if (devices.size() == 0) {
            callback.onExecute("No connected devices. Check devices and try initialize.");
            return;
        }
        executorLoggerService.shutdownNow();
        System.out.println("Is shutDown after down " + executorLoggerService.isShutdown());

    }
}
