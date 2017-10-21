package com.blazingmuffin.health.mdmsystem.other.repositories;

import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.couchbase.lite.Database;
import com.couchbase.lite.QueryEnumerator;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentRepository extends RepositoryBase<ResidentEntity> {
    public ResidentRepository (Database database) {
        super(database);
    }

    @Override
    public ResidentEntity get(String id) {
        return null;
    }

    @Override
    public ResidentEntity save(ResidentEntity entity) {
        return null;
    }

    @Override
    public boolean edit(ResidentEntity entity) {
        return false;
    }

    @Override
    public boolean delete(ResidentEntity entity) {
        return false;
    }

    @Override
    public QueryEnumerator list() {
        return null;
    }
}
