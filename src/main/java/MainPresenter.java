import javafx.stage.FileChooser;
import util.StyleTextColor;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainPresenter {
    private MainView mainView;
    private MainModel mainModel;

    public MainPresenter(MainView mainView, MainModel mainModel) {
        this.mainModel = mainModel;
        this.mainView = mainView;
    }

    public void setApkPath() {
        File apkFile = customFileChoser("apk files (*.apk)", "*.apk", "Select apk file");
        if (!(apkFile ==null)) {
            String apkPath = apkFile.getPath();
            mainModel.setApkPath(apkPath);
        }
    }

    public void checkConnectedDevices() {
        mainView.clearCenterTextArea();
        try {
            mainModel.checkDevices();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, String> devicesMap = mainModel.getDevices();

        if (!(devicesMap.size() == 0)) {
            devicesMap.forEach((s, s2) -> {
                mainView.appendTextOnTextArea(s + " - " + s2, StyleTextColor.GREEN);
            });
        } else {
            mainView.setTextOnTextArea("No connected devices", StyleTextColor.RED);
        }
    }

    public void installApk(BooleanCallback bCallBack) {
        mainModel.installApk(bCallBack, result -> {
            mainView.appendTextOnTextArea(result, StyleTextColor.GREEN);
        });
    }

    public void uninstallApk() {
        mainModel.uninstall(result -> mainView.appendTextOnTextArea(result, StyleTextColor.GREEN));
    }

    public void startApp(){
        mainModel.startApp(result -> {
            if(result.equals("Line ** No activities found to run, monkey aborted")){
                mainView.setTextOnTextArea("Application not installed", StyleTextColor.RED );
            }
            mainView.setTextOnTextArea(result, StyleTextColor.RED);
        });
    }

    public void checkLog() {
        File logFile = customFileChoser("TXT files (*.txt)", "*.txt", "Select log file");
        if (!(logFile==null)) {
            String logPath = logFile.getPath();
            System.out.println("Log file path " + logPath);

            mainModel.checkLog(logPath, result -> {
                if (result.length() == 10) {
                    mainView.setTextOnTextArea("Exeption not found", StyleTextColor.GREEN);
                } else {
                    mainView.setTextOnTextArea(result, StyleTextColor.RED);
                }
            });
        }
    }

    public void startLogger(){
        mainModel.startLogRecord(result -> {
            mainView.setTextOnTextArea(result, StyleTextColor.RED);
        });
    }

    public void stopLogger(){
        mainModel.stopLogRecord(result -> {
            mainView.setTextOnTextArea(result, StyleTextColor.RED);
        });
    }

    private File customFileChoser(String description, String extensions, String title) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                description, extensions);

        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle(title);

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Documents"));
        File file = fileChooser.showOpenDialog(mainView.getScene().getWindow());
        return file;
    }



}
