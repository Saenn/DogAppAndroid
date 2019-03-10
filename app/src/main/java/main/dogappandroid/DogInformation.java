package main.dogappandroid;

import android.os.Bundle;
import android.provider.BaseColumns;

public class DogInformation {

    private String dogStatus, deathRemark, missingDate, sterilizedDate, ageRange;
    private int id, pregnant, childNumber, sterilized, age, isSubmit, dogID;
//    note, dogID is id from internal db

    public DogInformation() {
    }

    public DogInformation(Bundle dogInformation) {
        dogStatus = dogInformation.getString("dogStatus");
        ageRange = dogInformation.getString("ageRange");
        deathRemark = dogInformation.getString("deathRemark");
        missingDate = dogInformation.getString("missingDate");
        sterilizedDate = dogInformation.getString("sterilizedDate");
        age = dogInformation.getInt("age");
        childNumber = dogInformation.getInt("childNumber");
        sterilized = dogInformation.getInt("sterilized");
        pregnant = dogInformation.getInt("pregnant");
        isSubmit = 0; // 0 represent false 1 represent true
    }

    public static class DogInformationEntry implements BaseColumns {
        public static final String TABLE_NAME = "information";
        public static final String ID = BaseColumns._ID;
        public static final String INTERNAL_DOG_ID = "internalDogID";
        public static final String DOG_STATUS = "dogStatus";
        public static final String PREGNANT = "pregnant";
        public static final String CHILD_NUMBER = "childNumber";
        public static final String DEATH_REMARK = "deathRemark";
        public static final String MISSING_DATE = "missingDate";
        public static final String STERILIZED = "sterilized";
        public static final String STERILIZED_DATE = "sterilizedDate";
        public static final String AGE = "age";
        public static final String AGE_RANGE = "ageRange";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogInformationEntry.TABLE_NAME + " (" +
                    DogInformationEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogInformationEntry.INTERNAL_DOG_ID + " INTEGER," +
                    DogInformationEntry.DOG_STATUS + " TEXT," +
                    DogInformationEntry.PREGNANT + " INTEGER," +
                    DogInformationEntry.CHILD_NUMBER + " INTEGER," +
                    DogInformationEntry.DEATH_REMARK + " TEXT," +
                    DogInformationEntry.MISSING_DATE + " TEXT," +
                    DogInformationEntry.STERILIZED + " INTEGER," +
                    DogInformationEntry.STERILIZED_DATE + " TEXT," +
                    DogInformationEntry.AGE + " INTEGER," +
                    DogInformationEntry.AGE_RANGE + " TEXT," +
                    DogInformationEntry.IS_SUBMIT + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogInformationEntry.TABLE_NAME;

    // getter and setter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDogID() {
        return dogID;
    }

    public void setDogID(int dogID) {
        this.dogID = dogID;
    }

    public String getDogStatus() {
        return dogStatus;
    }

    public void setDogStatus(String dogStatus) {
        this.dogStatus = dogStatus;
    }

    public String getDeathRemark() {
        return deathRemark;
    }

    public void setDeathRemark(String deathRemark) {
        this.deathRemark = deathRemark;
    }

    public String getMissingDate() {
        return missingDate;
    }

    public void setMissingDate(String missingDate) {
        this.missingDate = missingDate;
    }

    public String getSterilizedDate() {
        return sterilizedDate;
    }

    public void setSterilizedDate(String sterilizedDate) {
        this.sterilizedDate = sterilizedDate;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public int getPregnant() {
        return pregnant;
    }

    public void setPregnant(int pregnant) {
        this.pregnant = pregnant;
    }

    public int getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
    }

    public int getSterilized() {
        return sterilized;
    }

    public void setSterilized(int sterilized) {
        this.sterilized = sterilized;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
    }
}
