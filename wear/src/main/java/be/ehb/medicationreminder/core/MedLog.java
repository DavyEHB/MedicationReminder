package be.ehb.medicationreminder.core;

import java.util.ArrayList;
import java.util.Iterator;

public class MedLog {

	private static MedLog instance = null;
	private final ArrayList<String> arLogList = new ArrayList<>();

	private MedLog() {
		arLogList.clear();
	}

	public static MedLog getInstance() {
		if(instance == null){
            synchronized (MedLog.class) {
                if(instance == null){
                    instance = new MedLog();
                }
            }
        }
        return instance;
	}

	/**
	 * 
	 * @param log
	 */
	public void addLog(String log) {
		arLogList.add(log);
	}

	public ArrayList<String> getLogList() {
		return arLogList;
	}

	public String toString() {
		Iterator<String> LogIter = arLogList.iterator();
		String TempStr = "";
		while (LogIter.hasNext()) {
			TempStr += LogIter.next() + "\n";			
		}
		return TempStr;
	}
}