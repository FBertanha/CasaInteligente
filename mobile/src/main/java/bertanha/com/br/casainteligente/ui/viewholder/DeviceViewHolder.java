package bertanha.com.br.casainteligente.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import bertanha.com.br.casainteligente.R;
import bertanha.com.br.mylibrary.model.Device;

/**
 * Created by berta on 2/24/2018.
 */

public class DeviceViewHolder extends RecyclerView.ViewHolder{
    private final String TAG = getClass().getName();
    public final TextView description;
    public final ToggleButton toggle;
    private final ImageView image;
    private final TextView status;


    public DeviceViewHolder(View itemView) {
        super(itemView);
        this.description = itemView.findViewById(R.id.device_item_description);
        this.toggle = itemView.findViewById(R.id.device_item_toggle);
        this.status = itemView.findViewById(R.id.device_item_status);
        this.image = itemView.findViewById(R.id.device_item_image);
    }

    public void bindDevice(Device device) {
        this.description.setText(device.getDescription());
        this.toggle.setChecked(device.getValue());
        this.status.setText(device.getStatus());

        if (device.getType() == Device.TYPE_WEATHER) {
            this.image.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_wb_cloudy_24dp));
            this.toggle.setVisibility(View.GONE);
        } else if (device.getType() == Device.TYPE_LED || device.getType() == Device.TYPE_RELE) {
            this.image.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_wb_incandescent_24dp));
        }

    }
}
