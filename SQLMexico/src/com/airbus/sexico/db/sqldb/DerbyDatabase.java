package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;

public class DerbyDatabase extends SQLDatabase {

	private DerbyDatabase() throws DatabaseException {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			Connection conn = DriverManager.getConnection("jdbc:derby:sexico.derby;create=true");
			init(conn);
		} catch (SQLException e) {
			// ignore. Tables already existing
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOW_DRIVER);
		}
	}

	public static Database getInstance() {
		if (_instance != null) {
			return _instance;
		} else {
			try {
				_instance = new DerbyDatabase();
				return _instance;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	private static Database _instance = null;
}
