package main.dogappandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DogDB {
    private DogDB() {
    }

    public static class DogDBEntry implements BaseColumns {
        public static final String TABLE_NAME = "dog";
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

    }
}
