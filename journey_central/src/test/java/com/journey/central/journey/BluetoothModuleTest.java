package com.journey.central.journey;

import com.intel.bluetooth.obex.OBEXClientSessionImpl;
import com.journey.central.journey.peripherals.bluetooth.BluetoothModule;
import org.junit.Test;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.StreamConnection;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class BluetoothModuleTest {

    private CountDownLatch lock = new CountDownLatch(1);
    @Test
    public void discoverDevices(){
        BluetoothModule bluetoothModule = new BluetoothModule();
        try {
            bluetoothModule.searchBluetoothDevices(10000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendSpeechToDevice(){
        BluetoothModule bluetoothModule = new BluetoothModule();
        try {
            /*bluetoothModule.searchBluetoothDevices(5);
            RemoteDevice naga = bluetoothModule.getDevice("Naga");
            bluetoothModule.searchServices(naga);
            StreamConnection connect = (StreamConnection) bluetoothModule.connect(naga);*/
            bluetoothModule.sendToDevice("Hello this is a test I'm doing");
            Thread.sleep(8000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void fastFileTest(){
        BluetoothModule bluetoothModule = new BluetoothModule();
        try {
            //StreamConnection connect = (StreamConnection) bluetoothModule.connect("EAE4B2DD71B5");
            bluetoothModule.playFile("output.mp3");
            Thread.sleep(10009);
        } catch (IOException | UnsupportedAudioFileException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
