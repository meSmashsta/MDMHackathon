package com.blazingmuffin.health.mdmsystem.other.repositories;

import com.blazingmuffin.health.mdmsystem.other.interfaces.IUpdatable;
import com.blazingmuffin.health.mdmsystem.other.models.EntityBase;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.UnsavedRevision;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentRepository extends RepositoryBase<ResidentEntity> {
    private IUpdatable IUpdatable;

    public ResidentRepository (Database database) {
        super(database);
    }

    @Override
    public ResidentEntity get(String id) {
        return new ResidentEntity(getDatabase().getDocument(id));
    }

    @Override
    public ResidentEntity create(ResidentEntity residentEntity) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ResidentEntity.FIRST_NAME, residentEntity.getFirstName());
        properties.put(ResidentEntity.MIDDLE_NAME, residentEntity.getMiddleName());
        properties.put(ResidentEntity.LAST_NAME, residentEntity.getLastName());
        properties.put(ResidentEntity.GENDER, residentEntity.getGender());
        properties.put(ResidentEntity.BIRTHDATE, residentEntity.getBirthdate());
        properties.put(ResidentEntity.TYPE, ResidentEntity.TYPE_VALUE);
        Document residentDocument = getDatabase().createDocument();
        try {
            residentDocument.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        residentEntity.setId(residentDocument.getId());
        return residentEntity;
    }

    @Override
    public void update(ResidentEntity residentEntity) {
        IUpdatable.update(residentEntity);
    }

    @Override
    public void delete(ResidentEntity residentEntity) {
        Document residentDocument = getDatabase().getDocument(residentEntity.getId());
        try {
            residentDocument.update(ResidentRepository::delete);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LiveQuery list() {
        com.couchbase.lite.View view = getDatabase().getView(ResidentEntity.VIEW);
        if (view.getMap() == null) {
            view.setMap((document, emitter) -> {
                if (document.get(ResidentEntity.TYPE).toString().equalsIgnoreCase(ResidentEntity.TYPE_VALUE)) {
                    emitter.emit(document, document.get(ResidentEntity.FIRST_NAME));
                }
            }, ResidentEntity.VIEW_VERSION);
        }
        return view.createQuery().toLiveQuery();
    }

    public IUpdatable getIUpdatable() {
        return IUpdatable;
    }

    public void setIUpdatable(IUpdatable IUpdatable) {
        this.IUpdatable = IUpdatable;
    }

    private static boolean delete(UnsavedRevision revision) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Map<String, Object> properties = revision.getUserProperties();
        properties.put(EntityBase.DELETED_AT, simpleDateFormat.format(new Date()));
        revision.setUserProperties(properties);
        revision.setIsDeletion(true);
        return true;
    }
}
