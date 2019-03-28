package main.dogappandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Dog {

    private int id, dogID, age, isSubmit;
    private String dogType, gender, color, name, breed, ageRange, address, subdistrict, district, province;
    private double latitude, longitude;

    public Dog() {
    }

    public Dog(Bundle bundle) {
        dogType = bundle.getString("dogType");
        name = bundle.getString("name");
        gender = bundle.getString("gender");
        color = bundle.getString("color");
        breed = bundle.getString("breed");
        if (bundle.getInt("age", -1) != -1) age = bundle.getInt("age");
        ageRange = bundle.getString("ageRange");
        address = bundle.getString("address");
        subdistrict = bundle.getString("subdistrict");
        district = bundle.getString("district");
        province = bundle.getString("province");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        isSubmit = 0;
    }

    public static class DogEntry implements BaseColumns {
        public static final String TABLE_NAME = "dog";
        public static final String ID = BaseColumns._ID;
        public static final String DOG_ID = "dogID"; // from rds database
        public static final String DOG_TYPE = "dogType";
        public static final String NAME = "name";
        public static final String GENDER = "gender";
        public static final String COLOR = "color";
        public static final String BREED = "breed";
        public static final String AGE = "age";
        public static final String AGE_RANGE = "ageRange";
        public static final String ADDRESS = "address";
        public static final String SUBDISTRICT = "subdistrict";
        public static final String DISTRICT = "district";
        public static final String PROVINCE = "province";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogEntry.TABLE_NAME + " (" +
                    DogEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogEntry.DOG_ID + " INTEGER," +
                    DogEntry.DOG_TYPE + " TEXT," +
                    DogEntry.GENDER + " TEXT," +
                    DogEntry.COLOR + " TEXT," +
                    DogEntry.BREED + " TEXT," +
                    DogEntry.NAME + " TEXT," +
                    DogEntry.AGE + " INTEGER," +
                    DogEntry.AGE_RANGE + " TEXT," +
                    DogEntry.ADDRESS + " TEXT," +
                    DogEntry.SUBDISTRICT + " TEXT," +
                    DogEntry.DISTRICT + " TEXT," +
                    DogEntry.PROVINCE + " TEXT," +
                    DogEntry.LATITUDE + " REAL," +
                    DogEntry.LONGITUDE + " REAL," +
                    DogEntry.IS_SUBMIT + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogEntry.TABLE_NAME;

    //    getter and setter

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

    public String getDogType() {
        return dogType;
    }

    public void setDogType(String dogType) {
        this.dogType = dogType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
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
}
