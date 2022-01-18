package com.journey.central.journey;

import com.google.protobuf.ByteString;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class MP3Player {
    private final ByteString byteString;
    private Player jlPlayer;

    public MP3Player(ByteString byteString) {
        this.byteString = byteString;
    }

    public void play() {
        try {
            ByteArrayInputStream bufferedInputStream = new ByteArrayInputStream(byteString.toByteArray());

            jlPlayer = new Player(bufferedInputStream);
        } catch (Exception e) {
            System.out.println("Problem playing mp3 file " + byteString);
            System.out.println(e.getMessage());
        }

        new Thread(() -> {
            try {
                jlPlayer.play();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();


    }

    public void close() {
        if (jlPlayer != null) jlPlayer.close();
    }
}