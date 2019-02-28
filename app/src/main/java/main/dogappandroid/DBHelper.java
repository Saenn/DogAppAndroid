package main.dogappandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

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
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT )",
                DogVaccine.TABLE,
                DogVaccine.Column.ID,
                DogVaccine.Column.VACCINE_NAME,
                DogVaccine.Column.VACCINE_DATE,
                DogVaccine.Column.VACCINE_DOG_INTERNAL_ID
                );

        // create friend table
        db.execSQL(CREATE_VACCINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_VACCINE_TABLE = "DROP TABLE IF EXISTS " + DogVaccine.TABLE;

        db.execSQL(DROP_VACCINE_TABLE);

        onCreate(db);
    }

   public List<DogVaccine> getRabiesVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies"};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.TABLE, null, "vaccine_name = ? and vaccine_dog_internal_id is null",whereArgs , null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setId(cursor.getInt(0));
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
                (DogVaccine.TABLE, null, "vaccine_name != ? and vaccine_dog_internal_id is null", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setId(cursor.getInt(0));
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            vaccines.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }

    public List<DogVaccine> getRabiesVaccineListById(int dogID) {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();
        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies", String.valueOf(dogID)};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.TABLE, null, "vaccine_name != ? && ID = ?", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setId(cursor.getInt(0));
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            vaccines.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }

    public List<DogVaccine> getOtherVaccineListById(int id) {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies", String.valueOf(id)};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.TABLE, null, "vaccine_name != ? && ID = ?", whereArgs, null, null, null);

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
        Log.i("InternalID :" , String.valueOf(vaccine.getInternalId()));
        if(vaccine.getInternalId() != 0) {
            values.put(DogVaccine.Column.VACCINE_DOG_INTERNAL_ID, vaccine.getInternalId());
        }

        sqLiteDatabase.insert(DogVaccine.TABLE, null, values);
        sqLiteDatabase.close();
    }

    public void updateVaccine(DogVaccine vaccine) {

        sqLiteDatabase  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.Column.ID, vaccine.getId());
        values.put(DogVaccine.Column.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.Column.VACCINE_DATE, vaccine.getDate());
        values.put(DogVaccine.Column.VACCINE_DOG_INTERNAL_ID, vaccine.getInternalId());

        int row = sqLiteDatabase.update(DogVaccine.TABLE,
                values,
                DogVaccine.Column.ID + " = ? ",
                new String[] { String.valueOf(vaccine.getId()) });

        sqLiteDatabase.close();
    }

    public void updateVaccineWhileAddingDog(DogVaccine vaccine) {

        sqLiteDatabase  = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.Column.ID, vaccine.getId());
        values.put(DogVaccine.Column.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.Column.VACCINE_DATE, vaccine.getDate());

        int row = sqLiteDatabase.update(DogVaccine.TABLE,
                values,
                DogVaccine.Column.ID + " = ? ",
                new String[] { String.valueOf(vaccine.getId()) });

        sqLiteDatabase.close();
    }

    public void deleteVaccine(String id) {

        sqLiteDatabase = this.getWritableDatabase();

    /*sqLiteDatabase.delete(Friend.TABLE, Friend.Column.ID + " = ? ",
            new String[] { String.valueOf(friend.getId()) });*/
        sqLiteDatabase.delete(DogVaccine.TABLE, DogVaccine.Column.ID + " = " + id, null);
//        sqLiteDatabase.delete(DogVaccine.TABLE,null,null);
        sqLiteDatabase.close();
    }

    public void deleteNull(){

        sqLiteDatabase = this.getWritableDatabase();

    /*sqLiteDatabase.delete(Friend.TABLE, Friend.Column.ID + " = ? ",
            new String[] { String.valueOf(friend.getId()) });*/
        sqLiteDatabase.delete(DogVaccine.TABLE, DogVaccine.Column.VACCINE_DOG_INTERNAL_ID + " is null ", null);
//        sqLiteDatabase.delete(DogVaccine.TABLE,null,null);
        sqLiteDatabase.close();
    }
}
