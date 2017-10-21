package com.blazingmuffin.health.mdmsystem.other.models;

import com.couchbase.lite.Document;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentEntity extends EntityBase {
    public static final String FULL_NAME = "full_name";
    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LAST_NAME = "last_name";
    public static final String GENDER = "gender";
    public static final String BIRTHDATE = "birthdate";

    public static final String HOUSEHOLD = "household";

    public static final String VIEW = "resident-view";
    public static final String VIEW_VERSION = "1";

    public static final String TYPE = "type";
    public static final String TYPE_VALUE = "resident";


    private String mFullName;
    private String mFirstName;
    private String mMiddleName;
    private String mLastName;
    private String gender;
    private String mBirthdate;
    private Household mHousehold;

    {
        Household household = new Household();
        setHousehold(household);
    }

    public ResidentEntity() { }

    public ResidentEntity(Document residentDocument) {
        setFullName(getPropertyString(residentDocument, FULL_NAME));
        setGender(getPropertyString(residentDocument, GENDER));
        setBirthdate(getPropertyString(residentDocument, BIRTHDATE));
//        setFirstName(residentDocument.getPropertyString(FIRST_NAME).toString());
//        setMiddleName(residentDocument.getPropertyString(MIDDLE_NAME).toString());
//        setLastName(residentDocument.getPropertyString(LAST_NAME).toString());
        Object householdProperties = residentDocument.getProperty(HOUSEHOLD);
        if (householdProperties != null) {
            Household household = getHousehold();
            household.setNoOfFamilyInHousehold(Integer.parseInt(residentDocument.getProperty(Household.NO_OF_FAMILY_IN_HOUSEHOLD).toString()));
            household.setNoOfPeopleInHousehold(Integer.parseInt(residentDocument.getProperty(Household.NO_OF_PEOPLE_IN_HOUSEHOLD).toString()));
            household.setFamily4PsMember(Boolean.valueOf(residentDocument.getProperty(Household.IS_FAMILY_4PS_MEMBER).toString()));
        }
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
        return this.mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
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

    public Household getHousehold() {
        return this.mHousehold;
    }

    public void setHousehold(Household household) {
        this.mHousehold = household;
    }

    public class Household {
        public static final String NO_OF_FAMILY_IN_HOUSEHOLD = "no_of_family_in_household";
        public static final String NO_OF_PEOPLE_IN_HOUSEHOLD = "no_of_people_in_household";
        public static final String IS_FAMILY_4PS_MEMBER = "is_family_4ps_member";

        private int mNoOfFamilyInHousehold,
                    mNoOfPeopleInHousehold;

        private boolean mIsFamily4PsMember;

        private Household() {}

        public int getNoOfFamilyInHousehold() {
            return mNoOfFamilyInHousehold;
        }

        public void setNoOfFamilyInHousehold(int noOfFamilyInHousehold) {
            this.mNoOfFamilyInHousehold = noOfFamilyInHousehold;
        }

        public int getNoOfPeopleInHousehold() {
            return mNoOfPeopleInHousehold;
        }

        public void setNoOfPeopleInHousehold(int noOfPeopleInHousehold) {
            this.mNoOfPeopleInHousehold = noOfPeopleInHousehold;
        }

        public boolean isFamily4PsMember() {
            return mIsFamily4PsMember;
        }

        public void setFamily4PsMember(boolean family4PsMember) {
            mIsFamily4PsMember = family4PsMember;
        }
    }
}
