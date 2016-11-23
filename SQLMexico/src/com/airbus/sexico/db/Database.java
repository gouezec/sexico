package com.airbus.sexico.db;

public interface Database {

	Port[] getAllPorts() throws DatabaseException;
	
	int getPortLength() throws DatabaseException;
	
	void insertPort(Port port) throws DatabaseException;

	void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException;

	void commitBase() throws DatabaseException;
	
	void updateIndex() throws DatabaseException;
	
	void dropBase() throws DatabaseException;
}