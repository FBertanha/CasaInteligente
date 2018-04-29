package bertanha.com.br.casainteligente.manager;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bertanha.com.br.mylibrary.model.Device;
import bertanha.com.br.mylibrary.util.FirebaseUtils;

/**
 * Created by berta on 2/24/2018.
 */

public class BoardManager {
    private final String TAG = getClass().getName();
    private List<Device> deviceList;

    public void start() {
        deviceList =  new ArrayList<>();
        loadAndListen();
    }

    public void stop() {
        closeAllGpio();
    }


    private void loadAndListen() {
        //seed();
        //TODO habilitar adicição de novos devices
        DatabaseReference devicesRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.DEVICES);

        devicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Device device = dataSnapshot.getValue(Device.class);
                setupDevice(device);
                deviceList.add(device);

                Log.d(TAG, "onChildAdded: " + device);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Device deviceSnapshot = dataSnapshot.getValue(Device.class);
                Gpio gpio = deviceList.get(deviceList.indexOf(deviceSnapshot)).getGpio();

                if (deviceSnapshot.getType() == Device.TYPE_LED) {
                    try {
                        gpio.setValue(deviceSnapshot.getValue());
                    } catch (IOException e) {
                        Log.e(TAG, "onChildChanged: " + e.getMessage());
                    }
                }

                Log.d(TAG, "onChildChanged: " + deviceSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Device deviceSnapshot = dataSnapshot.getValue(Device.class);
                Gpio gpio = deviceList.get(deviceList.indexOf(deviceSnapshot)).getGpio();
                closeGpio(gpio);

                Log.d(TAG, "onChildRemoved: " + deviceSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupDevice(Device device) {
        Log.e(TAG, "setupDevices: " + device.getValue());
        device.setGpio(openGpio(device.getBcm()));

        if (device.getGpio() == null) return;

        if (device.getType() == Device.TYPE_LED) {
            try {
                device.getGpio().setDirection(device.getValue() ? Gpio.DIRECTION_OUT_INITIALLY_HIGH : Gpio.DIRECTION_OUT_INITIALLY_LOW);
                device.getGpio().setActiveType(Gpio.ACTIVE_HIGH);
            } catch (IOException e) {
                Log.e(TAG, "setupDevice: " + e.getMessage());
            }
        }
    }

    private Gpio openGpio(String bcm) {
        PeripheralManager managerService = PeripheralManager.getInstance();
        Gpio bus = null;

        try {
            bus = managerService.openGpio(bcm);
            Log.d(TAG, "openGpio: " + bcm + " opened");
        } catch (IOException e) {
            Log.e(TAG, "openGpio: " + e.getMessage());
        }
        return bus;
    }

    private void closeGpio(Gpio gpio) {
        try {
            gpio.close();
            Log.d(TAG, "closeGpio: " + gpio.getName() + " closed");
        } catch (IOException e) {
            Log.e(TAG, "closeGpio: " + e.getMessage());
        }
    }

    private void closeAllGpio() {
        for (Device device : deviceList) {
            closeGpio(device.getGpio());
        }
    }

    private void seed() {
        DatabaseReference devicesRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.DEVICES);
        final List<Device> devices = new ArrayList<>();

        devices.add(new Device("Led azul", "BCM17", Device.TYPE_LED));
        devices.add(new Device("Led Vermelho", "BCM27", Device.TYPE_LED));
        devices.add(new Device("Weather", "BCM26", Device.TYPE_WEATHER));

        for (Device device:
             devices) {
            String key = device.getBcm();


            devicesRef.child(key).setValue(device);


        }


    }
}
