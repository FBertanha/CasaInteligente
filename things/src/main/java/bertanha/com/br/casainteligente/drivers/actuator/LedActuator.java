package bertanha.com.br.casainteligente.drivers.actuator;

import android.util.Log;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

import bertanha.com.br.casainteligente.drivers.contract.BaseDriver;

/**
 * Created by berta on 2/18/2018.
 */

public class LedActuator implements BaseDriver{
    private final String TAG = getClass().getName();
    private final Gpio bus;

    public LedActuator(Gpio bus) {
        this.bus = bus;
    }

    @Override
    public void startup() {
        try {
            bus.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            bus.setActiveType(Gpio.ACTIVE_HIGH);
        } catch (IOException e) {
            throw new IllegalStateException("Actuator can't start", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            bus.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to shut down. You might get errors next time you try to start.", e);
        }
    }
}
