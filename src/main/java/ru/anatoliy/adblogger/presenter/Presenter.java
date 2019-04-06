package ru.anatoliy.adblogger.presenter;

import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.anatoliy.adblogger.service.Service;
import ru.anatoliy.adblogger.util.StyleTextColor;
import ru.anatoliy.adblogger.view.ApplicationView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public class Presenter {
    private ApplicationView applicationView;
    private Service service;
    private static final Logger logger = LoggerFactory.getLogger(Presenter.class);

    public Presenter(ApplicationView applicationView, Service service) {

        this.service = service;
        this.applicationView = applicationView;
    }

    public void setApkPath() {
        File apkFile = getCustomFileChooser("apk files (*.apk)", "*.apk", "Select apk file");
        if (!(apkFile == null)) {
            String apkPath = apkFile.getPath();
            logger.debug("Apk path {}", apkPath);
            service.setApkPath(apkPath);
        }
    }

    public void checkConnectedDevices() {
        applicationView.clearCenterTextArea();
        try {
            service.checkDevices();
        } catch (IOException e) {
            logger.error("Check device", e);
        }

        HashMap<String, String> devicesMap = service.getDevices();

        if (!(devicesMap.size() == 0)) {
            devicesMap.forEach((s, s2) -> {
                applicationView.appendTextOnTextArea(s + " - " + s2, StyleTextColor.GREEN);
            });
        } else {
            applicationView.setTextOnTextArea("No connected devices", StyleTextColor.RED);
        }
    }

    public void installApk(Consumer<Boolean> bCallBack) {
        service.installApk(bCallBack, result -> {
            applicationView.appendTextOnTextArea(result, StyleTextColor.GREEN);
        });
    }

    public void uninstallApk() {
        service.uninstall(result -> applicationView.appendTextOnTextArea(result, StyleTextColor.GREEN));
    }

    public void startApp() {
        service.startApp(result -> {
            if (result.equals("Line ** No activities found to run, monkey aborted")) {
                applicationView.setTextOnTextArea("Application not installed", StyleTextColor.RED);
            }
            applicationView.setTextOnTextArea(result, StyleTextColor.RED);
        });
    }

    public void checkLog() {
        File logFile = getCustomFileChooser("TXT files (*.txt)", "*.txt", "Select log file");
        if (!(logFile == null)) {
            String logPath = logFile.getPath();
            logger.debug("Log file path {}", logPath);

            service.checkLog(logPath, result -> {
                if (result.length() == 10) {
                    applicationView.setTextOnTextArea("Exception not found", StyleTextColor.GREEN);
                } else {
                    applicationView.setTextOnTextArea(result, StyleTextColor.RED);
                }
            });
        }
    }

    public void startLogger() {
        service.startLogRecord(result -> {
            applicationView.setTextOnTextArea(result, StyleTextColor.RED);
        });
    }

    public void stopLogger() {
        service.stopLogRecord(result -> {
            applicationView.setTextOnTextArea(result, StyleTextColor.RED);
        });
    }

    private File getCustomFileChooser(String description, String extensions, String title) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                description, extensions);

        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle(title);

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Documents"));
        return fileChooser.showOpenDialog(applicationView.getScene().getWindow());
    }
}
