package main.musicplayer;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.musicplayer.controller.Controller;

import java.io.IOException;

public class MusicPlayerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MusicPlayerApp.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 640);

        /*Controller controller = fxmlLoader.getController();

        scene.setOnMousePressed(mouseEvent -> controller.isMouseClicked(true));
        scene.setOnMouseReleased((EventHandler) -> controller.isMouseClicked(false));*/

        stage.setTitle("MusicPlayer");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}