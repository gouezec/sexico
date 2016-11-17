package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;

public class H2InMemoryDatabase extends SQLDatabase {

	private H2InMemoryDatabase() throws DatabaseException {
		super();
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:mem:db1", "sa", "");
			init(conn);
		} catch (SQLException e) {
			// ignore. Tables already existing
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	public static Database getInstance() {
		if (_instance != null) {
			return _instance;
		} else {
			try {
				_instance = new H2InMemoryDatabase();
				return _instance;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	

	private static Database _instance = null;
}
