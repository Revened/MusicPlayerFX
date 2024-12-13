package main.musicplayer.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.MediaView;
import main.musicplayer.model.ModelApp;
import main.musicplayer.model.musicRename.ModelMusicRename;
import main.musicplayer.model.player.ModelPlayer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Controller {
    private static final Properties properties;
    private static final ModelApp modelApp = new ModelApp();
    private static final ModelPlayer modelPlayer = new ModelPlayer();
    private static final ModelMusicRename modelMusicRename = new ModelMusicRename();
    private static final String propertiesFolder = "src\\main\\resources\\main\\musicplayer\\settings.properties";

    public void isMouseClicked(boolean q) {
        modelPlayer.setMouseClicked(q);
    }

    static {
        try {
            properties = new Properties();                          //Загрузка конфига и передача остальный классам
            properties.load(new FileReader(propertiesFolder));
            modelApp.setProperties(properties);
            modelPlayer.setProperties(properties);
            modelMusicRename.setProperties(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private MediaView mediaView;
    @FXML
    private void playButton() {
        modelPlayer.toggleButton(tb);
    }
    @FXML
    private Slider volumeSlider;
    @FXML
    private void volumeSliderAction() {
        modelPlayer.setVolume(volumeSlider);
    }
    @FXML
    private Slider durationSlider;
    @FXML
    private void durationSliderAction() {
        modelPlayer.durationSlider(durationSlider);
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
        modelMusicRename.init(musicRenameStartLabel, musicRenameStartButton, musicRenameEndLabel);
        modelPlayer.init(durationSlider);
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

}
