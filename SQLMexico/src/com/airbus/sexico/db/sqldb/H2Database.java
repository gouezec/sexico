package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;

public class H2Database extends SQLDatabase {

//	private final static String H2_DB_URL = "jdbc:h2:file:c:/Users/to28077/Desktop/sexico.h2;LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0";
	private final static String H2_DB_URL = "jdbc:h2:file:d:/sexico.h2";
	
	private H2Database() throws DatabaseException {
		super();
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(H2_DB_URL, "sa", "");
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
				_instance = new H2Database();
				return _instance;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	

	private static Database _instance = null;
}
