package com.blazingmuffin.health.mdmsystem.other.models;

import com.couchbase.lite.Document;

/**
 * Created by lenovo on 10/21/2017.
 */

public abstract class EntityBase {
    public static final String DELETED_AT = "deleted_at";
    private String mId;

    public void setId(String id) {
        this.mId = id;
    }
    public String getId() {
        return this.mId;
    }

    public String getPropertyString(Document entityDocument, String column) {
        Object property = entityDocument.getProperty(column);
        if (property != null) return property.toString();
        return "";
    }
}
