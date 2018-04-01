package bertanha.com.br.casainteligente.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import bertanha.com.br.casainteligente.R;
import bertanha.com.br.casainteligente.model.Device;
import bertanha.com.br.casainteligente.ui.adapter.DeviceAdapter;
import bertanha.com.br.mylibrary.util.FirebaseUtils;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getName();
    DatabaseReference mDatabaseRef;
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseRef = FirebaseUtils.getDatabaseReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.main_lista);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query listasQuery = mDatabaseRef.child(FirebaseUtils.DEVICES);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Device>()
                .setQuery(listasQuery, Device.class)
                .build();
        mAdapter = new DeviceAdapter(options);
        mRecyclerView.setAdapter(mAdapter);

        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }
}
