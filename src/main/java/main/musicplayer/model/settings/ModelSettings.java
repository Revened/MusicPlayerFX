package main.musicplayer.model.settings;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.musicplayer.model.ModelApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;

public class ModelSettings extends ModelApp {

    private Properties tempProp = new Properties();
    public void init(CheckBox startCB, Button startB, CheckBox endCB, Button endB, Label... labels) {

        ArrayList<String> list = new ArrayList();
        properties.keySet().forEach(key -> {
            String str = properties.get(key).toString();
            list.add(str);
            tempProp.put(key, str);
        });
        startCB.setSelected("true".equals(list.get(1)));
        endCB.setSelected("true".equals(list.get(0)));
        checkBox(startCB, startB);
        checkBox(endCB, endB);
        try {
            for (int i = 0; i < labels.length; i++) {
                Label label = labels[i];
                label.setText(list.get(i+2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Path chooseFolder() {
        try {
            return new DirectoryChooser().showDialog(new Stage()).toPath();
        } catch (Exception e) {
            return null;
        }
    }
    public void folderForRead(Label label) {
        Path folder = chooseFolder();
        if (folder != null) {
            label.setText(folder.toString());
            tempProp.put("defaultFolderForRead", folder.toString());
        } else {

        }
    }
    public void folderForWrite(Label label) {
        Path folder = chooseFolder();
        if (folder != null) {
            label.setText(folder.toString());
            tempProp.put("defaultFolderForWrite", folder.toString());
        }
    }
    public void PathToPlaylist(Label label) {
        Path folder = chooseFolder();
        if (folder != null) {
            label.setText(folder.toString());
            tempProp.put("songsPathToPlayer", folder.toString());
        }
    }
    public void saveSettings() {
        String propertiesFolder = "src\\main\\resources\\main\\musicplayer\\settings.properties";
        properties = tempProp;
        try {
            properties.store(new FileOutputStream(propertiesFolder),"qqq");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String getDefaultValue(String key) {
        return properties.getProperty(key);
    }
    public void checkBox(CheckBox checkBox, Button button) {
        if (checkBox.isSelected()) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }
}
