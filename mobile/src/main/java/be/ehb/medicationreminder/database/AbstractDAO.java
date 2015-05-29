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
    protected Context context;

    public AbstractDAO(Context context){
        this.context = context;
        dbHelper = new MedRemSQLiteHelper(context);
    }

    public void close(){
        db.close();
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void delete(AbstractDatabaseObject object) {
        int id = object.getID();
        this.open();
        db.delete(this.getTableName(), MedRemSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public int count() {
        String selectQuery = "SELECT  * FROM " + this.getTableName();

        this.open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        this.close();
        return cursor.getCount();
    }

    public ArrayList getAll() {
        ArrayList<AbstractDatabaseObject> objects = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + this.getTableName();
        this.open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                objects.add(this.cursorToObject(cursor));
            } while (cursor.moveToNext());
        }
        return objects;
    }

    public AbstractDatabaseObject getByID(int id) {
        AbstractDatabaseObject object = null;
        String selectQuery = "SELECT  * FROM " + this.getTableName() + " WHERE " + MedRemSQLiteHelper.COLUMN_ID + " ='" + id + "'";
        this.open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            object = this.cursorToObject(cursor);
        }
        return object;
    }

    public abstract AbstractDatabaseObject insert(AbstractDatabaseObject object);
    public abstract int update(AbstractDatabaseObject object);

    protected abstract AbstractDatabaseObject cursorToObject(Cursor cursor);
    protected abstract String getTableName();



}
