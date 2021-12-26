package com.journey.central.journey;

import com.journey.central.journey.peripherals.bluetooth.BluetoothModule;
import org.junit.Test;

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
}
