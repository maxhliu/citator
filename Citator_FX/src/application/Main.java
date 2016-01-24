package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    static javafx.scene.Scene scene;

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("library_view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Hello World");
        scene = new javafx.scene.Scene(root, 1000, 650);
        scene.getStylesheets().add(
                getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shakes Pear");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
