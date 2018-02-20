package bertanha.com.br.casainteligente;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bertanha.com.br.casainteligente.model.Device;
import bertanha.com.br.casainteligente.util.FirebaseUtils;


/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService managerService = new PeripheralManagerService();
 * gpioLedAzul = managerService.openGpio("BCM6");
 * gpioLedAzul.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * gpioLedAzul.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {
    private final String TAG = getClass().getName();
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + new PeripheralManagerService().getGpioList());

        deviceList = new ArrayList<>();
        //seed();
        loadDevices();


        //teste();

    }

    private void loadDevices() {
        //TODO habilitar adicição de novos devices
        DatabaseReference devicesRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.DEVICES);

        devicesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Device device = dataSnapshot.getValue(Device.class);
                device.setId(dataSnapshot.getKey());
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

//        devicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot deviceSnapshot: dataSnapshot.getChildren()) {
//
//                    Device device = deviceSnapshot.getValue(Device.class);
//                    device.setId(dataSnapshot.getKey());
//                    device.setGpio(openGpio(device.getBcm()));
//                    deviceList.add(device);
//
//                    Log.d(TAG, "onDataChange: " + device);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }

    private void seed() {
        DatabaseReference devicesRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.DEVICES);
        final List<Device> devices = new ArrayList<>();
        devices.add(new Device("Led azul", "BCM17", Device.TYPE_LED));
        devices.add(new Device("Led Vermelho", "BCM27", Device.TYPE_LED));

        devicesRef.setValue(devices);
    }

//    void teste() {
//        DatabaseReference devicesRef = FirebaseUtils.getDatabaseReference().child("deviceList");
//        final List<Device> devices = new ArrayList<>();
//        devices.add(new Device("Led azul", "BCM17", Device.TYPE_LED));
//        devices.add(new Device("Led Vermelho", "BCM27", Device.TYPE_LED));
//
//        devicesRef.setValue(devices);
//
//        devicesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Device device = postSnapshot.getValue(Device.class);
//                    setupDevice(device);
//                    deviceList.add(device);
//                    Log.d(TAG, "onDataChange: " + deviceList.size());
//                }
//                //Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        devicesRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Device device = dataSnapshot.getValue(Device.class);
//                deviceList.clear();
//                deviceList.add(device);
////                if (device.getType() == Device.TYPE_RELE) {
////                    LedActuator ledActuator = new LedActuator(openGpio(device.getBcm()));
////                    ledActuator.startup();
////
////                }
////                Log.d(TAG, "onChildAdded: " + device.getBcm());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Device device = dataSnapshot.getValue(Device.class);
//
//                Log.d(TAG, "onChildChanged: " + dataSnapshot.getValue(Device.class).getBcm());
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onChildRemoved: " + dataSnapshot.getValue(Device.class).getBcm());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//    }

    private void setupDevice(Device device) {
        Log.e(TAG, "setupDevices: " + device.getValue());
        if (device.getType() == Device.TYPE_LED) {
            device.setGpio(openGpio(device.getBcm()));
            try {
                device.getGpio().setDirection(device.getValue() ? Gpio.DIRECTION_OUT_INITIALLY_HIGH : Gpio.DIRECTION_OUT_INITIALLY_LOW);
                device.getGpio().setActiveType(Gpio.ACTIVE_HIGH);
            } catch (IOException e) {
                throw new IllegalStateException("Can't setup GPIO", e);
            }
        }
    }

    private Gpio openGpio(String bcm) {
        PeripheralManagerService managerService = new PeripheralManagerService();
        Gpio bus;

        try {
            bus = managerService.openGpio(bcm);
            Log.i(TAG, "openGpio: " + bcm + " opened");
        } catch (IOException e) {
            throw new IllegalStateException("Can't open GPIO", e);
        }
        return bus;
    }

    private void closeGpio(Gpio gpio) {
        try {
            gpio.close();
            Log.i(TAG, "closeGpio: " + gpio.getName() + " closed");
        } catch (IOException e) {
            throw new IllegalStateException("Can't close GPIO", e);
        }
    }

    private void closeAllGpio() {
        for (Device device : deviceList) {
            closeGpio(device.getGpio());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeAllGpio();

    }
}
