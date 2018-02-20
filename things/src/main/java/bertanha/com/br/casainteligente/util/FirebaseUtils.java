package bertanha.com.br.casainteligente.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by berta on 2/18/2018.
 */

public class FirebaseUtils {

    public static String DEVICES = "devices";


    private static FirebaseDatabase mDatabase;
    private static DatabaseReference mDatabaseReference;

    private static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
        }
        return mDatabase;
    }

    public static DatabaseReference getDatabaseReference() {
        return getDatabase().getReference();
    }
}
