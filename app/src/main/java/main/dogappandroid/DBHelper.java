package main.dogappandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dog.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Dog.SQL_CREATE_ENTRIES);
        db.execSQL(DogInformation.SQL_CREATE_ENTRIES);
        db.execSQL(DogVaccine.SQL_CREATE_ENTRIES);
        db.execSQL(DogImage.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Dog.SQL_DELETE_ENTRIES);
        db.execSQL(DogInformation.SQL_DELETE_ENTRIES);
        db.execSQL(DogVaccine.SQL_DELETE_ENTRIES);
        db.execSQL(DogImage.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Dog Image

    // convert from bitmap to byte array
    // insert to db
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    // retrieve data
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public long addDogImage(DogImage dogImage) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DogImage.DogImageEntry.KEY_IMAGE, dogImage.getKeyImage());
        values.put(DogImage.DogImageEntry.DOG_INTERNAL_ID, dogImage.getDog_internal_id());
        values.put(DogImage.DogImageEntry.TYPE, dogImage.getType());

        long index = sqLiteDatabase.insert(DogImage.DogImageEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        Log.i("add picture ได้แล้วเด้อ"," xD");
        return index;

        // getImage byte[] image = cursor.getBlob(1);

    }

    public List<DogImage> getDogImageById(int id) {
        List<DogImage> dogImages = new ArrayList<>();
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (DogImage.DogImageEntry.TABLE_NAME, null, DogImage.DogImageEntry.ID + " = " + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            DogImage tmp = new DogImage();

            tmp.setId(cursor.getInt(0));
            tmp.setKeyImage(cursor.getBlob(1));
            tmp.setType(cursor.getInt(2));
            tmp.setDog_internal_id(cursor.getInt(3));

            dogImages.add(tmp);
            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return dogImages;
    }

    //    Dog
    public long addDog(Dog dog) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Dog.DogEntry.BREED, dog.getBreed());
        values.put(Dog.DogEntry.COLOR, dog.getColor());
        values.put(Dog.DogEntry.GENDER, dog.getGender());
        values.put(Dog.DogEntry.REGISTER_DATE, dog.getRegisterDate());
        values.put(Dog.DogEntry.STERILIZED, dog.getSterilized());
        values.put(Dog.DogEntry.STERILIZED_DATE, dog.getSterilizedDate());
        values.put(Dog.DogEntry.IS_SUBMIT, 0);
        values.put(Dog.DogEntry.NAME, dog.getName());

        long index = sqLiteDatabase.insert(Dog.DogEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        return index;
    }

    public Dog getDogById(int id) {
        Dog tmp = new Dog();
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (Dog.DogEntry.TABLE_NAME, null, Dog.DogEntry.ID + " = " + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            tmp.setId(cursor.getInt(0));
            tmp.setDogID(cursor.getInt(1));
            tmp.setGender(cursor.getString(2));
            tmp.setColor(cursor.getString(3));
            tmp.setSterilized(cursor.getInt(4));
            tmp.setSterilizedDate(cursor.getString(5));
            tmp.setBreed(cursor.getString(6));
            tmp.setRegisterDate(cursor.getString(7));
            tmp.setIsSubmit(0);
            tmp.setName(cursor.getString(9));

            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return tmp;
    }

    public List<Dog> getDog() {

        List<Dog> dogs = new ArrayList<Dog>();

        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (Dog.DogEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            Dog tmp = new Dog();
            tmp.setId(cursor.getInt(0));
            tmp.setDogID(cursor.getInt(1));
            tmp.setGender(cursor.getString(2));
            tmp.setColor(cursor.getString(3));
            tmp.setSterilized(cursor.getInt(4));
            tmp.setSterilizedDate(cursor.getString(5));
            tmp.setBreed(cursor.getString(6));
            tmp.setRegisterDate(cursor.getString(7));
            tmp.setName(cursor.getString(9));
            tmp.setIsSubmit(0);
            dogs.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return dogs;
    }

    public void updateDog(Dog dog) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Dog.DogEntry.ID, dog.getId());
        values.put(Dog.DogEntry.DOG_ID, dog.getDogID());
        values.put(Dog.DogEntry.BREED, dog.getBreed());
        values.put(Dog.DogEntry.COLOR, dog.getColor());
        values.put(Dog.DogEntry.GENDER, dog.getGender());
        values.put(Dog.DogEntry.REGISTER_DATE, dog.getRegisterDate());
        values.put(Dog.DogEntry.STERILIZED, dog.getSterilized());
        values.put(Dog.DogEntry.STERILIZED_DATE, dog.getSterilizedDate());
        values.put(Dog.DogEntry.IS_SUBMIT, 0);


        int row = sqLiteDatabase.update(Dog.DogEntry.TABLE_NAME,
                values,
                Dog.DogEntry.ID + " = ? ",
                new String[]{String.valueOf(dog.getId())});

        sqLiteDatabase.close();
    }

    public void deleteDog(String id) {

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Dog.DogEntry.TABLE_NAME, Dog.DogEntry.ID + " = " + id, null);
        sqLiteDatabase.close();
    }

    //    Dog Information
    public long addDogInformation(DogInformation dogInformation) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DogInformation.DogInformationEntry.INTERNAL_DOG_ID, dogInformation.getDogID());
        values.put(DogInformation.DogInformationEntry.SUBMIT_DATE, dogInformation.getSubmitDate());
        values.put(DogInformation.DogInformationEntry.DOG_TYPE, dogInformation.getDogType());
        values.put(DogInformation.DogInformationEntry.AGE, dogInformation.getAge());
        values.put(DogInformation.DogInformationEntry.AGE_RANGE, dogInformation.getAgeRange());
        values.put(DogInformation.DogInformationEntry.ADDRESS, dogInformation.getAddress());
        values.put(DogInformation.DogInformationEntry.SUBDISTRICT, dogInformation.getSubdistrict());
        values.put(DogInformation.DogInformationEntry.DISTRICT, dogInformation.getDistrict());
        values.put(DogInformation.DogInformationEntry.PROVINCE, dogInformation.getProvince());
        values.put(DogInformation.DogInformationEntry.LATITUDE, dogInformation.getLatitude());
        values.put(DogInformation.DogInformationEntry.LONGITUDE, dogInformation.getLongitude());
        values.put(DogInformation.DogInformationEntry.IS_SUBMIT, dogInformation.getIsSubmit());
        long index = sqLiteDatabase.insert(DogInformation.DogInformationEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();
        return index;
    }

    public DogInformation getAllDogInformationByDogID(int dogID) {
        DogInformation dogInformation = new DogInformation();
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query
                (DogInformation.DogInformationEntry.TABLE_NAME,
                        null,
                        DogInformation.DogInformationEntry.INTERNAL_DOG_ID + " = " + dogID,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            dogInformation.setId(cursor.getInt(0));
            dogInformation.setDogID(cursor.getInt(1));
            dogInformation.setDogType(cursor.getString(2));
            dogInformation.setAge(cursor.getInt(3));
            dogInformation.setAgeRange(cursor.getString(4));
            dogInformation.setAddress(cursor.getString(5));
            dogInformation.setSubdistrict(cursor.getString(6));
            dogInformation.setDistrict(cursor.getString(7));
            dogInformation.setProvince(cursor.getString(8));
            dogInformation.setLatitude(cursor.getDouble(9));
            dogInformation.setLongitude(cursor.getDouble(10));
            dogInformation.setSubmitDate(cursor.getString(11));
            dogInformation.setIsSubmit(cursor.getInt(12));

            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return dogInformation;
    }

    public void updateDogInfo(DogInformation dogInformation) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogInformation.DogInformationEntry.INTERNAL_DOG_ID, dogInformation.getDogID());
        values.put(DogInformation.DogInformationEntry.SUBMIT_DATE, dogInformation.getSubmitDate());
        values.put(DogInformation.DogInformationEntry.DOG_TYPE, dogInformation.getDogType());
        values.put(DogInformation.DogInformationEntry.AGE, dogInformation.getAge());
        values.put(DogInformation.DogInformationEntry.AGE_RANGE, dogInformation.getAgeRange());
        values.put(DogInformation.DogInformationEntry.ADDRESS, dogInformation.getAddress());
        values.put(DogInformation.DogInformationEntry.SUBDISTRICT, dogInformation.getSubdistrict());
        values.put(DogInformation.DogInformationEntry.DISTRICT, dogInformation.getDistrict());
        values.put(DogInformation.DogInformationEntry.PROVINCE, dogInformation.getProvince());
        values.put(DogInformation.DogInformationEntry.LATITUDE, dogInformation.getLatitude());
        values.put(DogInformation.DogInformationEntry.LONGITUDE, dogInformation.getLongitude());
        values.put(DogInformation.DogInformationEntry.IS_SUBMIT, dogInformation.getIsSubmit());

        int row = sqLiteDatabase.update(DogInformation.DogInformationEntry.TABLE_NAME,
                values,
                DogInformation.DogInformationEntry.ID + " = ? ",
                new String[]{String.valueOf(dogInformation.getId())});

        sqLiteDatabase.close();
    }

    public void deleteDogInfo(String id) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DogInformation.DogInformationEntry.TABLE_NAME, DogInformation.DogInformationEntry.ID + " = " + id, null);
        sqLiteDatabase.close();
    }

    //    Vaccine
    public List<DogVaccine> getRabiesVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies"};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccine_name = ? and vaccine_dog_internal_id is null", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

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
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccine_name != ? and vaccine_dog_internal_id is null", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

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
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccine_name = ? and vaccine_dog_internal_id = ?", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setId(cursor.getInt(0));
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            tmp.setDogID(cursor.getInt(3));
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
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccine_name != ? and vaccine_dog_internal_id = ?", whereArgs, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            DogVaccine tmp = new DogVaccine();
            tmp.setId(cursor.getInt(0));
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            tmp.setDogID(cursor.getInt(3));
            vaccines.add(tmp);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }

    public List<DogVaccine> getTwoLatestVaccines(int dogID) {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();
        sqLiteDatabase = this.getWritableDatabase();
        int i = 0;
        String[] whereArgs = new String[]{String.valueOf(dogID)};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccine_dog_internal_id = ?", whereArgs, null, null, DogVaccine.DogVaccineEntry.ID + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast() && i < 2) {

            DogVaccine tmp = new DogVaccine();
            tmp.setId(cursor.getInt(0));
            tmp.setName(cursor.getString(1));
            tmp.setDate(cursor.getString(2));
            tmp.setDogID(cursor.getInt(3));
            vaccines.add(tmp);
            i++;
            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return vaccines;
    }


    public void addVaccine(DogVaccine vaccine) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.DogVaccineEntry.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_DATE, vaccine.getDate());

        if (vaccine.getDogID() != 0) {
            values.put(DogVaccine.DogVaccineEntry.VACCINE_DOG_INTERNAL_ID, vaccine.getDogID());
        }

        sqLiteDatabase.insert(DogVaccine.DogVaccineEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();
    }

    public void updateVaccine(DogVaccine vaccine) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.DogVaccineEntry.ID, vaccine.getId());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_DATE, vaccine.getDate());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_DOG_INTERNAL_ID, vaccine.getDogID());

        int row = sqLiteDatabase.update(DogVaccine.DogVaccineEntry.TABLE_NAME,
                values,
                DogVaccine.DogVaccineEntry.ID + " = ? ",
                new String[]{String.valueOf(vaccine.getId())});

        sqLiteDatabase.close();
    }

    public void updateVaccineWhileAddingDog(DogVaccine vaccine) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.DogVaccineEntry.ID, vaccine.getId());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_DATE, vaccine.getDate());

        int row = sqLiteDatabase.update(DogVaccine.DogVaccineEntry.TABLE_NAME,
                values,
                DogVaccine.DogVaccineEntry.ID + " = ? ",
                new String[]{String.valueOf(vaccine.getId())});

        sqLiteDatabase.close();
    }

    public void deleteVaccine(String id) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DogVaccine.DogVaccineEntry.TABLE_NAME, DogVaccine.DogVaccineEntry.ID + " = " + id, null);
        sqLiteDatabase.close();
    }

    public void deleteNull() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DogVaccine.DogVaccineEntry.TABLE_NAME, DogVaccine.DogVaccineEntry.VACCINE_DOG_INTERNAL_ID + " is null ", null);
        sqLiteDatabase.close();
    }

}
