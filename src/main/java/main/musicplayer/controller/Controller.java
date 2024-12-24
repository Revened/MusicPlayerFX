package main.musicplayer.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.musicplayer.MusicPlayerApp;
import main.musicplayer.model.musicRename.ModelMusicRename;
import main.musicplayer.model.player.ModelPlayer;
import main.musicplayer.model.player.Playlist;
import main.musicplayer.model.settings.ModelSettings;

import java.io.*;
import java.util.Properties;

public class Controller {
    private static final Playlist playlist = new Playlist();
    private static final ModelPlayer modelPlayer = new ModelPlayer(playlist);
    private static final ModelMusicRename modelMusicRename = new ModelMusicRename();
    private static final ModelSettings modelSettings = new ModelSettings();
    private String propertyFolder;

    @FXML
    private TextArea songNames;
    @FXML
    private Label songName;
    @FXML
    private Label durationLabel;
    @FXML
    private ImageView imageView;
    @FXML private VBox playlistVBox;
    @FXML private AnchorPane scrollsAnchorPane;

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
        Properties properties = new Properties();
        try {
            smallParser();
            properties.load(new FileReader(propertyFolder));    //Загрузка конфига и передача остальный классам
            playlist.setProperties(properties);
            modelPlayer.setProperties(properties);
            modelMusicRename.setProperties(properties);
            modelSettings.setProperties(properties); modelSettings.setPropertiesFolder(propertyFolder);

        } catch (Exception e) {
            System.out.println(e);
        }
        playlist.init(songNames);
        modelMusicRename.init(musicRenameStartLabel, musicRenameStartButton, musicRenameEndLabel);
        modelPlayer.init(durationSlider, volumeSlider, imageView, songName, durationLabel, playlistVBox, scrollsAnchorPane);
        modelSettings.init(settingsMRStartCheckBox, settingsMRStartButton, settingsMREndCheckBox, settingsMREndButton, settingsPlaylistLabel, settingsMREndFolderLabel, settingsMRStartFolderLabel);
    }
    private void smallParser() {
        propertyFolder = String.valueOf(MusicPlayerApp.class.getResource("settings.properties").getPath());       //через jar/exe
        StringBuilder sb = new StringBuilder();
        String[] str = propertyFolder.split("/");
        for (int i = 1; !str[i].contains("MusicPlayer"); i++) {
            if (!str[i].contains("file")) {
                sb.append(str[i]).append("\\");
            }
        }
        sb.append("settings.properties");
        propertyFolder = sb.toString();

        /*propertyFolder = "src/main/resources/main/musicplayer/settings.properties";*/
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
    @FXML
    private CheckBox settingsMRStartCheckBox;
    @FXML
    private CheckBox settingsMREndCheckBox;
    @FXML
    private Label settingsPlaylistLabel;
    @FXML
    private Label settingsMRStartFolderLabel;
    @FXML
    private Label settingsMREndFolderLabel;
    @FXML private Button settingsMRStartButton;
    @FXML private Button settingsMREndButton;
    @FXML
    private void settingsMRStartCheckBox(){
        modelSettings.checkBox(settingsMRStartCheckBox, settingsMRStartButton);
    }
    @FXML
    private void settingsMREndCheckBox(){
        modelSettings.checkBox(settingsMREndCheckBox, settingsMREndButton);
    }
    @FXML
    private void settingsPlaylistPathButton() {
        modelSettings.PathToPlaylist(settingsPlaylistLabel);
    }
    @FXML
    private void settingsMRStartFolderButton() {
        modelSettings.folderForRead(settingsMRStartFolderLabel);
    }
    @FXML
    private void settingsMREndFolderButton() {
        modelSettings.folderForWrite(settingsMREndFolderLabel);
    }
    @FXML
    private void saveSettings() {
        modelSettings.saveSettings();
        updateProgram();
    }
    @FXML
    private void createPlaylist() {
        modelPlayer.createPlaylist();
    }
    @FXML
    private void shufflePlaylist() {
        modelPlayer.shufflePlaylist();
    }
    @FXML
    private void updateProgram() {
        modelPlayer.stop();
        initialize();
    }
}
