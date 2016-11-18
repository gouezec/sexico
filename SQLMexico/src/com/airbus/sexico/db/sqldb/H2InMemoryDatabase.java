package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.airbus.sexico.db.DatabaseException;

public class H2InMemoryDatabase extends SQLDatabase {

	public H2InMemoryDatabase(String name) throws DatabaseException {
		super();
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:mem:"+name, "sa", "");
			init(conn);
		} catch (SQLException e) {
			// ignore. Tables already existing
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}
}
