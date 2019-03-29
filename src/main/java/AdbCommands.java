
public class AdbCommands {
    public static String install(String deviceID, String apkPath){
        return "adb -s " + deviceID + " install -r " + apkPath;
    }

    public static String devices (){
        return "adb devices";
    }

    public static String uninstall (String deviceID){
        return "adb -s " + deviceID + " uninstall com.tassta.flex";
    }

    public static String launch(String deviceID){
        return "adb -s " + deviceID + " shell monkey -p com.tassta.flex -c android.intent.category.LAUNCHER 1";
    }

    public static String logcat(String deviceID, String logFile){
        return "adb -s " + deviceID + " -d logcat com.tassta.flex:V > "+logFile;
    }

}
