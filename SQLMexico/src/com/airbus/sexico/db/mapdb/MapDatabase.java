package com.airbus.sexico.db.mapdb;

import java.util.HashMap;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Port;

public class MapDatabase implements Database {

	@Override
	public void updateIndex() throws DatabaseException {
		// nop
		
	}

	private HashMap<String, Port> ports;
	
	private HashMap<String, PortConnection> connections;
	
	private MapDatabaseContentHandler handler;

	public MapDatabase(String name) {
		initBase();
	}

	@Override
	public DatabaseContentHandler getContentHandler() throws DatabaseException {
		return handler;
	}

	public void finalize() {
		// NOP with a map
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
		ports = new HashMap<String, Port>();
		connections = new HashMap<String, PortConnection>();
		handler = new MapDatabaseContentHandler(ports, connections);
	}
}
