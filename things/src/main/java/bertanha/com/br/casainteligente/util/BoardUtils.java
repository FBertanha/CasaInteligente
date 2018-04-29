package bertanha.com.br.casainteligente.util;

import com.google.android.things.pio.PeripheralManager;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import bertanha.com.br.mylibrary.util.FirebaseUtils;


/**
 * Created by berta on 2/20/2018.
 */

public class BoardUtils {
    
    public static void updateGpioList() {
        DatabaseReference gpioRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.NODE_GPIO_LIST);
        PeripheralManager managerService = PeripheralManager.getInstance();

        List<String> gpioList = managerService.getGpioList();
    }
}
