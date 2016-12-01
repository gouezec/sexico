package com.airbus.sexico.db.nulldb;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;

public class NullDatabase implements Database {



	@Override
	public DatabaseContentHandler getContentHandler() throws DatabaseException {
		// TODO Auto-generated method stub
		return new NullDatabaseContentHandler();
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
