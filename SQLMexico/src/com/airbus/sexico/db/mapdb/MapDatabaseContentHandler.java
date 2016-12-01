package com.airbus.sexico.db.mapdb;

import java.util.HashMap;

import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Port;

public class MapDatabaseContentHandler implements DatabaseContentHandler {

	private HashMap<String, Port> ports;
	
	private HashMap<String, PortConnection> connections;
	
	public MapDatabaseContentHandler(HashMap<String, Port> ports, HashMap<String, PortConnection> connections) {
		super();
		this.ports = ports;
		this.connections = connections;
	}

	@Override
	public Port[] getAllPorts() throws DatabaseException {
		return ports.values().toArray(new Port[1]);
	}

	@Override
	public int getPortLength() throws DatabaseException {
		return ports.size();
	}

	@Override
	public void insertPort(Port port) throws DatabaseException {
		ports.put(port.getModelName() + port.getPortName(), new Port(port.getModelName(), port.getPortName(), port.getDescription(), port.getTypeName(), port.getUnit(), port.getDirection(), port.isMicdConsistency()));
	}

	@Override
	public void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException {
		connections.put(modelName + portName, new PortConnection(modelName, portName, connectionName));
	}


}
