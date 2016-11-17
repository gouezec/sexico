package com.airbus.sexico.importation;

import java.io.File;

import com.airbus.sexico.db.Database;

public abstract class Importer {

	protected Database _db = null;

	private boolean _timeStamped;

	public boolean isTimeStamped() {
		return _timeStamped;
	}

	public void setTimeStamped(boolean timeStamped) {
		this._timeStamped = timeStamped;
	}

	public Importer(Database db) {
		_db = db;
		_timeStamped = false;
	}

	protected abstract long rawImportFile(File file) throws ImportException;
	
	public final long importFile(File file) throws ImportException {
		if (isTimeStamped()) {
			return elaspedTimeImportFile(file);
		} else {
			return rawImportFile(file);
		}
	}

	protected final long elaspedTimeImportFile(File file) throws ImportException {
		long statTime = System.currentTimeMillis();
		long nb = rawImportFile(file);
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - statTime;
		System.out.println(nb+" items from "+file.getName() + " imported in " + elapsedTime + "ms");
		return nb;
	}
}
