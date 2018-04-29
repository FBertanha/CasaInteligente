package bertanha.com.br.casainteligente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import bertanha.com.br.casainteligente.R;
import bertanha.com.br.casainteligente.ui.adapter.DeviceAdapter;
import bertanha.com.br.casainteligente.ui.adapter.OnItemClickListener;
import bertanha.com.br.casainteligente.ui.adapter.OnMenuItemClickListener;
import bertanha.com.br.mylibrary.model.Device;
import bertanha.com.br.mylibrary.util.FirebaseUtils;

import static bertanha.com.br.casainteligente.activity.NewDeviceActivity.KEY_DEVICE;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_DEVICE = 0;
    private static final int REQUEST_EDIT_DEVICE = 1;
    private String TAG = getClass().getName();
    DatabaseReference mDatabaseRef;
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewDeviceActivity.class);
                startActivityForResult(intent, REQUEST_NEW_DEVICE);
            }
        });

        mDatabaseRef = FirebaseUtils.getDatabaseReference();

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_lista);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_DEVICE || requestCode == REQUEST_EDIT_DEVICE) {
            if (resultCode == RESULT_OK) {
                Device device = (Device) data.getSerializableExtra(KEY_DEVICE);
                mDatabaseRef.child(FirebaseUtils.NODE_DEVICES).child(device.getBcm()).setValue(device);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupAdapter();
    }

    private void setupAdapter() {
        Query listasQuery = mDatabaseRef.child(FirebaseUtils.NODE_DEVICES);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Device>()
                .setQuery(listasQuery, Device.class)
                .build();
        mAdapter = new DeviceAdapter(this, options);
        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter != null) {
            mAdapter.startListening();
        }
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Device device) {
                Toast.makeText(HomeActivity.this, device.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item, Device device) {

                switch (item.getItemId()) {
                    case R.id.action_edit:
                        Intent intent = new Intent(HomeActivity.this, NewDeviceActivity.class);
                        intent.putExtra(KEY_DEVICE, device);
                        startActivityForResult(intent, REQUEST_EDIT_DEVICE);
                        break;
                    case R.id.action_delete:
                        mDatabaseRef.child(FirebaseUtils.NODE_DEVICES).child(device.getBcm()).removeValue();
                        break;
                }
                return false;
            }
        });
//        mAdapter.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(Device device) {
//                Log.e(TAG, "onLongClick: " + device);
//                Intent intent = new Intent(HomeActivity.this, NewDeviceActivity.class);
//                intent.putExtra(KEY_DEVICE, device);
//                startActivityForResult(intent, REQUEST_EDIT_DEVICE);
//                return true;
//            }
//        });
    }

}
