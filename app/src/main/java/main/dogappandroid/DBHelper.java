package main.dogappandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, DogVaccine.DATABASE_NAME, null, DogVaccine.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_VACCINE_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT)",
                DogVaccine.TABLE,
                DogVaccine.Column.ID,
                DogVaccine.Column.VACCINE_NAME,
                DogVaccine.Column.VACCINE_DATE);

        // create friend table
        db.execSQL(CREATE_VACCINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_VACCINE_TABLE = "DROP TABLE IF EXISTS " + DogVaccine.TABLE;

        db.execSQL(DROP_VACCINE_TABLE);

        onCreate(db);
    }

    public List<DogVaccine> getVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.TABLE, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            vaccines.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }

    public List<DogVaccine> getRabiesVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies"};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.TABLE, null, "vaccine_name = ?",whereArgs , null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            vaccines.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }

    public List<DogVaccine> getOtherVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies"};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.TABLE, null, "vaccine_name != ?", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            vaccines.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }

    public void addVaccine(DogVaccine vaccine) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DogVaccine.Column.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.Column.VACCINE_DATE, vaccine.getDate());

        sqLiteDatabase.insert(DogVaccine.TABLE, null, values);
        sqLiteDatabase.close();
    }

}
