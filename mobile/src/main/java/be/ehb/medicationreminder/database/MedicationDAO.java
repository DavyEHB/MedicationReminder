package be.ehb.medicationreminder.database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import be.ehb.medicationreminder.core.Medication;

/**
 * Created by davy.van.belle on 27/05/2015.
 */
public class MedicationDAO extends AbstractDAO {

    private String[] allColumns = { MedRemSQLiteHelper.COLUMN_ID,
            MedRemSQLiteHelper.COLUMN_NAME,MedRemSQLiteHelper.COLUMN_PICTURE };


    public MedicationDAO(Context context) {
        super(context);
    }

    protected Medication cursorToObject(Cursor cursor) {
        Medication med = new Medication(cursor.getString(1));
        med.setID(cursor.getInt(0));
        med.setPicture(cursor.getString(2));
        return med;
    }

    @Override
    public ArrayList<Medication> getAll() {
        ArrayList meds = new ArrayList();

          Cursor cursor = db.query(MedRemSQLiteHelper.TABLE_MEDICATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medication comment = cursorToObject(cursor);
            meds.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return meds;

    }

    @Override
    public ArrayList<Medication> getAllByID(int id) {
        ArrayList meds = new ArrayList();

        Cursor cursor = db.query(MedRemSQLiteHelper.TABLE_MEDICATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medication comment = cursorToObject(cursor);
            meds.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return meds;
    }
}
