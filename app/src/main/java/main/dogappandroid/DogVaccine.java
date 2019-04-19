package main.dogappandroid;

import android.provider.BaseColumns;

public class DogVaccine {

    private String name, date;
    private int id, dogID, isSubmit, position;

    public DogVaccine() {
    }

    public class DogVaccineEntry {
        public static final String TABLE_NAME = "vaccine";
        public static final String ID = BaseColumns._ID;
        public static final String VACCINE_NAME = "vaccineName";
        public static final String VACCINE_DATE = "vaccineDate";
        public static final String DOG_INTERNAL_ID = "dogInternalID";
        public static final String VACCINE_POSITION = "position";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogVaccineEntry.TABLE_NAME + " (" +
                    DogVaccineEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogVaccineEntry.VACCINE_NAME + " TEXT," +
                    DogVaccineEntry.VACCINE_DATE + " TEXT," +
                    DogVaccineEntry.DOG_INTERNAL_ID + " INTEGER," +
                    DogVaccineEntry.VACCINE_POSITION + " INTEGER," +
                    DogVaccineEntry.IS_SUBMIT + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogVaccineEntry.TABLE_NAME;

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

    public int getDogID() {
        return dogID;
    }

    public void setDogID(int dogID) {
        this.dogID = dogID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
    }
}
