package bertanha.com.br.casainteligente.drivers.contract;

/**
 * Created by berta on 2/18/2018.
 */

public interface MotionDriver extends BaseDriver{

    interface Listener {
        void onMovement();
    }
}
