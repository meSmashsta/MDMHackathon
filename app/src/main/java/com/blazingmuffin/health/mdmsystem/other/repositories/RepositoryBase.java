package com.blazingmuffin.health.mdmsystem.other.repositories;

import com.blazingmuffin.health.mdmsystem.other.models.EntityBase;
import com.couchbase.lite.Database;
import com.couchbase.lite.LiveQuery;

/**
 * Created by lenovo on 10/21/2017.
 */

abstract class RepositoryBase<T extends EntityBase> {
    private final Database mDatabase;

    public RepositoryBase (Database database) {
        this.mDatabase = database;
    }

    Database getDatabase() {
        return mDatabase;
    }

    public abstract T get(String id);
    public abstract T create(T entity);
    public abstract void update(T entity);
    public abstract void delete(T entity);
    public abstract LiveQuery list();
}
