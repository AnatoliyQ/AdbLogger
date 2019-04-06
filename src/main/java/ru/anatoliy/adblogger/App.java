package ru.anatoliy.adblogger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.anatoliy.adblogger.presenter.Presenter;
import ru.anatoliy.adblogger.service.Service;
import ru.anatoliy.adblogger.view.ApplicationView;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationView root = new ApplicationView();
        root.setPresenter(new Presenter(root, new Service()));
        root.build();

        final Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("css/style.css");

        primaryStage.setTitle("ADB Logger");
        primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/logo.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

