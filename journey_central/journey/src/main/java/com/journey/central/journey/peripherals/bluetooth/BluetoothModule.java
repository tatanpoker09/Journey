package com.journey.central.journey.peripherals.bluetooth;


import javax.bluetooth.*;
import java.io.IOException;
import java.util.Vector;

public class BluetoothModule {
    final Object inquiryCompletedEvent = new Object();
    Vector<RemoteDevice> devicesDiscovered = new Vector<>();
    //Function to get bluetooth devices
    public void searchBluetoothDevices(int timeout) throws IOException, InterruptedException {
        System.out.println("Searching for bluetooth devices...");
        DiscoveryListener listener = new DiscoveryListener(){

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
            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                System.out.println("Service discovered: " + servRecord[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
            }

            @Override
            public void serviceSearchCompleted(int transID, int respCode) {
                System.out.println("Service search completed: " + respCode);
            }

            @Override
            public void inquiryCompleted(int discType) {
                System.out.println("Inquiry completed: " + discType);
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }
        };
        synchronized (inquiryCompletedEvent){
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started){
                System.out.println("Inquiry started");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() + " devices discovered");
            }
        }
        System.out.println("Stopping bluetooth discovery...");


    }
}
