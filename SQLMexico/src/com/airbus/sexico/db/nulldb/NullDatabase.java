package com.airbus.sexico.db.nulldb;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Port;

public class NullDatabase implements Database {

	@Override
	public Port[] getAllPorts() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPortLength() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insertPort(Port port) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void commitBase() throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateIndex() throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropBase() throws DatabaseException {
		// TODO Auto-generated method stub

	}

}
