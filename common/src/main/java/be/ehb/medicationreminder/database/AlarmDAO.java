package be.ehb.medicationreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import be.ehb.medicationreminder.core.AbstractDatabaseObject;
import be.ehb.medicationreminder.core.Alarm;
import be.ehb.medicationreminder.core.Medication;


/**
 * Created by davy.van.belle on 28/05/2015.
 */
public class AlarmDAO extends AbstractDAO {

    private final String TABLE_NAME = MedRemSQLiteHelper.TABLE_ALARMS;

    public AlarmDAO (Context context){
        super(context);
    }

    @Override
    public Alarm insert(AbstractDatabaseObject object) {
        Alarm alarm = (Alarm) object;
        ContentValues values = new ContentValues();
        values.put(MedRemSQLiteHelper.COLUMN_ID, alarm.getID());
        values.put(MedRemSQLiteHelper.COLUMN_TIME, alarm.getTime());
        values.put(MedRemSQLiteHelper.COLUMN_DAYS, alarm.printDays());
        values.put(MedRemSQLiteHelper.COLUMN_MED_ID,alarm.getParentID());

        this.open();
        int insertID = (int) db.insert(this.TABLE_NAME, null, values);
        alarm = (Alarm) this.getByID(insertID);
        this.close();
        return alarm;
    }

    @Override
    public ArrayList<Alarm> getAll() {
        return super.getAll();
    }

    @Override
    public Alarm getByID(int id) {
        return (Alarm) super.getByID(id);
    }

    @Override
    public int update(AbstractDatabaseObject object) {
        Alarm alarm = (Alarm) object;
        ContentValues values = new ContentValues();
        values.put(MedRemSQLiteHelper.COLUMN_ID, alarm.getID());
        values.put(MedRemSQLiteHelper.COLUMN_TIME, alarm.getTime());
        values.put(MedRemSQLiteHelper.COLUMN_DAYS, alarm.printDays());
        values.put(MedRemSQLiteHelper.COLUMN_MED_ID,alarm.getParentID());

        this.open();
        int ret = db.update(this.TABLE_NAME, values, MedRemSQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(alarm.getID()) });
        this.close();
        return ret;
    }

    public ArrayList<Alarm> getAlarmsByMed(Medication med)
    {
        ArrayList<Alarm> objects = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + this.getTableName() + " WHERE " + MedRemSQLiteHelper.COLUMN_MED_ID + " ='" + med.getID() + "'";
        this.open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                objects.add(new Alarm(cursor.getInt(0), cursor.getString(1), cursor.getString(2),med.getID()));
            } while (cursor.moveToNext());
        }
        this.close();
        return objects;
    }


    @Override
    protected Alarm cursorToObject(Cursor cursor) {

        return new Alarm(cursor.getInt(0), cursor.getString(1), cursor.getString(2),cursor.getInt(3));
    }

    @Override
    protected String getTableName() {
        return this.TABLE_NAME;
    }
}
