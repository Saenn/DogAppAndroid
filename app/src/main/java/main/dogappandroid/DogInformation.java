package main.dogappandroid;

import android.os.Bundle;
import android.provider.BaseColumns;

public class DogInformation {

    private String dogType, address, subdistrict, district, province, submitDate, ageRange;
    private double latitude, longitude;
    private int age, isSubmit, id, dogID;

    public DogInformation() {
    }

    public DogInformation(Bundle dogInformation) {
        dogType = dogInformation.getString("dogType");
        ageRange = dogInformation.getString("ageRange");
        address = dogInformation.getString("address");
        subdistrict = dogInformation.getString("subdistrict");
        district = dogInformation.getString("district");
        province = dogInformation.getString("province");
        latitude = dogInformation.getDouble("latitude");
        longitude = dogInformation.getDouble("longitude");
        submitDate = dogInformation.getString("submitDate");
        age = dogInformation.getInt("age");
        isSubmit = 0; // 0 represent false 1 represent true
    }

    public static class DogInformationEntry implements BaseColumns {
        public static final String TABLE_NAME = "information";
        public static final String ID = BaseColumns._ID;
        public static final String INTERNAL_DOG_ID = "internalDogID";
        public static final String DOG_TYPE = "dogType";
        public static final String AGE = "age";
        public static final String AGE_RANGE = "ageRange";
        public static final String ADDRESS = "address";
        public static final String SUBDISTRICT = "subdistrict";
        public static final String DISTRICT = "district";
        public static final String PROVINCE = "province";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String SUBMIT_DATE = "submitDate";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogInformationEntry.TABLE_NAME + " (" +
                    DogInformationEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogInformationEntry.INTERNAL_DOG_ID + "INTEGER," +
                    DogInformationEntry.DOG_TYPE + "TEXT," +
                    DogInformationEntry.AGE + "INTEGER," +
                    DogInformationEntry.AGE_RANGE + "TEXT," +
                    DogInformationEntry.ADDRESS + "TEXT," +
                    DogInformationEntry.SUBDISTRICT + "TEXT," +
                    DogInformationEntry.DISTRICT + "TEXT," +
                    DogInformationEntry.PROVINCE + "TEXT," +
                    DogInformationEntry.LATITUDE + "REAL," +
                    DogInformationEntry.LONGITUDE + "REAL," +
                    DogInformationEntry.SUBMIT_DATE + "TEXT," +
                    DogInformationEntry.IS_SUBMIT + "INTEGER)";

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

    public String getDogType() {
        return dogType;
    }

    public void setDogType(String dogType) {
        this.dogType = dogType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
