package main.musicplayer.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.musicplayer.model.musicRename.ModelMusicRename;
import main.musicplayer.model.player.ModelPlayer;
import main.musicplayer.model.player.Playlist;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Controller {
    private static final Playlist playlist = new Playlist();
    private static final ModelPlayer modelPlayer = new ModelPlayer(playlist);
    private static final ModelMusicRename modelMusicRename = new ModelMusicRename();
    private static final String propertiesFolder = "src\\main\\resources\\main\\musicplayer\\settings.properties";
    @FXML
    private TextArea songNames;
    @FXML
    private Label songName;
    @FXML
    private Label durationLabel;
    @FXML
    private ImageView imageView;

    static {
        try {
            Properties properties = new Properties();                          //Загрузка конфига и передача остальный классам
            properties.load(new FileReader(propertiesFolder));
            playlist.setProperties(properties);

            modelPlayer.setProperties(properties);
            modelMusicRename.setProperties(properties);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void playButton() {
        modelPlayer.toggleButton(tb);
    }

    @FXML
    private Slider volumeSlider;

    @FXML
    private void volumeSliderAction() {
        modelPlayer.setVolume();
    }

    @FXML
    private Slider durationSlider;

    @FXML
    private void durationSliderAction() {
        modelPlayer.durationSlider();
        modelPlayer.setMouseClicked(false);
    }

    @FXML
    private void durationSliderActionWhileMove() {
        modelPlayer.setMouseClicked(true);
    }

    @FXML
    private ToggleButton tb;
    @FXML
    private Tab player;
    @FXML
    private Tab musicRename;
    @FXML
    private Button musicRenameStartButton;
    @FXML
    private Label musicRenameStartLabel;
    @FXML
    private Label musicRenameEndLabel;

    @FXML
    private void initialize() {
        playlist.init(songNames);
        modelMusicRename.init(musicRenameStartLabel, musicRenameStartButton, musicRenameEndLabel);
        modelPlayer.init(durationSlider, volumeSlider, imageView, songName, durationLabel);
    }

    @FXML
    private void ChooseFolderOnButtonClick() {
        modelMusicRename.chooseStartFolder();
        modelMusicRename.updateLabels(musicRenameStartLabel);
    }

    @FXML
    private void startRename() {
        modelMusicRename.renameFiles();
    }

    @FXML
    private void ChooseEndFolderOnButtonClick() {
        modelMusicRename.chooseEndFolder();
        modelMusicRename.updateLabels(musicRenameEndLabel);
    }

    @FXML
    private void nextSongButton() {
        modelPlayer.nextSong();
    }

    @FXML
    private void previousSongButton() {
        modelPlayer.previousSong();
    }
}
