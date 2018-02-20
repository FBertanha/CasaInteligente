package bertanha.com.br.casainteligente.drivers.sensor;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;

import java.io.IOException;

import bertanha.com.br.casainteligente.drivers.contract.MotionDriver;

/**
 * Created by berta on 2/18/2018.
 */

public class PirMotionSensor implements MotionDriver {
    private final String TAG = getClass().getName();
    private final Gpio bus;
    private final MotionDriver.Listener listener;


    public PirMotionSensor(Gpio bus, Listener listener) {
        this.bus = bus;
        this.listener = listener;
    }

    @Override
    public void startup() {
        try {
            bus.setDirection(Gpio.DIRECTION_IN);
            bus.setActiveType(Gpio.ACTIVE_HIGH);
            bus.setEdgeTriggerType(Gpio.EDGE_RISING);
        } catch (IOException e) {
            throw new IllegalStateException("Sensor can't start", e);
        }
        try {
            bus.registerGpioCallback(callback);
        } catch (IOException e) {
            throw new IllegalStateException("Sensor can't register callback - App is foobar'd", e);
        }
    }

    private final GpioCallback callback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            listener.onMovement();
            return true;
        }
    };

    @Override
    public void shutdown() {
        bus.unregisterGpioCallback(callback);
        try {
            bus.close();
        } catch (IOException e) {
            Log.e(TAG, "shutdown: " + "Failed to shut down. You might get errors next time you try to start.", e);
        }
    }
}
