package bertanha.com.br.casainteligente.drivers.actuator;

import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

import bertanha.com.br.casainteligente.drivers.contract.ReleDriver;

/**
 * Created by berta on 2/18/2018.
 */

public class ReleActuator implements ReleDriver {
    private final String TAG = getClass().getName();
    private final Gpio bus;
    private Handler handler;
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;

    public ReleActuator(Gpio bus) {
        this.bus = bus;
        this.handler = new Handler();
    }

    @Override
    public void startup() {
        try {
            bus.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            bus.setActiveType(Gpio.ACTIVE_LOW);
        } catch (IOException e) {
            throw new IllegalStateException("Actuator can't start", e);
        }
    }

    @Override
    public void shutdown() {
        handler.removeCallbacks(blinkRunnable);
        try {
            bus.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to shut down. You might get errors next time you try to start.", e);
        }
    }

    @Override
    public void toggle() {
        try {
            bus.setValue(!bus.getValue());
        } catch (IOException e) {
            Log.e(TAG, "toggle: " + "Error on set value to pio");
        }
    }

    public void blink() {
        handler.post(blinkRunnable);
    }

    public void stopBlink() {
        handler.removeCallbacks(blinkRunnable);
    }

    //Blink runnable
    private Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (bus == null){
                return;
            }

            try {
                bus.setValue(!bus.getValue());

                handler.postDelayed(blinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "run: " + "Error on set value to pio");
            }
        }
    };
}
