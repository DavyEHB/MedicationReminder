package be.ehb.medicationreminder.database;

import android.content.Context;

import java.util.ArrayList;

import be.ehb.medicationreminder.core.Medication;

/**
 * Created by davy.van.belle on 27/05/2015.
 */
public class MedicationDAO extends AbstractDAO {


    public MedicationDAO(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Medication> getAll() {
        ArrayList meds = new ArrayList();

        // Name of the columns we want to select
        String[] tableColumns = new String[] {"_id","todo"};

        // Query the database
        Cursor cursor = db.query("todos", tableColumns, null, null, null, null, null);
        cursor.moveToFirst();

        // Iterate the results
        while (!cursor.isAfterLast()) {
            Todo todo = new Todo();
            // Take values from the DB
            todo.setId(cursor.getInt(0));
            todo.setText(cursor.getString(1));

            // Add to the DB
            todoList.add(todo);

            // Move to the next result
            cursor.moveToNext();
        }

        return meds;

    }

    @Override
    public ArrayList<Medication> getAllByID(int id) {

    }
}
