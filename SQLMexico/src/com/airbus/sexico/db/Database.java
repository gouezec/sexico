package com.airbus.sexico.db;

public interface Database {

	DatabaseContentHandler getContentHandler() throws DatabaseException;
	
	void commitBase() throws DatabaseException;
	
	void updateIndex() throws DatabaseException;
	
	void dropBase() throws DatabaseException;
}