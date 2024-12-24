package main.musicplayer.model.player;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import main.musicplayer.model.ModelApp;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Playlist extends ModelApp implements FileVisitor {
    private List<File> playlist = new ArrayList<File>();
    private List<File> previousSong = new ArrayList<File>();
    private File thisSong;
    private TextArea textArea;

    public void init(TextArea textArea) {
        this.textArea = textArea;
        createPlaylist();
    }

    public List<File> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<File> playlist) {
        this.playlist = playlist;
    }

    public File previousSong() {
        if (!previousSong.isEmpty()) {
            playlist.addFirst(previousSong.getLast());
            File file = previousSong.getLast();
            previousSong.removeLast();
            return file;
        }
        return null;
    }

    public File getFirstSong() {
        if (!playlist.isEmpty()) {
            return playlist.getFirst();
        }
        return null;
    }
    public File getSong(int index) {
        if (playlist.size() >= index) {
            return playlist.get(index - 1);
        }
        return null;
    }
    public File getLastSong() {
        if (!previousSong.isEmpty()) {
            return previousSong.get(previousSong.size() - 2);
        } return null;
    }
    public File nextSong() {
        try {
            previousSong.add(playlist.getFirst());
            playlist.removeFirst();
            return playlist.getFirst();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }
    public File setFirstSong(String index) {
        int i = Integer.parseInt(index) - 1;
        File file = playlist.get(i);
        previousSong.add(playlist.getFirst());
        playlist.remove(i);
        playlist.removeFirst();
        playlist.addFirst(file);
        return file;
    }

    public void createPlaylist() {
        Path path = Path.of(properties.get("songsPathToPlayer").toString());
        Set<FileVisitOption> options = new HashSet<>();
        playlist = new ArrayList<>();
        try {
            Files.walkFileTree(path, options, 1, this);
            //visitFile(path, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (playlist.isEmpty()) {
            thisSong = null;
        } else {
            thisSong = playlist.getFirst();
        }
        updatePlaylist();
    }
    public void shufflePlaylist() {
        thisSong = playlist.getFirst();
        Collections.shuffle(playlist);
        playlist.remove(thisSong);
        playlist.addFirst(thisSong);
        updatePlaylist();
    }

    public void deletePlaylist() {
        playlist.clear();
    }

    public void updatePlaylist() {
        showPlaylist();
    }

    public void showPlaylist() {
        textArea.clear();
        int i = 1;
        for (File file : playlist) {
            textArea.appendText(i++ + ": " + file.getName() + "\n");
        }
    }

    public void addSong(File song) {
        playlist.add(song);
    }

    public void removeSong(File song) {
        playlist.remove(song);
    }
    public void removeDown(String id) {
        int i = Integer.parseInt(id);
        File song = playlist.get(i - 1);
        playlist.remove(song);
        playlist.add(i, song);
    }
    public void removeUp(String id) {

        int i = Integer.parseInt(id);
        File song = playlist.get(i - 1);
        playlist.remove(song);
        playlist.add(i - 2, song);
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        Path path = Path.of(file.toString());
        if (file.toString().endsWith("mp3")) {
            try {
                addSong(new File(path.toString()));
                return FileVisitResult.CONTINUE;
            } catch (Exception e) {
                //System.out.println(ConsoleOutput.ANSI_RED + e + ConsoleOutput.RESET_COLOR);
                return FileVisitResult.CONTINUE;
            }
        } else { // если формат не mp3
            return FileVisitResult.CONTINUE;
        }

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
