package bertanha.com.br.casainteligente.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import bertanha.com.br.casainteligente.R;
import bertanha.com.br.mylibrary.model.Device;
import bertanha.com.br.mylibrary.util.FirebaseUtils;

public class NewDeviceActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private EditText description;
    private Spinner type;
    private Spinner gpio;
    private ImageView image;
    public static final String KEY_DEVICE = "device";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_device);
        setupActionBar();
        loadView();

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_DEVICE)) {
            Device device = (Device) intent.getSerializableExtra(KEY_DEVICE);
            putDeviceInForm(device);
        }

    }

    private void putDeviceInForm(Device device) {
        description.setText(device.getDescription());
        type.setSelection(device.getType());
        type.setEnabled(false);
        gpio.setEnabled(false);


    }

    private Device getDeviceFromForm() {
        Device device = new Device();
        device.setDescription(description.getText().toString());
        device.setBcm(gpio.getSelectedItem().toString());
        device.setType(type.getSelectedItemPosition());
        device.setValue(false);
        return device;
    }

    private void loadView() {
        image = findViewById(R.id.new_device_image);
        description = findViewById(R.id.new_device_description);
        type = findViewById(R.id.new_device_type);
        gpio = findViewById(R.id.new_device_gpio);

        setupTypeSpinner();

        setupGpioSpinner();
    }

    private void setupGpioSpinner() {
        DatabaseReference devicesRef = FirebaseUtils.getDatabaseReference().child(FirebaseUtils.NODE_DEVICES);
        devicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupTypeSpinner() {
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Drawable drawable = null;
                switch (position) {
                    case 0:
                        drawable = getResources().getDrawable(R.drawable.ic_wb_incandescent_24dp);
                        break;
                    case 1:
                        drawable = getResources().getDrawable(R.drawable.ic_wb_cloudy_24dp);
                        break;
                    case 2:
                        drawable = getResources().getDrawable(R.drawable.ic_directions_walk_24dp);
                        break;

                }

                image.setImageDrawable(drawable);
                Log.i(TAG, "onItemSelected: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_new_device, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_new_device_save:
                returnDevice();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnDevice() {
        Intent intent = new Intent();
        Device device = getDeviceFromForm();
        intent.putExtra(KEY_DEVICE, device);
        setResult(RESULT_OK, intent);
    }
}
