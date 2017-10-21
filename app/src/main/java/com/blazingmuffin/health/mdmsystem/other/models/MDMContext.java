package com.blazingmuffin.health.mdmsystem.other.models;

import android.content.Context;

import com.blazingmuffin.health.mdmsystem.R;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

/**
 * Created by lenovo on 10/21/2017.
 */

public class MDMContext {
    private static Database mDatabase;
    private static Manager mManager;
    public static synchronized Manager Manager(Context context) {
        try {
            if (mManager == null) {
                mManager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mManager;
    }

    public static synchronized Database Instance(Context context) {
        try {
            if (mDatabase == null) {
                mDatabase = MDMContext.Manager(context).getDatabase(context.getString(R.string.general_database_name));
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return mDatabase;
    }
}
