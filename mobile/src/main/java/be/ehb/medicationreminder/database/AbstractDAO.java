package be.ehb.medicationreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by davy.van.belle on 27/05/2015.
 */
public abstract class AbstractDAO {

    private SQLiteDatabase db;
    private MedRemSQLiteHelper dbHelper;

    public AbstractDAO(Context context){
        dbHelper = new MedRemSQLiteHelper(context);
    }

    public void close(){
        db.close();
    };

    public void open(){
        db = dbHelper.getWritableDatabase();
    }


    public abstract ArrayList getAll();
    public abstract ArrayList getAllByID(int id);
}
