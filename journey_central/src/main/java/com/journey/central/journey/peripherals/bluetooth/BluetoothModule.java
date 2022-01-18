package com.journey.central.journey.peripherals.bluetooth;


import com.google.protobuf.ByteString;
import com.intel.bluetooth.obex.OBEXClientSessionImpl;
import com.journey.central.journey.MP3Player;
import com.journey.central.journey.utils.SpeechSynthesizer;
import javazoom.jl.player.Player;

import javax.bluetooth.*;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.Vector;

public class BluetoothModule {
    private JourneyDiscoveryListener discoveryListener = new JourneyDiscoveryListener();

    //Function to get bluetooth devices
    public void searchBluetoothDevices(int timeout) throws IOException, InterruptedException {
        System.out.println("Searching for bluetooth devices...");
        synchronized (discoveryListener.inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, discoveryListener);
            if (started) {
                System.out.println("Inquiry started");
                discoveryListener.inquiryCompletedEvent.wait();
                System.out.println(discoveryListener.devicesDiscovered.size() + " devices discovered");
            }
        }
        System.out.println("Stopping bluetooth discovery...");


    }

    public RemoteDevice getDevice(String name) {
        for (RemoteDevice device : discoveryListener.devicesDiscovered) {
            try {
                if (device.getFriendlyName(false).equals(name)) {
                    return device;
                }
            } catch (IOException e) {
                System.out.println("Error getting device name: " + e.getMessage());
            }
        }
        return null;
    }

    public void searchServices(RemoteDevice device) throws BluetoothStateException {
        UUID[] uuidSet = new UUID[1];
        uuidSet[0] = new UUID(0x1105); //OBEX Object Push service

        int[] attrIDs = new int[]{
                0x0100 // Service name
        };

        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        try {
            System.out.println("Searching for services... in " + device.getFriendlyName(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        agent.searchServices(null, uuidSet, device, discoveryListener);


        try {
            synchronized (discoveryListener.inquiryCompletedEvent) {
                discoveryListener.inquiryCompletedEvent.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    public Connection connect(RemoteDevice device) throws IOException {
        System.out.println("Connecting to device...");
        String url = device.getBluetoothAddress();
        url = "btspp://" + url + ":2;authenticate=false;encrypt=false;master=false";
        System.out.println(url);
        StreamConnection connection = (StreamConnection) Connector.open(url);
        System.out.println("Connected to device");
        return connection;
    }

    public Connection connect(String macAddress) throws IOException {
        System.out.println("Connecting to device...");
        String url = "btspp://"+macAddress+":2;authenticate=false;encrypt=false;master=false";
        StreamConnection connection = (StreamConnection) Connector.open(url);
        System.out.println("Connected to device");
        return connection;

    }

    public void sendToDevice(String message) throws IOException {
        System.out.println("Sending message...");

        try {
            ByteString bytes = SpeechSynthesizer.synthesizeText(message);
            MP3Player mp3Player = new MP3Player(bytes);
            mp3Player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Message sent");
    }

    public void playFile(String filePath) throws UnsupportedAudioFileException, IOException, InterruptedException {
        // Take the path of the audio file from command line
        //MP3Player mp3Player = new MP3Player(filePath);
        //mp3Player.play();
    }

}
class JourneyDiscoveryListener implements DiscoveryListener{

    protected final Object inquiryCompletedEvent = new Object();
    protected Vector<RemoteDevice> devicesDiscovered = new Vector<>();

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        System.out.println("Device discovered: " + btDevice.getBluetoothAddress());
        devicesDiscovered.addElement(btDevice);
        try{
            System.out.println("Device name: " + btDevice.getFriendlyName(false));
        } catch (IOException e) {
            System.out.println("Error getting device name: " + e.getMessage());
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] services) {
        for (int i = 0; i < services.length; i++) {
            String url = services[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (url == null) {
                continue;
            }

            DataElement serviceName = services[i].getAttributeValue(0x0100);
            if (serviceName != null) {
                System.out.println("service " + serviceName.getValue() + " found " + url);
            } else {
                System.out.println("service found " + url);
            }

            if(serviceName.getValue().equals("OBEX Object Push")){
                System.out.println("We're getting closer!");
            }
        }
    }

    @Override
    public void serviceSearchCompleted(int transID, int respCode) {
        System.out.println("Service search completed: " + respCode);
        synchronized (inquiryCompletedEvent){
            inquiryCompletedEvent.notify();
        }
    }

    @Override
    public void inquiryCompleted(int discType) {
        System.out.println("Inquiry completed: " + discType);
        synchronized (inquiryCompletedEvent) {
            inquiryCompletedEvent.notifyAll();
        }
    }

}