package com.blazingmuffin.health.mdmsystem.other.repositories;

import com.blazingmuffin.health.mdmsystem.other.interfaces.IUpdatable;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentRepository extends RepositoryBase<ResidentEntity> {
    private IUpdatable<ResidentEntity> IUpdatable;

    public ResidentRepository (Database database) {
        super(database);
    }

    @Override
    public ResidentEntity get(String id) {
        Document residentDocument = mDatabase.getDocument(id);
        ResidentEntity residentEntity = new ResidentEntity(residentDocument);
        return residentEntity;
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
        Document residentDocument = mDatabase.createDocument();
        try {
            residentDocument.putProperties(properties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        residentEntity.setId(residentDocument.getId());
        return residentEntity;
    }

    public IUpdatable<ResidentEntity> getIUpdatable() {
        return IUpdatable;
    }

    public void setIUpdatable(IUpdatable<ResidentEntity> IUpdatable) {
        this.IUpdatable = IUpdatable;
    }

    @Override
    public ResidentEntity update(ResidentEntity residentEntity) {
        return IUpdatable.update(residentEntity);
    }

    @Override
    public boolean delete(ResidentEntity entity) {
        return false;
    }

    @Override
    public LiveQuery list() {
        com.couchbase.lite.View cview = mDatabase.getView(ResidentEntity.VIEW);
        if (cview.getMap() == null) {
            cview.setMap(new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (document.get(ResidentEntity.TYPE).toString().equalsIgnoreCase(ResidentEntity.TYPE_VALUE)) {
                        emitter.emit(document, document.get(ResidentEntity.FIRST_NAME));
                    }
                }
            }, ResidentEntity.VIEW_VERSION);
        }

        LiveQuery liveQuery = cview.createQuery().toLiveQuery();
        return liveQuery;
    }
}
