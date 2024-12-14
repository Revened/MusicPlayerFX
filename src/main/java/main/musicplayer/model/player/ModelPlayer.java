package main.musicplayer.model.player;


import com.mpatric.mp3agic.Mp3File;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.musicplayer.model.Model;
import main.musicplayer.model.ModelApp;

import java.io.File;

public class ModelPlayer extends ModelApp implements Model {
    private MediaPlayer mediaPlayer;
    private boolean isMouseClicked = false;
    File failik = new File("C:\\Users\\pvare\\Desktop\\Music\\NotAddedToYaMusic\\Full Moon Full Life.mp3");

    public void setMouseClicked(boolean mouseClicked) {
        isMouseClicked = mouseClicked;
    }
    public void init(Slider durationSlider) {
        mediaPlayer = new MediaPlayer(new Media(new File(String.valueOf(failik)).toURI().toString()));

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
    public void showImage(ImageView imageView) {
        File file2 = new File("C:\\Users\\pvare\\Desktop\\test.jpg");
        try {
            imageView.setImage(getImage(new Mp3File(new File(String.valueOf(failik))).getId3v2Tag()));
        } catch (Exception e) {

        }
        //Image image = new Image();
        //imageView.setImage(image);
    }

}
