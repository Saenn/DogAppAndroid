package main.dogappandroid;

import android.provider.BaseColumns;

public class DogImage {

    private byte[] keyImage;
    private int id, type, dog_internal_id, isSubmit;

    public DogImage() {

    }

    public static class DogImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "dog_image";
        public static final String ID = BaseColumns._ID;
        public static final String KEY_IMAGE = "image_data";
        public static final String TYPE = "type";
        public static final String DOG_INTERNAL_ID = "dog_internal_id";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogImageEntry.TABLE_NAME + " (" +
                    DogImageEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogImageEntry.KEY_IMAGE + " BLOB," +
                    DogImageEntry.TYPE + " INTEGER," +
                    DogImageEntry.IS_SUBMIT + " INTEGER," +
                    DogImageEntry.DOG_INTERNAL_ID + " INTEGER) ";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogImageEntry.TABLE_NAME;


    // getter and setter //


    public int getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
    }

    public byte[] getKeyImage() {
        return keyImage;
    }

    public void setKeyImage(byte[] keyImage) {
        this.keyImage = keyImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDog_internal_id() {
        return dog_internal_id;
    }

    public void setDog_internal_id(int dog_internal_id) {
        this.dog_internal_id = dog_internal_id;
    }
}
