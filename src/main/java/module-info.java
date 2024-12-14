module main.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    /*requires tika.core;
    requires tika.parsers;*/
    requires java.desktop;
    requires javafx.media;
    requires mp3agic;


    opens main.musicplayer to javafx.fxml;
    exports main.musicplayer;
    exports main.musicplayer.controller;
    opens main.musicplayer.controller to javafx.fxml;
}