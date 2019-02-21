package main.dogappandroid;

import android.provider.BaseColumns;

public class DogVaccine {

    //Database
    public static final String DATABASE_NAME = "vaccine.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "vaccine";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String VACCINE_NAME = "vaccine_name";
        public static final String VACCINE_DATE = "vaccine_date";
    }

    public DogVaccine(){

    }

    private String name,date;
    private int id;


    public DogVaccine(int id,String name, String date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
