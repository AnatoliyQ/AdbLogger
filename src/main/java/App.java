import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView root = new MainView();
        root.setMainPresenter(new MainPresenter(root, new MainModel()));
        root.build();

        final Scene scene = new Scene(root, 800, 600);


        primaryStage.setTitle("ADB Logger");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add("main.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

