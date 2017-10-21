package com.blazingmuffin.health.mdmsystem.other.models;

import com.couchbase.lite.Document;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentEntity extends EntityBase {
    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LAST_NAME = "last_name";
    public static final String GENDER = "gender";
    public static final String BIRTHDATE = "birthdate";

    public static final String VIEW = "resident-view";
    public static final String VIEW_VERSION = "1";

    public static final String TYPE = "type";
    public static final String TYPE_VALUE = "resident";


    private String mFirstName;
    private String mMiddleName;
    private String mLastName;
    private String gender;
    private String mBirthdate;

    public ResidentEntity() { }

    public ResidentEntity(Document residentDocument) {
        setFirstName(residentDocument.getProperty(FIRST_NAME).toString());
        setMiddleName(residentDocument.getProperty(MIDDLE_NAME).toString());
        setLastName(residentDocument.getProperty(LAST_NAME).toString());
        setGender(residentDocument.getProperty(GENDER).toString());
        setBirthdate(residentDocument.getProperty(BIRTHDATE).toString());
        setId(residentDocument.getId());
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public void setMiddleName(String middleName) {
        this.mMiddleName = middleName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getFullName() {
        return String.format("%1$s %2$s", getFirstName(), getLastName());
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return mBirthdate;
    }

    public void setBirthdate(String birthdate) {
        this.mBirthdate = birthdate;
    }
}
