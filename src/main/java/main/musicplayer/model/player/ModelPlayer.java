package main.musicplayer.model.player;


import com.mpatric.mp3agic.Mp3File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private VBox box;
    private AnchorPane scrollsAnchorPane;
    //File failik = new File("C:\\Users\\pvare\\Desktop\\Music\\NotAddedToYaMusic\\Full Moon Full Life.mp3");

    public ModelPlayer(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setMouseClicked(boolean mouseClicked) {
        isMouseClicked = mouseClicked;
    }

    public void init(Slider durationSlider, Slider volumeSlider, ImageView imageView, Label songName, Label durationLabel, VBox box, AnchorPane scrollsAnchorPane) {
        this.scrollsAnchorPane = scrollsAnchorPane;
        this.box = box;
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

            if (durationSlider.getValue() >= 99.95) { // Слайдер не всегда хочет доходить ровно до 100
                nextSong();
            }
        });
        updateSongInfo(mp3File);
        updateSongPanel();
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
    public void chooseSong(String id) {
        File file = playlist.setFirstSong(id);
        changeSong(file);
    }

    private void changeSong(File file) {
        if (file != null) {
            Mp3File mp3File = null;
            Media media = new Media(file.toURI().toString());
            mediaPlayer.stop();
            try {
                mp3File = new Mp3File(file);
                player(media, mp3File);
                mediaPlayer.play();
                playlist.updatePlaylist();
            } catch (Exception e) {
                nextSong();
                //e.printStackTrace();
            }

        }
        updateSongPanel();
    }
    public void songPanel() {
        List<File> list = playlist.getPlaylist();
        int i = 0;
        clearSongPanel();

        for (File file : list) {
            i++;
            scrollsAnchorPane.setPrefSize(800, i * 25);

            HBox hbox = new HBox();
            hbox.setPrefSize(1000,  24);

            Label id = new Label(String.valueOf(i));
            id.setPrefSize(32,24);
            id.setAlignment(Pos.CENTER);

            Button up = getUpBut(i);
            Button down = getDownBut(i);
            Button song = getSongBut(file, i);
            Button del = getDelBut(i, song);

            hbox.getChildren().setAll(id, up, down, song, del);
            box.getChildren().add(hbox);
        }

    }

    private Button getSongBut(File file, int i) {
        Button song = new Button(file.getName());
        song.setPrefSize(600, 24);
        song.setAlignment(Pos.CENTER_LEFT);
        song.setId(String.valueOf(i));
        if (i <= 1) {
            song.setDisable(true);
        }
        song.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                chooseSong(song.getId());
                updateSongPanel();
            }
        });
        return song;
    }

    private Button getDelBut(int i, Button song) {
        Button del = new Button("Del");
        del.setPrefSize(48, 24);
        del.setAlignment(Pos.CENTER_LEFT);
        del.setId(String.valueOf(i));
        if (i <= 1) {
            del.setVisible(false);
        }
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removeSong(song.getId());
                updateSongPanel();
            }
        });
        return del;
    }

    private Button getDownBut(int i) {
        Button down = new Button("Down");
        down.setPrefSize(48, 24);
        down.setAlignment(Pos.CENTER_LEFT);
        down.setId(String.valueOf(i));

        if (i <= 1 || i == playlist.getPlaylist().size()) {
            down.setVisible(false); // Костыль
        }

        down.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playlist.removeDown(down.getId());
                updateSongPanel();
            }
        });
        return down;
    }

    private Button getUpBut(int i) {
        Button up = new Button("Up");
        up.setPrefSize(48, 24);
        up.setAlignment(Pos.CENTER_LEFT);
        up.setId(String.valueOf(i));
        if (i <= 2) {
            up.setVisible(false);   // Костыль
        }
        up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playlist.removeUp(up.getId());
                updateSongPanel();
            }
        });
        return up;
    }

    private void removeSong(String id) {
        File file = playlist.getSong(Integer.parseInt(id));
        playlist.removeSong(file);
    }
    private void clearSongPanel() {
        box.getChildren().clear();
    }
    private void updateSongPanel() {
        songPanel();
    }
    public void shufflePlaylist() {
        playlist.shufflePlaylist();
        updateSongPanel();
    }
    public void createPlaylist() {
        playlist.createPlaylist();
        updateSongPanel();
        changeSong(playlist.getFirstSong());
    }
}
