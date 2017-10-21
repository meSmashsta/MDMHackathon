package com.blazingmuffin.health.mdmsystem.other.repositories;

import com.blazingmuffin.health.mdmsystem.other.models.EntityBase;
import com.couchbase.lite.Database;
import com.couchbase.lite.LiveQuery;

/**
 * Created by lenovo on 10/21/2017.
 */

public abstract class RepositoryBase<T extends EntityBase> {
    protected Database mDatabase;

    public RepositoryBase (Database database) {
        setDatabase(database);
    }

    public Database getDatabase() {
        return mDatabase;
    }

    public void setDatabase(Database database) {
        mDatabase = database;
    }

    public abstract T get(String id);
    public abstract T create(T entity);
    public abstract void update(T entity);
    public abstract void delete(T entity);
    public abstract LiveQuery list();
}
