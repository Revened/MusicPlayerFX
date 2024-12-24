package main.musicplayer.model.musicRename;

import com.mpatric.mp3agic.Mp3File;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.musicplayer.model.Model;
import main.musicplayer.model.ModelApp;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class ModelMusicRename extends ModelApp implements Model, FileVisitor {
    private Path startFolder;
    private Path endFolder;
    private boolean triggerWriter = false;

    private List<String> formatNames = new ArrayList<>();
    private List<Path> oldPath = new ArrayList<>();


    public void init(Label musicRenameStartLabel, Button musicRenameStartButton, Label musicRenameEndLabel) {
        boolean trigger = properties.get("settingsDefaultFolderForReadIsChosen").equals("true");
        if (trigger) {
            String str = properties.getProperty("defaultFolderForRead");
            startFolder = Path.of(str);
            musicRenameStartLabel.setText(str);
            musicRenameStartButton.setDisable(false);
            initPathToWrite(musicRenameEndLabel);
        } else if (startFolder != null) {
            musicRenameStartLabel.setText(startFolder.toString());
            musicRenameStartButton.setDisable(false);
            initPathToWrite(musicRenameEndLabel);
        } else {
            musicRenameStartLabel.setText("file not selected");
            musicRenameStartButton.setDisable(true);
        }
    }

    public void updateLabels(Label label) {
        switch (label.getId()) {
            case "musicRenameStartLabel":
                label.setText(String.valueOf(startFolder));
                break;
            case "musicRenameEndLabel":
                label.setText(String.valueOf(endFolder));
                break;
        }
    }

    private void initPathToWrite(Label musicRenameEndButton) {
        triggerWriter = properties.get("settingsDefaultFolderForWriteIsChosen").equals("true");
        if (triggerWriter) {
            String str = properties.getProperty("defaultFolderForWrite");
            endFolder = Path.of(str);
            musicRenameEndButton.setText(endFolder.toString());
        } else {
            endFolder = startFolder;
            musicRenameEndButton.setText(endFolder.toString());
        }
    }

    public void chooseStartFolder() {
        chooseFolder(startFolder);
    }

    public void chooseEndFolder() {
        chooseFolder(endFolder);
    }

    private void chooseFolder(Path folder) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        try {
            if (folder == startFolder) {
                startFolder = directoryChooser.showDialog(new Stage()).toPath();
            } else if (folder == endFolder) {
                endFolder = directoryChooser.showDialog(new Stage()).toPath();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void renameFiles() {
        Set<FileVisitOption> options = new HashSet<>();
        try {
            Files.walkFileTree(startFolder, options, 1, this);
            //visitFile(path, null);
        } catch (Exception e) {
            System.out.println(e);
        }
        createFiles();
    }

    private void createFiles() {
        try {
            for (int i = 0; i < formatNames.size(); i++) {
                Path path = Path.of(oldPath.get(i).getParent() + "\\" + formatNames.get(i));
                if (triggerWriter) {
                    if (oldPath.get(i).toFile().renameTo(new File(endFolder.toFile() + "\\" + formatNames.get(i)))) {
                        //место под вывод информиции о создании файла
                    } else {

                    }
                } else {
                    if (oldPath.get(i).toFile().renameTo(path.toFile())) { // Файлы с одинаковым названием созданы не будут
                        //System.out.println("Файл: " + ConsoleOutput.ANSI_GREEN + oldPath.get(i) + " был переименован" +  ConsoleOutput.RESET_COLOR + "\t Новое имя: " + path.getFileName());
                    } else {
                        //System.out.println("Файл: " + ConsoleOutput.ANSI_RED + oldPath.get(i) + " не был переименован" + ConsoleOutput.RESET_COLOR);
                    }
                }
            }

        } catch (Exception e) {
            //System.out.println(e);
        }
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        if (!attrs.isDirectory()) {
            Path path = Path.of(file.toString());
            if (file.toString().endsWith("mp3")) {
                try /*(InputStream input = new FileInputStream(path.toFile()))*/ {
                    Mp3File song = new Mp3File(String.valueOf(file));                         //mp3agic
                    HashMap hash = getSongInfo(song);

                    if (hash != null ) {
                        uploadInfo(path, StringFormatter.formatArtistTitle(hash.get("artist").toString(), hash.get("songName").toString()), getImage(song));
                    }

                    /*if (song.hasId3v1Tag()){
                        hash = id3v1Tag(song.getId3v1Tag());
                        uploadInfo(path, StringFormatter.formatArtistTitle(hash.get("artist").toString(), hash.get("songName").toString()));
                    } else if (song.hasId3v2Tag()) {
                        hash = id3v2Tag(song.getId3v2Tag());
                        uploadInfo(path, StringFormatter.formatArtistTitle(hash.get("artist").toString(), hash.get("songName").toString()), (byte[]) hash.get("image"));
                    }*/
                    /*ContentHandler handler = new DefaultHandler();                        //apache.tika v1.4
                    Metadata metadata = new Metadata();
                    Parser parser = new Mp3Parser();
                    ParseContext parseCtx = new ParseContext();
                    parser.parse(input, handler, metadata, parseCtx);

                    String artist = metadata.get("xmpDM:artist");
                    String songName = metadata.get("title");

                    uploadInfo(path, StringFormatter.formatArtistTitle(artist, songName)); // Отправка инфы для создания файла*/

                    return FileVisitResult.CONTINUE;
                } catch (Exception e) {
                    //System.out.println(ConsoleOutput.ANSI_RED + e + ConsoleOutput.RESET_COLOR);
                    return FileVisitResult.CONTINUE;
                }
            } else { // если формат не mp3
                //System.out.println(ConsoleOutput.ANSI_RED + StringFormatter.getFileName(file) + ConsoleOutput.RESET_COLOR + " Имеет неверный формат");
                return FileVisitResult.CONTINUE;
            }
        }
        return FileVisitResult.CONTINUE; // если не файл
    }


    private void uploadInfo(Path oldPath, String list) {
        this.oldPath.add(oldPath);
        formatNames.add(list);
    }

    private void uploadInfo(Path oldPath, String list, Image image) {
        this.oldPath.add(oldPath);
        formatNames.add(list);
        //не реализованно
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
