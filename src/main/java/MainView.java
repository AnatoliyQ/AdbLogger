import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import lombok.Setter;

public class MainView extends BorderPane {
    @Setter
    private MainPresenter mainPresenter;
    private TextArea centerTextArea;

    void build() {
        VBox rightBox = rightVBox();
        setRight(rightBox);

        VBox leftBox = leftVbox();
        setLeft(leftBox);

        HBox centerBottomHbox = centerHbox();
        centerBottomHbox.setAlignment(Pos.CENTER);
        setBottom(centerBottomHbox);

        centerTextArea = centerTextArea();
        setCenter(centerTextArea);


        setStyle("-fx-background-color: #1d1d1d;");
        setPadding(new Insets(15, 20, 15, 10));
    }

    private VBox rightVBox() {
        Button bParseOneFile = new MainButtons.ButtonsBuilder("Parse log file").
                myTooltip("Choose log file and find FATAL EXEPTION").
                build();
        bParseOneFile.setOnAction(event -> mainPresenter.checkLog());

        Button test = new MainButtons.ButtonsBuilder("TEST").build();


        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(10);

        Text title = new Text("Parser log");
        title.setId("welcome-text");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        title.setTextAlignment(TextAlignment.CENTER);

        vbox.getChildren().addAll(title, bParseOneFile, test);

        return vbox;
    }

    private VBox leftVbox() {
        Button bCheckDevices = new MainButtons.ButtonsBuilder("Check devices").myTooltip("Check connected devices").build();
        bCheckDevices.setOnAction(event -> mainPresenter.checkConnectedDevices());

        Button bSetPath = new MainButtons.ButtonsBuilder("Set path").myTooltip("Choose application's path").build();
        bSetPath.setOnAction(event -> mainPresenter.setApkPath());

        Button bInstall = new MainButtons.ButtonsBuilder("Install app").myTooltip("Start installation T.Flex").build();
        bInstall.setOnAction(event -> {
            mainPresenter.installApk(bInstall::setDisable);
        });

        Button bSettings = new MainButtons.ButtonsBuilder("Set settings").myTooltip("Set settings for each devices").build();

        Button bRunApp = new MainButtons.ButtonsBuilder("Run App").myTooltip("Start T.Flex and connect to server").build();
        bRunApp.setOnAction(event -> {
            mainPresenter.startApp();
        });

        Button bUninstallApp = new MainButtons.ButtonsBuilder("Uninstall App").myTooltip("Uninstall T.Flex from all devices").build();
        bUninstallApp.setOnAction(event -> mainPresenter.uninstallApk());

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(10);

        Text title = new Text("App management");
        title.setId("welcome-text");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        title.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().addAll(title, bCheckDevices, bSetPath, bInstall, bSettings, bRunApp, bUninstallApp);

        return vbox;
    }

    private HBox centerHbox() {
        Button bStartLogger = new MainButtons.ButtonsBuilder("Start logging")
                .myTooltip("Start logging for all connected devices")
                .build();
        bStartLogger.setOnAction(event -> mainPresenter.startLogger());

        Button bStopLogger = new MainButtons.ButtonsBuilder("Stop logging")
                .myTooltip("Stop logging for all connected devices")
                .build();

        bStopLogger.setOnAction(event -> mainPresenter.stopLogger());

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(20));
        hbox.setSpacing(10);

        hbox.getChildren().addAll(bStartLogger, bStopLogger);
        return hbox;
    }

    private TextArea centerTextArea() {
        TextArea logOutput = new TextArea();
        logOutput.setPadding(new Insets(5, 5, 5, 5));
        logOutput.setEditable(false);
//            logOutput.setEffect(dropShadow());
        return logOutput;
    }

    public void setTextOnTextArea(String s, String style){
        centerTextArea.setText(s);
        centerTextArea.setStyle(style);
    }

    public void setTextOnTextArea(String s){
        centerTextArea.setText(s);
    }



    public void appendTextOnTextArea(String s, String style){
        centerTextArea.setStyle(style);
        centerTextArea.appendText(s+"\n");
    }

    public void clearCenterTextArea(){
        centerTextArea.clear();
    }
}
