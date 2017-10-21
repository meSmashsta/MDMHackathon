package com.blazingmuffin.health.mdmsystem.other.models.resident.update;

import com.blazingmuffin.health.mdmsystem.other.interfaces.IUpdatable;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.UnsavedRevision;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 10/21/2017.
 */

public class BasicUpdatable implements IUpdatable {

    private final Database mDatabase;

    public BasicUpdatable(Database database) {
        mDatabase = database;
    }

    @Override
    public void update(final ResidentEntity residentEntity) {
        Document residentDocument = mDatabase.getDocument(residentEntity.getId());
        try {
            residentDocument.update(new Document.DocumentUpdater() {
                @Override
                public boolean update(UnsavedRevision newRevision) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put(ResidentEntity.FIRST_NAME, residentEntity.getFirstName());
                    properties.put(ResidentEntity.MIDDLE_NAME, residentEntity.getMiddleName());
                    properties.put(ResidentEntity.LAST_NAME, residentEntity.getLastName());
                    properties.put(ResidentEntity.GENDER, residentEntity.getGender());
                    properties.put(ResidentEntity.BIRTHDATE, residentEntity.getBirthdate());
                    properties.put(ResidentEntity.TYPE, ResidentEntity.TYPE_VALUE);
                    newRevision.setUserProperties(properties);
                    return true;
                }
            });
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
