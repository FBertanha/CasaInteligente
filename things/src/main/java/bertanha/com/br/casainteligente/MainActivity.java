package bertanha.com.br.casainteligente;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

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
    PeripheralManagerService managerService;
    Gpio gpioLedAzul;
    Gpio gpioLedVermelho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        managerService = new PeripheralManagerService();

        try {
            gpioLedAzul = managerService.openGpio("BCM17");
            gpioLedAzul.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            gpioLedVermelho = managerService.openGpio("BCM27");
            gpioLedVermelho.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            while (true) {
                gpioLedAzul.setValue(!gpioLedAzul.getValue());
                Thread.sleep(500);
                gpioLedVermelho.setValue(!gpioLedVermelho.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            gpioLedAzul.registerGpioCallback(new GpioCallback() {
//                @Override
//                public boolean onGpioEdge(Gpio gpio) {
//                    try {
//                        Log.i(TAG, "onGpioEdge: " + gpio.getValue());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return super.onGpioEdge(gpio);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mButtonInputDriver != null) {
//            mButtonInputDriver.unregister();
//            try {
//                mButtonInputDriver.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Error closing Button driver", e);
//            } finally{
//                mButtonInputDriver = null;
//            }
//        }

        if (gpioLedAzul != null) {
            try {
                gpioLedAzul.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                gpioLedAzul = null;
            }
            gpioLedAzul = null;
        }

        if (gpioLedVermelho != null) {
            try {
                gpioLedVermelho.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                gpioLedVermelho = null;
            }
            gpioLedVermelho = null;
        }
    }
}
