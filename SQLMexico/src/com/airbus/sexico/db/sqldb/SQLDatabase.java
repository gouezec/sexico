package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;

public abstract class SQLDatabase implements Database {


	private int batchSize;

	private Connection conn = null;

	private HashMap<Long, SQLDatabaseContentHandler> handlers;

	@Override
	public void updateIndex() throws DatabaseException {
		createIndexes();
	}


	protected void initialize(Connection conn) throws DatabaseException, SQLException {
		this.conn = conn;
		conn.setAutoCommit(false);
		handlers = new HashMap<Long, SQLDatabaseContentHandler>();
	}

	public void build() throws DatabaseException {
		try {
			dropBase();
		} catch (DatabaseException e) {
			// ignore. Tables don't exist
		}
		createTables();

		batchSize = 0;
	}

	public void connect() throws DatabaseException {
	}

	public void finalize() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airbus.sexico.db.Database#commitBase()
	 */
	@Override
	public void commitBase() throws DatabaseException {
		try {
			//			_insertPortStatement.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	private void createTables() {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(CREATE_PORT_TABLE);
			stmt.executeUpdate(CREATE_CONNECTION_TABLE);
		} catch (SQLException e) {
			// ignore, tables already exists
		}
	}

	private void createIndexes() {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(CREATE_CONNECTION_INDEX);
			stmt.executeUpdate(CREATE_CONNECTION_INDEX2);
			stmt.executeUpdate(CREATE_PORT_INDEX);
			stmt.executeUpdate(CREATE_PORT_INDEX2);
		} catch (SQLException e) {
			// ignore, tables already exists
		}
	}

	@Override
	public void dropBase() throws DatabaseException {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(DROP_PORT_TABLE);
			stmt.executeUpdate(DROP_CONNECTION_TABLE);
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	@Override
	public DatabaseContentHandler getContentHandler() throws DatabaseException {
		try {
			Long threadId = new Long(Thread.currentThread().getId());
			SQLDatabaseContentHandler handler = handlers.get(threadId);
			if (handler == null) {
				handler = new SQLDatabaseContentHandler(conn);
				handlers.put(threadId, handler);
			}
			return handler;
		}
		catch(SQLException e ){
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}


	private final static String CREATE_PORT_TABLE = "CREATE TABLE PORTS " + "(modelname VARCHAR(100), "
			+ " portname VARCHAR(100), " + " type VARCHAR(30), " + " description VARCHAR(255), "
			+ " direction CHAR(1), " + " unit VARCHAR(30), " + " MICDconsistency BOOLEAN, "
			+ " PRIMARY KEY ( modelname, portname, direction ))";

	private final static String CREATE_CONNECTION_TABLE = "CREATE TABLE CONNECTIONS " + "(connectionName VARCHAR(100), "
			+ " modelName VARCHAR(100), " + " portname VARCHAR(100))";

	private final static String CREATE_CONNECTION_INDEX = "CREATE INDEX CONNECTION_INDEX ON CONNECTIONS (portname)";

	private final static String CREATE_CONNECTION_INDEX2 = "CREATE INDEX CONNECTION_INDEX2 ON CONNECTIONS (modelname)";

	private final static String CREATE_PORT_INDEX = "CREATE INDEX PORT_INDEX ON PORTS (portname)";

	private final static String CREATE_PORT_INDEX2 = "CREATE INDEX PORT_INDEX2 ON PORTS (modelname)";

	private final static String DROP_PORT_TABLE = "DROP TABLE PORTS";

	private final static String DROP_CONNECTION_TABLE = "DROP TABLE CONNECTIONS";
}
