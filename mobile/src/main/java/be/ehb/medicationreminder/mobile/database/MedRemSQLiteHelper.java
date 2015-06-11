package be.ehb.medicationreminder.mobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by davy.van.belle on 27/05/2015.
 */
class MedRemSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MEDICATIONS = "medications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PICTURE = "picture";

    public static final String TABLE_ALARMS = "alarms";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DAYS = "days";
    public static final String COLUMN_MED_ID = "med_id";

    private static final String DATABASE_NAME = "MedRem.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String MEDICATION_TABLE_CREATE = "create table "
            + TABLE_MEDICATIONS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_PICTURE + " blob);";

    private static final String ALARM_TABLE_CREATE = "create table "
            + TABLE_ALARMS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TIME
            + " text, " + COLUMN_DAYS + " text, " + COLUMN_MED_ID + " integer);";

    public MedRemSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MEDICATION_TABLE_CREATE);
        database.execSQL(ALARM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MedRemSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }
}
