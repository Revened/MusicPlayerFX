package main.musicplayer.model.musicRename;

import java.nio.file.Path;

public class StringFormatter {
    public static void IsArtistNeeded(boolean isArtistNeeded) {
        StringFormatter.isArtistNeeded = isArtistNeeded;
    }
    public static void IsTitleNeeded(boolean isTitleNeeded) {
        StringFormatter.isTitleNeeded = isTitleNeeded;
    }
    private static boolean isArtistNeeded = true;
    private static boolean isTitleNeeded = true;

    private static String pathToNameSong(Path path) {
        String str = path.toString();
        String[] actor_song = str.split("\\\\");
        String[] array = actor_song[actor_song.length - 1].split("-");
        StringBuilder songName = new StringBuilder();
        if (array.length > 2) {                     // Если в названии песни есть знак -
            for (int i = 1; i < array.length; i++) {
                songName.append(array[i] + (array[i].contains("mp3") ? "":"-"));
            }
        } else songName.append(array[1]);           // Если нет знака -
        return songName.toString();    //  название песни;
    }
    public static String formatArtistTitle(String artist, String title) {
        if (isTitleNeeded) {
            if (title.contains("\"")) {
                String[] strings = title.split("\"");
                String str = "";
                for (int i = 0; i < strings.length; i++) {
                    str += strings[i];
                }
                title = str;
            }
            if (title.contains("*")) {
                title = title.replace("*", "");
            }
        }
        if (isArtistNeeded) {
            StringBuilder fileName = getNewFileName(artist, title);
            return String.valueOf(fileName);
        } else return title + ".mp3";
    }

    private static StringBuilder getNewFileName(String artist, String title) {
        StringBuilder newArtistName = new StringBuilder();

        if (artist.contains("/")) {
            String[] array = artist.split("/");
            for (int i = 0; i < array.length; i++) {
                newArtistName.append(array[i]);
                if (i + 1 != array.length) {
                    newArtistName.append(", ");
                }
            }
            newArtistName.append(" - " + title + ".mp3");
        } else {
            newArtistName.append(artist + " - " + title + ".mp3");
        }
        return newArtistName;
    }

    @Deprecated
    public static String formatArtistSong(String artist, Path songPath) {       // использовать если нет в файле название песни
        String song = pathToNameSong(songPath);                                 // в таком случае будет использоваться имя файла
        if (isArtistNeeded) {
            if (artist.contains("/")) {
                StringBuilder newArtistName = new StringBuilder();
                String[] array = artist.split("/");
                for (int i = 0; i < array.length; i++) {
                    newArtistName.append(array[i]);
                    if (i + 1 != array.length) {
                        newArtistName.append(", ");
                    }
                }
                return newArtistName + " -" + song;
            }
            return artist + " -" + song; // имя артиста + название песни
        } else {
            return song;
        }
    }
    @Deprecated
    public static String formatArtistSong(String artist, String songName) {     // использовать если есть в файле название песни
        if (isArtistNeeded) {
            if (artist.contains("/")) {
                StringBuilder newArtistName = new StringBuilder();
                String[] array = artist.split("/");
                for (int i = 0; i < array.length; i++) {
                    newArtistName.append(array[i]);
                    if (i + 1 != array.length) {
                        newArtistName.append(", ");
                    }
                }
                return newArtistName + " - " + songName + ".mp3";
            }
            return artist + " - " + songName + ".mp3"; // имя артиста + название песни
        } else {
            return songName + ".mp3";
        }
    }
    public static String getFileName(Object path) {
        String stringPath = path.toString();
        String[] array = stringPath.split("\\\\");
        return array[array.length - 1];
    }
}
