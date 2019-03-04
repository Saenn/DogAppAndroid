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

    private String gender, color, sterilizedDate, breed, registerDate, name;
    private int id, dogID, sterilized, isSubmit;

    public Dog() {
    }

    public Dog(Bundle bundle) {
        name = bundle.getString("name");
        gender = bundle.getString("gender");
        color = bundle.getString("color");
        breed = bundle.getString("breed");
        if (bundle.getBoolean("sterilized")) {
            sterilized = 1;
            sterilizedDate = bundle.getString("sterilizedDate");
        } else {
            sterilized = 0;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        registerDate = dateFormat.format(date);
        isSubmit = 0;
    }

    public static class DogEntry implements BaseColumns {
        public static final String TABLE_NAME = "dog";
        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String DOG_ID = "dogID"; // from rds database
        public static final String GENDER = "gender";
        public static final String COLOR = "color";
        public static final String STERILIZED = "sterilized";
        public static final String STERILIZED_DATE = "sterilizedDate";
        public static final String BREED = "breed";
        public static final String REGISTER_DATE = "registerDate";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogEntry.TABLE_NAME + " (" +
                    DogEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogEntry.DOG_ID + " INTEGER," +
                    DogEntry.GENDER + " TEXT," +
                    DogEntry.COLOR + " TEXT," +
                    DogEntry.STERILIZED + " INTEGER," +
                    DogEntry.STERILIZED_DATE + " TEXT," +
                    DogEntry.BREED + " TEXT," +
                    DogEntry.REGISTER_DATE + " TEXT," +
                    DogEntry.IS_SUBMIT + " INTEGER," +
                    DogEntry.NAME + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogEntry.TABLE_NAME;

    //    getter and setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSterilizedDate() {
        return sterilizedDate;
    }

    public void setSterilizedDate(String sterilizedDate) {
        this.sterilizedDate = sterilizedDate;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

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

    public int getSterilized() {
        return sterilized;
    }

    public void setSterilized(int sterilized) {
        this.sterilized = sterilized;
    }

    public int getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
    }

}
