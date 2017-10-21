package com.blazingmuffin.health.mdmsystem.other.models;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentEntity extends EntityBase {
    private String mFirstName;
    private String mMiddleName;
    private String mLastName;
    private String mBirthdate;

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

    public String getBirthdate() {
        return mBirthdate;
    }

    public void setBirthdate(String birthdate) {
        this.mBirthdate = birthdate;
    }
}
