package main.musicplayer.model.player;

import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.musicplayer.model.Model;

import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Properties;

public class ModelPlayer implements Model {
    private MediaPlayer mediaPlayer;
    private Properties properties;
    private boolean isMouseClicked = false;

    public void setMouseClicked(boolean mouseClicked) {
        isMouseClicked = mouseClicked;
    }
    @Override
    public void setProperties(Properties prop) {
        properties = prop;
    }
    public void init(Slider durationSlider) {
        mediaPlayer = new MediaPlayer(new Media(new File("C:\\Users\\pvare\\Desktop\\Music\\NotAddedToYaMusic\\Full Moon Full Life.mp3").toURI().toString()));
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> { //передвижение слайдера во время проигрывания песни
            if (!isMouseClicked) {
                double songDuration = mediaPlayer.getTotalDuration().toSeconds();
                durationSlider.setValue(newValue.toSeconds() / songDuration * 100);
                //System.out.println("0000");
            } else {
                //System.out.println("111111");
            }

            /*System.out.println(newValue.toSeconds() / songDuration * 100);*/
        });
    }
    public void toggleButton(ToggleButton toggleButton) {
        if (toggleButton.isSelected()) {
            mediaPlayer.play();
            toggleButton.setText("Stop");
        } else {
            mediaPlayer.pause();
            toggleButton.setText("Play");
        }
    }
    public void setVolume(Slider volume) {
        double value = volume.getValue() / 100 / 1.5;
        mediaPlayer.setVolume(value);
        System.out.println("Volume set to " + value);
    }
    public void durationSlider(Slider durationSlider) {
        double songDuration = mediaPlayer.getTotalDuration().toSeconds();
        mediaPlayer.seek(Duration.seconds(durationSlider.getValue() * songDuration / 100));
        /*double value = durationSlider.getValue() * songDuration / 100 ;
        System.out.println("Value set to " + value);*/
    }
}
