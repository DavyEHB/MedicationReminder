package be.ehb.medicationreminder.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import be.ehb.medicationreminder.core.Medication;

/**
 * Created by davy.van.belle on 27/05/2015.
 */
public abstract class AbstractDAO {

    protected SQLiteDatabase db;
    private MedRemSQLiteHelper dbHelper;

    public AbstractDAO(Context context){
        dbHelper = new MedRemSQLiteHelper(context);
    }

    public void close(){
        db.close();
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }


    public abstract ArrayList getAll();
    public abstract ArrayList getAllByID(int id);

    protected abstract Object cursorToObject(Cursor cursor);

}
