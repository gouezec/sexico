package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.airbus.sexico.db.DatabaseException;

public class DerbyDatabase extends SQLDatabase {

	public DerbyDatabase(String name) throws DatabaseException {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			Connection conn = DriverManager.getConnection("jdbc:derby:"+name+".derby;create=true");
			init(conn);
		} catch (SQLException e) {
			// ignore. Tables already existing
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOW_DRIVER);
		}
	}
}
