package bertanha.com.br.casainteligente.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import bertanha.com.br.casainteligente.R;
import bertanha.com.br.casainteligente.ui.viewholder.DeviceViewHolder;
import bertanha.com.br.mylibrary.model.Device;

/**
 * Created by berta on 2/24/2018.
 */

public class DeviceAdapter extends FirebaseRecyclerAdapter<Device, DeviceViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DeviceAdapter(@NonNull FirebaseRecyclerOptions<Device> options) {
        super(options);
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
}
