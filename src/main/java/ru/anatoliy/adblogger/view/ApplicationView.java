package ru.anatoliy.adblogger.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import ru.anatoliy.adblogger.presenter.Presenter;

public class ApplicationView extends BorderPane {
    @Setter
    private Presenter presenter;
    private TextArea centerTextArea;

    public void build() {
        Pane rightPane = buildRightPane();
        Pane leftPane = buildLeftPane();
        Pane bottomPane = buildBottomPane();
        centerTextArea = buildLogOutputTextArea();

        setRight(rightPane);
        setLeft(leftPane);
        setBottom(bottomPane);
        setCenter(centerTextArea);

        getStyleClass().add("app-view");
    }

    private Pane buildRightPane() {
        Button parseLogFileButton = new Button("Parse log file");
        parseLogFileButton.setTooltip(new Tooltip("Choose log file and find FATAL EXEPTION"));
        parseLogFileButton.setOnAction(event -> presenter.checkLog());
        Label title = new Label("Parser log");

        VBox rightPane = new VBox(title, parseLogFileButton);
        rightPane.getStyleClass().add("control-pane");
        return rightPane;
    }

    private Pane buildLeftPane() {
        Button checkDeviceButton = new Button("Check devices");
        checkDeviceButton.setTooltip(new Tooltip("Check connected devices"));
        checkDeviceButton.setOnAction(event -> presenter.checkConnectedDevices());

        Button setPathButton = new Button("Set path");
        setPathButton.setTooltip(new Tooltip("Choose application's path"));
        setPathButton.setOnAction(event -> presenter.setApkPath());

        Button installAppButton = new Button("Install app");
        installAppButton.setTooltip(new Tooltip("Start installation T.Flex"));
        installAppButton.setOnAction(event -> presenter.installApk(installAppButton::setDisable));

        Button setAppSettingsButton = new Button("Set settings");
        setAppSettingsButton.setTooltip(new Tooltip("Set settings for each devices"));

        Button runAppButton = new Button("Run app");
        runAppButton.setTooltip(new Tooltip("Start T.Flex"));
        runAppButton.setOnAction(event -> presenter.startApp());

        Button uninstallAppButton = new Button("Uninstall app");
        uninstallAppButton.setTooltip(new Tooltip("Uninstall T.Flex from all devices"));
        uninstallAppButton.setOnAction(event -> presenter.uninstallApk());

        Label title = new Label("Parser log");

        VBox leftPane = new VBox(title,
                checkDeviceButton,
                setPathButton,
                installAppButton,
                setAppSettingsButton,
                runAppButton,
                uninstallAppButton);
        leftPane.getStyleClass().add("control-pane");
        return leftPane;
    }

    private Pane buildBottomPane() {
        Button startLoggerButton = new Button("Start logging");
        startLoggerButton.setTooltip(new Tooltip("Start logging for all connected devices"));
        startLoggerButton.setOnAction(event -> presenter.startLogger());

        Button stopLoggerButton = new Button("Stop logging");
        stopLoggerButton.setTooltip(new Tooltip("Stop logging for all connected devices"));
        stopLoggerButton.setOnAction(event -> presenter.stopLogger());

        HBox center = new HBox(startLoggerButton, stopLoggerButton);
        center.getStyleClass().add("control-pane");
        return center;

    }


    private TextArea buildLogOutputTextArea() {
        TextArea logOutput = new TextArea();
        logOutput.setPadding(new Insets(5, 5, 5, 5));
        logOutput.setEditable(false);
        return logOutput;
    }

    public void setTextOnTextArea(String s, String style) {
        centerTextArea.setText(s);
        centerTextArea.setStyle(style);
    }

    public void setTextOnTextArea(String s) {
        centerTextArea.setText(s);
    }


    public void appendTextOnTextArea(String s, String style) {
        centerTextArea.setStyle(style);
        centerTextArea.appendText(s + "\n");
    }

    public void clearCenterTextArea() {
        centerTextArea.clear();
    }
}
