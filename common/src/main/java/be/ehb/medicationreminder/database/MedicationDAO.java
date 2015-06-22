package be.ehb.medicationreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import be.ehb.medicationreminder.core.AbstractDatabaseObject;
import be.ehb.medicationreminder.core.Medication;


/**
 * Created by davy.van.belle on 27/05/2015.
 */
public class MedicationDAO extends AbstractDAO {

    private final String TABLE_NAME = MedRemSQLiteHelper.TABLE_MEDICATIONS;

    public MedicationDAO(Context context) {
        super(context);
    }

    protected Medication cursorToObject(Cursor cursor) {
        AlarmDAO alDAO = new AlarmDAO(this.context);
        Medication med = new Medication(Integer.parseInt(cursor.getString(0)), cursor.getString(1), DbUtility.getImage(cursor.getBlob(2)));

        return med;
    }

    @Override
    protected String getTableName() {
        return this.TABLE_NAME;
    }

    @Override
    public int update(AbstractDatabaseObject medication) {
        Medication med = (Medication) medication;
        ContentValues values = new ContentValues();
        values.put(MedRemSQLiteHelper.COLUMN_ID, med.getID());
        values.put(MedRemSQLiteHelper.COLUMN_NAME, med.getName());
        values.put(MedRemSQLiteHelper.COLUMN_PICTURE,DbUtility.getBytes(med.getPicture()));

        this.open();
        int ret = db.update(this.TABLE_NAME, values, MedRemSQLiteHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(med.getID()) });
        this.close();
        return ret;
    }



    @Override
    public Medication insert(AbstractDatabaseObject medication) {
        Medication med = (Medication) medication;

        ContentValues values = new ContentValues();

        //values.put(MedRemSQLiteHelper.COLUMN_ID, med.getID());
        values.put(MedRemSQLiteHelper.COLUMN_NAME, med.getName());
        values.put(MedRemSQLiteHelper.COLUMN_PICTURE,DbUtility.getBytes(med.getPicture()));

        this.open();
        long insertID = db.insert(this.TABLE_NAME, null, values);
        med = (Medication) this.getByID((int)insertID);
        this.close();
        return med;
    }

    @Override
    public ArrayList<Medication> getAll() {
        return super.getAll();
    }

    @Override
    public Medication getByID(int id) {
        return (Medication) super.getByID(id);
    }
}