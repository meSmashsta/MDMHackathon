package com.blazingmuffin.health.mdmsystem.other.models.resident.update;

import com.blazingmuffin.health.mdmsystem.other.interfaces.IUpdatable;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.UnsavedRevision;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lenovo on 10/22/2017.
 */

public class HouseholdUpdatable implements IUpdatable {
    private final Database mDatabase;

    public HouseholdUpdatable(Database database) {
        mDatabase = database;
    }

    @Override
    public void update(ResidentEntity residentEntity) {
        Document residentDocument = mDatabase.getDocument(residentEntity.getId());
        try {
            residentDocument.update(new Document.DocumentUpdater() {
                @Override
                public boolean update(UnsavedRevision newRevision) {
                    Map<String, Object> properties = new HashMap<>();
                    Map<String, Object> householdProperties = new HashMap<>();
                    ResidentEntity.Household household = residentEntity.getHousehold();
                    properties.put(ResidentEntity.Household.NO_OF_FAMILY_IN_HOUSEHOLD, household.getNoOfFamilyInHousehold());
                    properties.put(ResidentEntity.Household.NO_OF_PEOPLE_IN_HOUSEHOLD, household.getNoOfPeopleInHousehold());
                    properties.put(ResidentEntity.Household.IS_FAMILY_4PS_MEMBER, household.isFamily4PsMember());
                    properties.put(ResidentEntity.HOUSEHOLD, householdProperties);
                    newRevision.setUserProperties(properties);
                    return true;
                }
            });
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}