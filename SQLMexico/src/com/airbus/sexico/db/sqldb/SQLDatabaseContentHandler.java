package com.airbus.sexico.db.sqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Direction;
import com.airbus.sexico.db.Port;

public class SQLDatabaseContentHandler implements DatabaseContentHandler {

	private Connection conn;
	private PreparedStatement insertPortStatement;
	private PreparedStatement insertConnectionStatement;
	
	public SQLDatabaseContentHandler(Connection conn) throws SQLException {
		this.conn = conn;
		insertPortStatement = conn.prepareStatement(INSERT_PORT);
		insertConnectionStatement = conn.prepareStatement(INSERT_CONNECTION);
	}
	
	


	@Override
	public Port[] getAllPorts() throws DatabaseException {
		try {
			List<Port> ports = new ArrayList<Port>();
			Statement stmt = conn.createStatement();
			stmt.executeQuery(SELECT_ALL_PORTS);
			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {
				Port port = new Port(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), Direction.IN, rs.getBoolean(7));
				port.setDirection(rs.getString(6).equals("I") ? Direction.IN : Direction.OUT);
				ports.add(port);
			}
			return ports.toArray(new Port[1]);
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}
	

	@Override
	public int getPortLength() throws DatabaseException {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(SELECT_COUNT_PORTS);
			ResultSet rs = stmt.getResultSet();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airbus.sexico.db.Database#insertPort(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void insertPort(Port port) throws DatabaseException {
		
		try {
			insertPortStatement.setString(1, port.getModelName());
			insertPortStatement.setString(2, port.getPortName());
			insertPortStatement.setString(3, port.getTypeName());
			insertPortStatement.setString(4,
					port.getDescription().substring(0, Math.min(port.getDescription().length(), 254)));
			insertPortStatement.setString(5, (port.getDirection() == Direction.IN ? "I" : "O"));
			insertPortStatement.setString(6, port.getUnit());
			insertPortStatement.setBoolean(7, port.isMicdConsistency());
			
			// Sans Batch
			insertPortStatement.executeUpdate();
			
			// Avec Batch
//			_insertPortStatement.addBatch();
//			batchSize++;
//			
//			if (batchSize == 100000)
//			{
//				batchSize = 0;
//				_insertPortStatement.executeBatch();
//			}
		} catch (SQLException e) {
			throw new DatabaseException(e, resolveReason(e));
		}
	}


	@Override
	public void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException {
		try {
			insertConnectionStatement.setString(1, connectionName);
			insertConnectionStatement.setString(2, modelName);
			insertConnectionStatement.setString(3, portName);
			insertConnectionStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e, resolveReason(e));
		}
	}

	
	public void queryRuleA() throws DatabaseException {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(RULE_A);
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	public void queryRuleSpace() throws DatabaseException {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(RULE_SPACE);
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}
	
	public int queryHomonimy1() throws DatabaseException {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(RULE_HOMONIMY_1);
			ResultSet rs = stmt.getResultSet();
			rs.last();
	        return rs.getRow();
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}

	public int queryHomonimy2() throws DatabaseException {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(RULE_HOMONIMY_2);
			ResultSet rs = stmt.getResultSet();
			rs.last();
	        return rs.getRow();
		} catch (SQLException e) {
			throw new DatabaseException(e, DatabaseException.REASON_UNKNOWN);
		}
	}
	
	
	private int resolveReason(SQLException e) {
		if (e.getSQLState().equals(PRIMARY_KEY_VIOLATION_CODE)) {
			return DatabaseException.REASON_UNIQUE_ID_VIOLATION;
		} else {
			return DatabaseException.REASON_UNKNOWN;
		}
	}
	
	private final static String PRIMARY_KEY_VIOLATION_CODE = "23505";
	
	private final static String SELECT_ALL_PORTS = "SELECT * from PORTS";

	private final static String SELECT_COUNT_PORTS = "SELECT count(*) from PORTS";

	private final static String RULE_HOMONIMY_1 = "SELECT p1.modelname, p1.portname, p1.direction, p2.modelname, p2.portname, p2.direction FROM ports p1, ports p2 WHERE p1.portname = p2.portname AND p1.direction = 'O' AND p2.direction = 'I'";

	private final static String RULE_HOMONIMY_2 = "SELECT p1.modelname, p1.portname, p1.direction, p2.modelname, p2.portname, p2.direction FROM ports p1 INNER JOIN ports p2 ON p1.portname = p2.portname AND p1.direction = 'O' AND  p2.direction = 'I'";

	private final static String RULE_SPACE = "SELECT modelname, portname, unit FROM ports where unit CONTAINS ' '";

	private final static String RULE_A = "SELECT modelname, portname, unit FROM ports where unit CONTAINS 'a'";

	private final static String INSERT_PORT = "INSERT INTO PORTS VALUES (?,?,?,?,?,?,?)";

	private final static String INSERT_CONNECTION = "INSERT INTO CONNECTIONS VALUES (?,?,?)";
}
