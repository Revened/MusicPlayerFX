package main.musicplayer.model.player;


import com.mpatric.mp3agic.Mp3File;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.musicplayer.model.Model;
import main.musicplayer.model.ModelApp;

import java.io.File;
import java.util.*;

public class ModelPlayer extends ModelApp implements Model {
    private MediaPlayer mediaPlayer;
    private boolean isMouseClicked = false;
    private double songDuration = 3.1415926535;
    private final Playlist playlist;
    private Slider durationSlider;
    private Slider volumeSlider;
    private ImageView imageView;
    private Label songName;
    private Label durationLabel;
    //File failik = new File("C:\\Users\\pvare\\Desktop\\Music\\NotAddedToYaMusic\\Full Moon Full Life.mp3");

    public ModelPlayer(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setMouseClicked(boolean mouseClicked) {
        isMouseClicked = mouseClicked;
    }

    public void init(Slider durationSlider, Slider volumeSlider, ImageView imageView, Label songName, Label durationLabel) {
        this.durationSlider = durationSlider;
        this.volumeSlider = volumeSlider;
        this.imageView = imageView;
        this.songName = songName;
        this.durationLabel = durationLabel;

        File file = playlist.getFirstSong();
        if (file != null) {
            Media media = new Media(file.toURI().toString());

            Mp3File mp3File = null;
            try {
                mp3File = new Mp3File(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player(media, mp3File);

            playlist.showPlaylist();
        }

    }
    public void stop() {
        mediaPlayer.stop();
    }
    private void player(Media media, Mp3File mp3File) {
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> { //передвижение слайдера во время проигрывания песни
            if (!isMouseClicked) {
                songDuration = mediaPlayer.getTotalDuration().toSeconds();
                durationSlider.setValue(newValue.toSeconds() / songDuration * 100);
                durLabelParser(newValue.toSeconds(), songDuration);
            }

            if (newValue.toSeconds() >= mediaPlayer.getTotalDuration().toSeconds()) {
                nextSong();
            }
        });
        updateSongInfo(mp3File);
    }

    private void updateSongInfo(Mp3File song) {
        try {
            HashMap hashMap = getSongInfo(song);
            songName.setText(hashMap.get("songName").toString() + "\n" + hashMap.get("artist").toString());
            imageView.setImage(getImage(song));
            setVolume();
        } catch (Exception e) {

        }
    }

    private void durLabelParser(double dur, double songDur) {
        int sec = (int) dur % 60;
        int min = (int) dur / 60;
        int sec2 = (int) (songDur % 60);
        int min2 = (int) (songDur / 60);
        String secStr = String.format("%02d", sec);
        String sec2Str = String.format("%02d", sec2);
        durationLabel.setText(min + ":" + secStr + "/" + min2 + ":" + sec2Str);
    }

    public void toggleButton(ToggleButton toggleButton) {
        if (toggleButton.isSelected()) {
            mediaPlayer.play();
            toggleButton.setText("||");
        } else {
            mediaPlayer.pause();
            toggleButton.setText("➤");
        }
    }

    public void setVolume() {
        double value = volumeSlider.getValue() / 100 / 1.5;
        mediaPlayer.setVolume(value);
        //System.out.println("Volume set to " + value);
    }
    @Deprecated
    public void durationSlider() {
        double songDuration = mediaPlayer.getTotalDuration().toSeconds();
        mediaPlayer.seek(Duration.seconds(durationSlider.getValue() * songDuration / 100));
    }

    public void nextSong() {
        File file = playlist.nextSong();
        changeSong(file);
    }

    public void previousSong() {
        File file = playlist.previousSong();
        changeSong(file);
    }

    private void changeSong(File file) {
        if (file != null) {
            Mp3File mp3File = null;
            Media media = new Media(file.toURI().toString());
            try {
                mp3File = new Mp3File(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.stop();
            player(media, mp3File);
            mediaPlayer.play();
            playlist.updatePlaylist();
        }
    }
}
