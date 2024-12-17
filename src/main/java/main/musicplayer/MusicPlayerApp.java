package main.musicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MusicPlayerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MusicPlayerApp.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1140, 740);

        /*Controller controller = fxmlLoader.getController();

        scene.setOnMousePressed(mouseEvent -> controller.isMouseClicked(true));
        scene.setOnMouseReleased((EventHandler) -> controller.isMouseClicked(false));*/
        AnchorPane pane = new AnchorPane();
        Button b = new Button("test");
        //pane.getChildren().add(b);
        stage.setTitle("MusicPlayer");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}