package com.blazingmuffin.health.mdmsystem.other.repositories;

import com.blazingmuffin.health.mdmsystem.other.models.EntityBase;
import com.couchbase.lite.Database;
import com.couchbase.lite.QueryEnumerator;

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
    public abstract T save(T entity);
    public abstract boolean edit(T entity);
    public abstract boolean delete(T entity);
    public abstract QueryEnumerator list();
}
