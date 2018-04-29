package bertanha.com.br.casainteligente;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.device.DeviceManager;
import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.database.DatabaseReference;

import bertanha.com.br.casainteligente.manager.BoardManager;
import bertanha.com.br.mylibrary.util.FirebaseUtils;

import static bertanha.com.br.mylibrary.util.FirebaseUtils.GPIO_LIST;


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
    private BoardManager boardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardManager = new BoardManager();

        PeripheralManager managerService = PeripheralManager.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        boardManager.start();

        PeripheralManager managerService = PeripheralManager.getInstance();

        DatabaseReference gpioList = FirebaseUtils.getDatabaseReference().child(GPIO_LIST);


        Log.i(TAG, "onStart: " + managerService.getGpioList());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boardManager.stop();

    }
}
