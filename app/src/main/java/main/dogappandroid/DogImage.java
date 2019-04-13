package main.dogappandroid;

import android.provider.BaseColumns;

public class DogImage {

    private String imagePath;
    private int id, type, dogInternalID, isSubmit;

    public DogImage() {

    }

    public static class DogImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "dog_image";
        public static final String ID = BaseColumns._ID;
        public static final String IMAGE_PATH = "imagePath";
        public static final String TYPE = "type";
        public static final String DOG_INTERNAL_ID = "dogInternalID";
        public static final String IS_SUBMIT = "isSubmit";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DogImageEntry.TABLE_NAME + " (" +
                    DogImageEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DogImageEntry.IMAGE_PATH + " TEXT," +
                    DogImageEntry.TYPE + " INTEGER," +
                    DogImageEntry.IS_SUBMIT + " INTEGER," +
                    DogImageEntry.DOG_INTERNAL_ID + " INTEGER) ";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DogImageEntry.TABLE_NAME;

    public int getIsSubmit() {
        return isSubmit;
    }

    public void setIsSubmit(int isSubmit) {
        this.isSubmit = isSubmit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public int getDogInternalId() {
        return dogInternalID;
    }

    public void setDogInternalId(int dogInternalID) {
        this.dogInternalID = dogInternalID;
    }
}
