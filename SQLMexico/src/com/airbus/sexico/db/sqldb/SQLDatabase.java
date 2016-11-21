package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Direction;
import com.airbus.sexico.db.Port;

public abstract class SQLDatabase implements Database {

	
	private final static String PRIMARY_KEY_VIOLATION_CODE = "23505";
	
	private PreparedStatement _insertPortStatement;
	private PreparedStatement _insertConnectionStatement;

	@Override
	public void updateIndex() throws DatabaseException {
		createIndexes();	
	}

	protected void init(Connection conn) throws DatabaseException {
		try {
			this.conn = conn;
			conn.setAutoCommit(false);
			try {
				dropBase();
			}
			catch (DatabaseException e) {
				// ignore. Tables don't exist
			}
			createTables();
			_insertPortStatement = conn.prepareStatement(INSERT_PORT);
			_insertConnectionStatement = conn.prepareStatement(INSERT_CONNECTION);
		} catch (SQLException e) {
			// ignore. Tables already existing
			e.printStackTrace();
		}
	}

	public void finalize() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.airbus.sexico.db.Database#insertPort(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void insertPort(Port	port) throws DatabaseException {
		try {
			_insertPortStatement.setString(1, port.getModelName());
			_insertPortStatement.setString(2, port.getPortName());
			_insertPortStatement.setString(3, port.getTypeName());
			_insertPortStatement.setString(4, port.getDescription().substring(0,Math.min(port.getDescription().length(), 254)));
			_insertPortStatement.setString(5, (port.getDirection() == Direction.IN ? "I" : "O"));
			_insertPortStatement.setString(6, port.getUnit());
			_insertPortStatement.setBoolean(7, port.isMicdConsistency());
			_insertPortStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e, resolveReason(e));
		}
	}

	/* (non-Javadoc)
	 * @see com.airbus.sexico.db.Database#insertConnection(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException {
		try {
			_insertConnectionStatement.setString(1, connectionName);
			_insertConnectionStatement.setString(2, modelName);
			_insertConnectionStatement.setString(3, portName);
			_insertConnectionStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e, resolveReason(e));
		}
	}

	/* (non-Javadoc)
	 * @see com.airbus.sexico.db.Database#commitBase()
	 */
	@Override
	public void commitBase() throws DatabaseException {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	
	private int resolveReason(SQLException e) {
		if(e.getSQLState().equals(PRIMARY_KEY_VIOLATION_CODE)) {
			return DatabaseException.REASON_UNIQUE_ID_VIOLATION;
		} else {
			return DatabaseException.REASON_UNKNOWN;
		}
	}
	
	private void createTables() {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(CREATE_PORT_TABLE);
			stmt.executeUpdate(CREATE_CONNECTION_TABLE);
		} catch(SQLException e) {
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
		} catch(SQLException e) {
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


	private Connection conn = null;

	final static String INSERT_PORT = "INSERT INTO PORTS VALUES (?,?,?,?,?,?,?)";

	final static String INSERT_CONNECTION = "INSERT INTO CONNECTIONS VALUES (?,?,?)";

	final static String CREATE_PORT_TABLE = "CREATE TABLE PORTS " + "(modelname VARCHAR(100), "
			+ " portname VARCHAR(100), " + " type VARCHAR(30), " + " description VARCHAR(255), "
 			+ " direction CHAR(1), "
 			+ " unit VARCHAR(30), "
 			+ " MICDconsistency BOOLEAN, "
			+ " PRIMARY KEY ( modelname, portname ))";

	final static String CREATE_CONNECTION_TABLE = "CREATE TABLE CONNECTIONS " + "(connectionName VARCHAR(100), "
			+ " modelName VARCHAR(100), " + " portname VARCHAR(100))";

	final static String CREATE_CONNECTION_INDEX = "CREATE INDEX CONNECTION_INDEX ON CONNECTIONS (portname)";
	
	final static String CREATE_CONNECTION_INDEX2 = "CREATE INDEX CONNECTION_INDEX2 ON CONNECTIONS (modelname)";

	final static String CREATE_PORT_INDEX = "CREATE INDEX PORT_INDEX ON PORTS (portname)";
	
	final static String CREATE_PORT_INDEX2 = "CREATE INDEX PORT_INDEX2 ON PORTS (modelname)";

	final static String DROP_PORT_TABLE = "DROP TABLE PORTS";
	
	final static String DROP_CONNECTION_TABLE = "DROP TABLE CONNECTIONS";
}
