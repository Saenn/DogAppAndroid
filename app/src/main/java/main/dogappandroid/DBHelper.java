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
    synchronized public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    // retrieve data
    synchronized public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    synchronized public long addDogImage(DogImage dogImage) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DogImage.DogImageEntry.KEY_IMAGE, dogImage.getKeyImage());
        values.put(DogImage.DogImageEntry.DOG_INTERNAL_ID, dogImage.getDog_internal_id());
        values.put(DogImage.DogImageEntry.TYPE, dogImage.getType());

        long index = sqLiteDatabase.insert(DogImage.DogImageEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        Log.i("add picture ได้แล้วเด้อ", " xD");
        return index;

        // getImage byte[] image = cursor.getBlob(1);

    }

    synchronized public void updateDogImage(DogImage dogImage, int type) {

        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DogImage.DogImageEntry.ID, dogImage.getId());
        values.put(DogImage.DogImageEntry.KEY_IMAGE, dogImage.getKeyImage());
        values.put(DogImage.DogImageEntry.DOG_INTERNAL_ID, dogImage.getDog_internal_id());
        values.put(DogImage.DogImageEntry.TYPE, dogImage.getType());


        int row = sqLiteDatabase.update(DogImage.DogImageEntry.TABLE_NAME,
                values,
                DogImage.DogImageEntry.ID + " = ? and " + DogImage.DogImageEntry.TYPE + " = " + type,
                new String[]{String.valueOf(dogImage.getId())});

        sqLiteDatabase.close();
    }

    synchronized public List<DogImage> getDogImageById(int id) {
        List<DogImage> dogImages = new ArrayList<>();
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (DogImage.DogImageEntry.TABLE_NAME, null, DogImage.DogImageEntry.DOG_INTERNAL_ID + " = " + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            DogImage tmp = new DogImage();

            tmp.setId(cursor.getInt(0));
            tmp.setKeyImage(cursor.getBlob(1));
            tmp.setType(cursor.getInt(2));
            tmp.setIsSubmit(cursor.getInt(3));
            tmp.setDog_internal_id(cursor.getInt(4));

            dogImages.add(tmp);
            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return dogImages;
    }

    synchronized public DogImage getDogFrontImageById(int id) {
        DogImage tmp = new DogImage();
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (DogImage.DogImageEntry.TABLE_NAME, null, DogImage.DogImageEntry.DOG_INTERNAL_ID + " = " + id + " and " + DogImage.DogImageEntry.TYPE + " = " + 1, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            tmp.setId(cursor.getInt(0));
            tmp.setKeyImage(cursor.getBlob(1));
            tmp.setType(cursor.getInt(2));
            tmp.setIsSubmit(cursor.getInt(3));
            tmp.setDog_internal_id(cursor.getInt(4));

            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return tmp;
    }

    synchronized public DogImage getDogSideImageById(int id) {
        DogImage tmp = new DogImage();
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (DogImage.DogImageEntry.TABLE_NAME, null, DogImage.DogImageEntry.DOG_INTERNAL_ID + " = " + id + " and " + DogImage.DogImageEntry.TYPE + " = " + 2, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {

            tmp.setId(cursor.getInt(0));
            tmp.setKeyImage(cursor.getBlob(1));
            tmp.setType(cursor.getInt(2));
            tmp.setIsSubmit(cursor.getInt(3));
            tmp.setDog_internal_id(cursor.getInt(4));

            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return tmp;
    }

    //    Dog
    synchronized public long addDog(Dog dog) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Dog.DogEntry.DOG_ID, dog.getDogID());
        values.put(Dog.DogEntry.DOG_TYPE, dog.getDogType());
        values.put(Dog.DogEntry.GENDER, dog.getGender());
        values.put(Dog.DogEntry.COLOR, dog.getColor());
        values.put(Dog.DogEntry.BREED, dog.getBreed());
        values.put(Dog.DogEntry.NAME, dog.getName());
        values.put(Dog.DogEntry.AGE, dog.getAge());
        values.put(Dog.DogEntry.AGE_RANGE, dog.getAgeRange());
        values.put(Dog.DogEntry.ADDRESS, dog.getAddress());
        values.put(Dog.DogEntry.SUBDISTRICT, dog.getSubdistrict());
        values.put(Dog.DogEntry.DISTRICT, dog.getDistrict());
        values.put(Dog.DogEntry.PROVINCE, dog.getProvince());
        values.put(Dog.DogEntry.LATITUDE, dog.getLatitude());
        values.put(Dog.DogEntry.LONGITUDE, dog.getLongitude());
        values.put(Dog.DogEntry.IS_SUBMIT, 0);

        long index = sqLiteDatabase.insert(Dog.DogEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        return index;
    }

    synchronized public Dog getDogById(int id) {
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
            tmp.setDogType(cursor.getString(2));
            tmp.setGender(cursor.getString(3));
            tmp.setColor(cursor.getString(4));
            tmp.setBreed(cursor.getString(5));
            tmp.setName(cursor.getString(6));
            tmp.setAge(cursor.getInt(7));
            tmp.setAgeRange(cursor.getString(8));
            tmp.setAddress(cursor.getString(9));
            tmp.setSubdistrict(cursor.getString(10));
            tmp.setDistrict(cursor.getString(11));
            tmp.setProvince(cursor.getString(12));
            tmp.setLatitude(cursor.getDouble(13));
            tmp.setLongitude(cursor.getDouble(14));
            tmp.setIsSubmit(cursor.getInt(15));
            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return tmp;
    }

    synchronized public List<Dog> getDog() {

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
            tmp.setDogType(cursor.getString(2));
            tmp.setGender(cursor.getString(3));
            tmp.setColor(cursor.getString(4));
            tmp.setBreed(cursor.getString(5));
            tmp.setName(cursor.getString(6));
            tmp.setAge(cursor.getInt(7));
            tmp.setAgeRange(cursor.getString(8));
            tmp.setAddress(cursor.getString(9));
            tmp.setSubdistrict(cursor.getString(10));
            tmp.setDistrict(cursor.getString(11));
            tmp.setProvince(cursor.getString(12));
            tmp.setLatitude(cursor.getDouble(13));
            tmp.setLongitude(cursor.getDouble(14));
            tmp.setIsSubmit(cursor.getInt(15));
            dogs.add(tmp);
            cursor.moveToNext();
        }
        sqLiteDatabase.close();
        return dogs;
    }

    synchronized public void updateDog(Dog dog) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Dog.DogEntry.ID, dog.getId());
        values.put(Dog.DogEntry.DOG_ID, dog.getDogID());
        values.put(Dog.DogEntry.DOG_TYPE, dog.getDogType());
        values.put(Dog.DogEntry.GENDER, dog.getGender());
        values.put(Dog.DogEntry.COLOR, dog.getColor());
        values.put(Dog.DogEntry.BREED, dog.getBreed());
        values.put(Dog.DogEntry.NAME, dog.getName());
        values.put(Dog.DogEntry.AGE, dog.getAge());
        values.put(Dog.DogEntry.AGE_RANGE, dog.getAgeRange());
        values.put(Dog.DogEntry.ADDRESS, dog.getAddress());
        values.put(Dog.DogEntry.SUBDISTRICT, dog.getSubdistrict());
        values.put(Dog.DogEntry.DISTRICT, dog.getDistrict());
        values.put(Dog.DogEntry.PROVINCE, dog.getProvince());
        values.put(Dog.DogEntry.LATITUDE, dog.getLatitude());
        values.put(Dog.DogEntry.LONGITUDE, dog.getLongitude());
        values.put(Dog.DogEntry.IS_SUBMIT, dog.getIsSubmit());

        int row = sqLiteDatabase.update(Dog.DogEntry.TABLE_NAME,
                values,
                Dog.DogEntry.ID + " = ? ",
                new String[]{String.valueOf(dog.getId())});

        sqLiteDatabase.close();
    }

    synchronized public void deleteDog(String id) {

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Dog.DogEntry.TABLE_NAME, Dog.DogEntry.ID + " = " + id, null);
        sqLiteDatabase.close();
    }

    //    Dog Information
    synchronized public long addDogInformation(DogInformation dogInformation) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DogInformation.DogInformationEntry.INTERNAL_DOG_ID, dogInformation.getDogID());
        values.put(DogInformation.DogInformationEntry.DOG_STATUS, dogInformation.getDogStatus());
        values.put(DogInformation.DogInformationEntry.PREGNANT, dogInformation.getPregnant());
        values.put(DogInformation.DogInformationEntry.CHILD_NUMBER, dogInformation.getChildNumber());
        values.put(DogInformation.DogInformationEntry.DEATH_REMARK, dogInformation.getDeathRemark());
        values.put(DogInformation.DogInformationEntry.MISSING_DATE, dogInformation.getMissingDate());
        values.put(DogInformation.DogInformationEntry.STERILIZED, dogInformation.getSterilized());
        values.put(DogInformation.DogInformationEntry.STERILIZED_DATE, dogInformation.getSterilizedDate());
        values.put(DogInformation.DogInformationEntry.AGE, dogInformation.getAge());
        values.put(DogInformation.DogInformationEntry.AGE_RANGE, dogInformation.getAgeRange());
        values.put(DogInformation.DogInformationEntry.IS_SUBMIT, dogInformation.getIsSubmit());
        long index = sqLiteDatabase.insert(DogInformation.DogInformationEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();
        return index;
    }

    synchronized public DogInformation getLastestDogInformationByDogID(int dogID) {
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
            dogInformation.setDogStatus(cursor.getString(2));
            dogInformation.setPregnant(cursor.getInt(3));
            dogInformation.setChildNumber(cursor.getInt(4));
            dogInformation.setDeathRemark(cursor.getString(5));
            dogInformation.setMissingDate(cursor.getString(6));
            dogInformation.setSterilized(cursor.getInt(7));
            dogInformation.setSterilizedDate(cursor.getString(8));
            dogInformation.setAge(cursor.getInt(9));
            dogInformation.setAgeRange(cursor.getString(10));
            dogInformation.setIsSubmit(cursor.getInt(11));

            cursor.moveToNext();
        }

        sqLiteDatabase.close();
        return dogInformation;
    }

    synchronized public List<DogInformation> getAllDogInformationByDogID(int dogID) {
        List<DogInformation> output = new ArrayList<>();
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
            DogInformation dogInformation = new DogInformation();
            dogInformation.setId(cursor.getInt(0));
            dogInformation.setDogID(cursor.getInt(1));
            dogInformation.setDogStatus(cursor.getString(2));
            dogInformation.setPregnant(cursor.getInt(3));
            dogInformation.setChildNumber(cursor.getInt(4));
            dogInformation.setDeathRemark(cursor.getString(5));
            dogInformation.setMissingDate(cursor.getString(6));
            dogInformation.setSterilized(cursor.getInt(7));
            dogInformation.setSterilizedDate(cursor.getString(8));
            dogInformation.setAge(cursor.getInt(9));
            dogInformation.setAgeRange(cursor.getString(10));
            dogInformation.setIsSubmit(cursor.getInt(11));
            output.add(dogInformation);
            cursor.moveToNext();
        }
        sqLiteDatabase.close();
        return output;
    }

    synchronized public void updateDogInfo(DogInformation dogInformation) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogInformation.DogInformationEntry.INTERNAL_DOG_ID, dogInformation.getDogID());
        values.put(DogInformation.DogInformationEntry.DOG_STATUS, dogInformation.getDogStatus());
        values.put(DogInformation.DogInformationEntry.PREGNANT, dogInformation.getPregnant());
        values.put(DogInformation.DogInformationEntry.CHILD_NUMBER, dogInformation.getChildNumber());
        values.put(DogInformation.DogInformationEntry.DEATH_REMARK, dogInformation.getDeathRemark());
        values.put(DogInformation.DogInformationEntry.MISSING_DATE, dogInformation.getMissingDate());
        values.put(DogInformation.DogInformationEntry.STERILIZED, dogInformation.getSterilized());
        values.put(DogInformation.DogInformationEntry.STERILIZED_DATE, dogInformation.getSterilizedDate());
        values.put(DogInformation.DogInformationEntry.AGE, dogInformation.getAge());
        values.put(DogInformation.DogInformationEntry.AGE_RANGE, dogInformation.getAgeRange());
        values.put(DogInformation.DogInformationEntry.IS_SUBMIT, dogInformation.getIsSubmit());

        int row = sqLiteDatabase.update(DogInformation.DogInformationEntry.TABLE_NAME,
                values,
                DogInformation.DogInformationEntry.ID + " = ? ",
                new String[]{String.valueOf(dogInformation.getId())});

        sqLiteDatabase.close();
    }

    synchronized public void deleteDogInfo(String id) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DogInformation.DogInformationEntry.TABLE_NAME, DogInformation.DogInformationEntry.ID + " = " + id, null);
        sqLiteDatabase.close();
    }

    //    AddDomestic4
    synchronized public List<DogVaccine> getRabiesVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies"};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccineName = ? and dogInternalID is null", whereArgs, null, null, null);

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

    synchronized public List<DogVaccine> getOtherVaccineList() {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies"};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccineName != ? and dogInternalID is null", whereArgs, null, null, null);

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

    synchronized public List<DogVaccine> getRabiesVaccineListById(int dogID) {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();
        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies", String.valueOf(dogID)};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccineName = ? and dogInternalID = ?", whereArgs, null, null, null);

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

    synchronized public List<DogVaccine> getOtherVaccineListById(int id) {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();

        sqLiteDatabase = this.getWritableDatabase();
        String[] whereArgs = new String[]{"Rabies", String.valueOf(id)};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "vaccineName != ? and dogInternalID = ?", whereArgs, null, null, null);

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

    synchronized public List<DogVaccine> getTwoLatestVaccines(int dogID) {
        List<DogVaccine> vaccines = new ArrayList<DogVaccine>();
        sqLiteDatabase = this.getWritableDatabase();
        int i = 0;
        String[] whereArgs = new String[]{String.valueOf(dogID)};

        Cursor cursor = sqLiteDatabase.query
                (DogVaccine.DogVaccineEntry.TABLE_NAME, null, "dogInternalID = ?", whereArgs, null, null, DogVaccine.DogVaccineEntry.ID + " DESC");

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


    synchronized public void addVaccine(DogVaccine vaccine) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.DogVaccineEntry.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_DATE, vaccine.getDate());

        if (vaccine.getDogID() != 0) {
            values.put(DogVaccine.DogVaccineEntry.DOG_INTERNAL_ID, vaccine.getDogID());
        }

        sqLiteDatabase.insert(DogVaccine.DogVaccineEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();
    }

    synchronized public void updateVaccine(DogVaccine vaccine) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DogVaccine.DogVaccineEntry.ID, vaccine.getId());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_NAME, vaccine.getName());
        values.put(DogVaccine.DogVaccineEntry.VACCINE_DATE, vaccine.getDate());
        values.put(DogVaccine.DogVaccineEntry.DOG_INTERNAL_ID, vaccine.getDogID());
        values.put(DogVaccine.DogVaccineEntry.IS_SUBMIT, vaccine.getIsSubmit());

        int row = sqLiteDatabase.update(DogVaccine.DogVaccineEntry.TABLE_NAME,
                values,
                DogVaccine.DogVaccineEntry.ID + " = ? ",
                new String[]{String.valueOf(vaccine.getId())});

        sqLiteDatabase.close();
    }

    synchronized public void updateVaccineWhileAddingDog(DogVaccine vaccine) {

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

    synchronized public void deleteVaccine(String id) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DogVaccine.DogVaccineEntry.TABLE_NAME, DogVaccine.DogVaccineEntry.ID + " = " + id, null);
        sqLiteDatabase.close();
    }

    synchronized public void deleteNull() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(DogVaccine.DogVaccineEntry.TABLE_NAME, DogVaccine.DogVaccineEntry.DOG_INTERNAL_ID + " is null ", null);
        sqLiteDatabase.close();
    }

}
