package com.airbus.sexico.db;

import com.airbus.sexico.db.mapdb.MapDatabase;
import com.airbus.sexico.db.sqldb.DerbyDatabase;
import com.airbus.sexico.db.sqldb.H2Database;
import com.airbus.sexico.db.sqldb.H2InMemoryDatabase;
import com.airbus.sexico.db.sqldb.SQLDatabase;

public class DatabaseFactory {

	public static DatabaseFactory getInstance() {
		if (_instance != null) {
			return _instance;
		} else {
			try {
				_instance = new DatabaseFactory();
				return _instance;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}


	private static DatabaseFactory _instance = null;


	public Database createH2Database(String name) throws DatabaseException {
		SQLDatabase db = new H2Database(name);
		db.build();
		return db;
	}

	public Database connectH2Database(String name) throws DatabaseException {
		SQLDatabase db = new H2Database(name);
		db.connect();
		return db;
	}
	
	public Database createH2InMemoryDatabase(String name) throws DatabaseException {
		SQLDatabase db =  new H2InMemoryDatabase(name);
		db.build();
		return db;
	}

	public Database createDerbyDatabase(String name) throws DatabaseException {
		SQLDatabase db = new DerbyDatabase(name);
		db.build();
		return db;
	}

	public Database createMapDatabase(String name) {
		return new MapDatabase(name);		
	}

}
