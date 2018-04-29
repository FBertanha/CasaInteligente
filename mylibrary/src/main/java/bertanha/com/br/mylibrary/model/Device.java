package bertanha.com.br.mylibrary.model;

import com.google.android.things.pio.Gpio;

/**
 * Created by berta on 2/15/2018.
 */

public class Device {
    public static final int TYPE_LED = 0;
    public static final int TYPE_RELE = 1;
    public static final int TYPE_PIR = 2;
    public static final int TYPE_WEATHER = 3;

    private String description;
    private String bcm;
    private int type;
    private boolean value;
    private String status;
    private Gpio gpio;

    public Device() {
    }

    public Device(String description, String bcm, int type) {
        this.description = description;
        this.bcm = bcm;
        this.type = type;
    }

    public String getBcm() {
        return bcm;
    }

    public void setBcm(String bcm) {
        this.bcm = bcm;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        return bcm.equals(device.bcm);
    }

    @Override
    public int hashCode() {
        return bcm.hashCode();
    }

    @Override
    public String toString() {
        return "Device{" +
                "description='" + description + '\'' +
                ", bcm='" + bcm + '\'' +
                ", type=" + type +
                ", value=" + value +
                ", status='" + status + '\'' +
                ", gpio=" + gpio +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGpio(Gpio gpio) {
        this.gpio = gpio;
    }

    public Gpio getGpio() {
        return gpio;
    }
}
