package bertanha.com.br.casainteligente.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import bertanha.com.br.casainteligente.R;
import bertanha.com.br.mylibrary.model.Device;

/**
 * Created by berta on 2/24/2018.
 */

public class DeviceAdapter extends FirebaseRecyclerAdapter<Device, DeviceAdapter.DeviceViewHolder> {

    private final Context context;
    private OnItemClickListener onItemClickListener;
    private OnMenuItemClickListener OnMenuItemClickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DeviceAdapter(Context context, @NonNull FirebaseRecyclerOptions<Device> options) {
        super(options);
        this.context = context;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DeviceViewHolder(inflater.inflate(R.layout.device_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final DeviceViewHolder holder, int position, @NonNull Device model) {
        final DatabaseReference deviceRef = getRef(position);

        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceRef.child("value").setValue(holder.toggle.isChecked());
            }
        });

        holder.bindDevice(model);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnMenuItemClickListener(bertanha.com.br.casainteligente.ui.adapter.OnMenuItemClickListener onMenuItemClickListener) {
        OnMenuItemClickListener = onMenuItemClickListener;
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = getClass().getName();
        public final TextView description;
        public final ToggleButton toggle;
        private final ImageView image;
        private final TextView status;
        private final ImageButton menu;
        private Device device;


        public DeviceViewHolder(View itemView) {
            super(itemView);
            this.description = itemView.findViewById(R.id.device_item_description);
            this.toggle = itemView.findViewById(R.id.device_item_toggle);
            this.status = itemView.findViewById(R.id.device_item_status);
            this.image = itemView.findViewById(R.id.device_item_image);
            this.menu = itemView.findViewById(R.id.device_item_menu);

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(device);
                }
            });

            this.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.actions, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            OnMenuItemClickListener.onMenuItemClick(item, device);
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });



        }

        public void bindDevice(Device device) {
            this.device = device;
            this.description.setText(device.getDescription());
            this.toggle.setChecked(device.getValue());
            this.status.setText(device.getStatus());

            if (device.getType() == Device.TYPE_LIGHT) {
                this.image.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_wb_incandescent_24dp));
            } else if (device.getType() == Device.TYPE_TEMPERATURE) {
                this.image.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_wb_cloudy_24dp));
                this.toggle.setVisibility(View.GONE);
            } else if (device.getType() == Device.TYPE_PIR) {
                this.image.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_directions_walk_24dp));
                this.toggle.setVisibility(View.GONE);
            }

        }


    }
}
