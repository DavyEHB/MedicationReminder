package be.ehb.medicationreminder.core;

/**
 * Created by davy.van.belle on 28/05/2015.
 */
public abstract class AbstractDatabaseObject {

    private int id;

    public void setID(int ID){
        this.id = ID;
    }

    public int getID(){
        return this.id;
    }

    public AbstractDatabaseObject(int id){
        this.id = id;
    }
}
