package com.airbus.sexico.db.mapdb;

import java.util.HashMap;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Direction;

public class MapDatabase implements Database {

	private HashMap<String, Port> _ports;
	
	private HashMap<String, PortConnection> _connections;

	private MapDatabase() {
		initBase();
	}

	public static Database getInstance() {
		if (_instance != null) {
			return _instance;
		} else {
			_instance = new MapDatabase();
			return _instance;
		}
	}

	public void finalize() {
		// NOP with a map
	}

	@Override
	public void insertPort(String modelName, String portName, String typeName, String description, Direction direction,
			boolean micdConsistency) throws DatabaseException {
		_ports.put(modelName + portName, new Port(modelName, portName, typeName, description, direction, micdConsistency));
	}

	@Override
	public void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException {
		_connections.put(modelName + portName, new PortConnection(modelName, portName, connectionName));
	}


	@Override
	public void commitBase() throws DatabaseException {
		// NOP with a map
	}

	
	@Override
	public void dropBase() throws DatabaseException {
		initBase();
	}
	
	private void  initBase() {
		_ports = new HashMap<String, Port>();
		_connections = new HashMap<String, PortConnection>();		
	}


	private static Database _instance = null;
}
