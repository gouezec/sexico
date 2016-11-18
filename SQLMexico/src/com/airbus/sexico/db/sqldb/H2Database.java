package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.airbus.sexico.db.DatabaseException;

public class H2Database extends SQLDatabase {

	private final static String H2_DB_URL = "jdbc:h2:file:c:/Users/to28077/Desktop/";
	private final static String OPTIONS = ";LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0";
	
	public H2Database(String name) throws DatabaseException {
		super();
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(H2_DB_URL+name+".h2", "sa", "");
			init(conn);
		} catch (SQLException e) {
			// ignore. Tables already existing
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOW_DRIVER);
		}
	}
}
