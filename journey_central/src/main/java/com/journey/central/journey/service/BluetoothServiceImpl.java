package com.journey.central.journey.service;

import com.google.protobuf.ByteString;
import com.journey.central.journey.MP3Player;
import com.journey.central.journey.peripherals.bluetooth.BluetoothModule;
import com.journey.central.journey.utils.SpeechSynthesizer;
import org.springframework.stereotype.Service;

@Service
public class BluetoothServiceImpl implements BluetoothService {
    private BluetoothModule bluetoothModule = new BluetoothModule();

    @Override
    public void speak(String text) {
        System.out.println("BluetoothServiceImpl speak: " + text);
        try {
            ByteString bytes = SpeechSynthesizer.synthesizeText(text);
            if(bytes!=null){
                MP3Player mp3Player = new MP3Player(bytes);
                mp3Player.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
