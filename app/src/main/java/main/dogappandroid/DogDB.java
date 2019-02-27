package main.dogappandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public final class DogDB {

    private String gender,color,sterilizedDate,breed,registerDate;
    private int id,dogID,sterilized,isSubmit;

    public DogDB() {
    }

    // getter & setter //

    public int getDogID() {
        return dogID;
    }

    public int getSterilized() {
        return sterilized;
    }

    public int getIsSubmit() {
        return isSubmit;
    }

    public void setDogID(int dogID) {
        this.dogID = dogID;
    }

    public void setSterilized(int sterilized) {
        this.sterilized = sterilized;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
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

    // end of getter & setter //

    public static class DogDBEntry implements BaseColumns {
        public static final String TABLE_NAME = "dog";
        public static final String ID = BaseColumns._ID;
        public static final String COLUMN_NAME_DOG_ID = "dogID"; // from rds database
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_STERILIZED = "sterilized";
        public static final String COLUMN_NAME_STERILIZED_DATE = "sterilizedDate";
        public static final String COLUMN_NAME_BREED = "breed";
        public static final String COLUMN_NAME_REGISTER_DATE = "registerDate";
        public static final String COLUMN_NAME_IS_SUBMIT = "isSubmit";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogDBEntry.TABLE_NAME + " (" +
                    DogDBEntry._ID + " INTEGER PRIMARY KEY," +
                    DogDBEntry.COLUMN_NAME_DOG_ID + " INTEGER," +
                    DogDBEntry.COLUMN_NAME_GENDER + " TEXT," +
                    DogDBEntry.COLUMN_NAME_COLOR + " TEXT," +
                    DogDBEntry.COLUMN_NAME_STERILIZED + " INTEGER," +
                    DogDBEntry.COLUMN_NAME_STERILIZED_DATE + " TEXT," +
                    DogDBEntry.COLUMN_NAME_BREED + " TEXT," +
                    DogDBEntry.COLUMN_NAME_REGISTER_DATE + " TEXT," +
                    DogDBEntry.COLUMN_NAME_IS_SUBMIT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogDBEntry.TABLE_NAME;

    public class DogDBHelper extends SQLiteOpenHelper {

        private SQLiteDatabase sqLiteDatabase;
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "dog.db";

        public DogDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public void addDogDB(DogDB dog) {

            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DogDBEntry.COLUMN_NAME_DOG_ID, dog.getDogID());
            values.put(DogDBEntry.COLUMN_NAME_BREED, dog.getBreed());
            values.put(DogDBEntry.COLUMN_NAME_COLOR, dog.getColor());
            values.put(DogDBEntry.COLUMN_NAME_GENDER, dog.getGender());
            values.put(DogDBEntry.COLUMN_NAME_REGISTER_DATE, dog.getRegisterDate());
            values.put(DogDBEntry.COLUMN_NAME_STERILIZED, dog.getSterilized());
            values.put(DogDBEntry.COLUMN_NAME_STERILIZED_DATE, dog.getSterilizedDate());
            values.put(DogDBEntry.COLUMN_NAME_IS_SUBMIT, 0);

            sqLiteDatabase.insert(DogVaccine.TABLE, null, values);
            sqLiteDatabase.close();
        }

        public List<DogDB> getDogDB() {

            List<DogDB> dogs = new ArrayList<DogDB>();

            sqLiteDatabase = this.getWritableDatabase();

            Cursor cursor = sqLiteDatabase.query
                    (DogDBEntry.TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }

            while(!cursor.isAfterLast()) {

                DogDB tmp = new DogDB();
                tmp.setId(cursor.getInt(0));
                tmp.setDogID(cursor.getInt(1));
                tmp.setGender(cursor.getString(2));
                tmp.setColor(cursor.getString(3));
                tmp.setSterilized(cursor.getInt(4));
                tmp.setSterilizedDate(cursor.getString(5));
                tmp.setBreed(cursor.getString(6));
                tmp.setRegisterDate(cursor.getString(7));
                tmp.setIsSubmit(0);
                dogs.add(tmp);

                cursor.moveToNext();
            }

            sqLiteDatabase.close();

            return dogs;
        }

        public void updateDogDB(DogDB dog) {

            sqLiteDatabase  = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DogVaccine.Column.ID, dog.getId());
            values.put(DogDBEntry.COLUMN_NAME_DOG_ID, dog.getDogID());
            values.put(DogDBEntry.COLUMN_NAME_BREED, dog.getBreed());
            values.put(DogDBEntry.COLUMN_NAME_COLOR, dog.getColor());
            values.put(DogDBEntry.COLUMN_NAME_GENDER, dog.getGender());
            values.put(DogDBEntry.COLUMN_NAME_REGISTER_DATE, dog.getRegisterDate());
            values.put(DogDBEntry.COLUMN_NAME_STERILIZED, dog.getSterilized());
            values.put(DogDBEntry.COLUMN_NAME_STERILIZED_DATE, dog.getSterilizedDate());
            values.put(DogDBEntry.COLUMN_NAME_IS_SUBMIT, 0);


            int row = sqLiteDatabase.update(DogDBEntry.TABLE_NAME,
                    values,
                    DogDBEntry.ID + " = ? ",
                    new String[] { String.valueOf(dog.getId()) });

            sqLiteDatabase.close();
        }

        public void deleteDogDB(String id) {

            sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.delete(DogDBEntry.TABLE_NAME, DogDBEntry.ID + " = " + id, null);
            sqLiteDatabase.close();
        }
    }
}
