package com.airbus.sexico.db.mapdb;

import java.util.HashMap;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Direction;
import com.airbus.sexico.db.Port;

public class MapDatabase implements Database {

	@Override
	public void updateIndex() throws DatabaseException {
		// nop
		
	}

	private HashMap<String, Port> _ports;
	
	private HashMap<String, PortConnection> _connections;

	public MapDatabase(String name) {
		initBase();
	}

	public void finalize() {
		// NOP with a map
	}

	@Override
	public Port[] getAllPorts() throws DatabaseException {
		return _ports.values().toArray(new Port[1]);
	}

	@Override
	public int getPortLength() throws DatabaseException {
		return _ports.size();
	}

	@Override
	public void insertPort(Port port) throws DatabaseException {
		_ports.put(port.getModelName() + port.getPortName(), new Port(port.getModelName(), port.getPortName(), port.getDescription(), port.getTypeName(), port.getUnit(), port.getDirection(), port.isMicdConsistency()));
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
}
