package bertanha.com.br.casainteligente.model;

import com.google.android.things.pio.Gpio;

/**
 * Created by berta on 2/15/2018.
 */

public class Device {
    public static final int TYPE_LED = 0;
    public static final int TYPE_RELE = 1;
    public static final int TYPE_PIR = 2;

    private String id;
    private String description;
    private String bcm;
    private Gpio gpio;
    private int type;
    private boolean value;

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


    public Gpio getGpio() {
        return gpio;
    }

    public void setGpio(Gpio gpio) {
        this.gpio = gpio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", bcm='" + bcm + '\'' +
                ", gpio=" + gpio +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
