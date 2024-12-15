package main.musicplayer.model;


import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import javafx.scene.image.Image;


import java.io.ByteArrayInputStream;
import java.util.*;

public abstract class ModelApp implements Model{
    public Properties properties;

    public void setProperties(Properties prop) {
        properties = prop;
    }
    public HashMap<String, Object> getSongInfo(Mp3File song) {
        HashMap hash = new HashMap();
        if (song == null) {
            return null;
        }
        if (song.hasId3v2Tag()) {
            hash = id3v2Tag(song.getId3v2Tag());
        } else if (song.hasId3v1Tag()) {
            hash = id3v1Tag(song.getId3v1Tag());
        }
        return hash;
    }
    private HashMap<String, Object> id3v1Tag(ID3v1 song) {
        HashMap hash = new HashMap();
        String artist = song.getArtist();
        String songName = song.getTitle();
        hash.put("artist", artist);
        hash.put("songName", songName);

        return hash;
    }
    private HashMap<String, Object> id3v2Tag(ID3v2 song) {
        HashMap hash = new HashMap();
        String artist = song.getArtist();
        String songName = song.getTitle();
        //byte[] albumImage = song.getAlbumImage();
        hash.put("artist", artist);
        hash.put("songName", songName);
        //hash.put("image", albumImage);

        return hash;
    }
    public Image getImage(Mp3File song) {
        if (song == null || song.hasId3v1Tag()) {
            return null;
        } else {
            byte[] albumImage = song.getId3v2Tag().getAlbumImage();
            Image image = new Image(new ByteArrayInputStream(albumImage));
            return image;
        }
    }
}
