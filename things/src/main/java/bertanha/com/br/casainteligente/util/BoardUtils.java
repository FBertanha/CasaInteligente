package bertanha.com.br.casainteligente.util;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DatabaseReference;

import java.util.List;


/**
 * Created by berta on 2/20/2018.
 */

public class BoardUtils {
    
    public static void updateGpioList() {
        DatabaseReference gpioRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.GPIO_LIST);
        PeripheralManagerService managerService = new PeripheralManagerService();

        List<String> gpioList = managerService.getGpioList();
    }
}
